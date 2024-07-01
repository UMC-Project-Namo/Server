package com.namo.spring.application.external.global.common.security.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetailsService;
import com.namo.spring.application.external.global.common.security.jwt.access.AccessTokenClaimKeys;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.JwtException;
import com.namo.spring.core.infra.common.jwt.JwtClaims;
import com.namo.spring.core.infra.common.jwt.JwtProvider;
import com.namo.spring.db.redis.cache.forbidden.ForbiddenTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtAuthenticationFilter는 HTTP 요청에 대한 JWT 인증을 처리하는 필터입니다. <br/>
 * 이 필터는 각 요청이 들어올 때마다 한 번씩 실행되며, JWT 토큰을 확인하고 유효성을 검사합니다. <br/>
 *
 * <p>JWT 토큰은 HTTP 요청의 'Authorization' 헤더에서 추출됩니다. 토큰이 존재하고 유효한 경우,
 * 토큰에서 사용자 정보를 추출하고, 이를 사용하여 {@link SecurityContextHolder}에 인증 정보를 설정합니다.
 * 이렇게 설정된 인증 정보는 이후 요청 처리 과정에서 사용됩니다.
 *
 * <p>{@link SecurityContextHolder}에 등록된 사용자에 대해서 {@code Controller}에서 {@code @AuthenticationPrincipal} 어노테이션을 통해 접근할 수 있다.
 *
 * <pre>
 * {@code
 *    @GetMapping("/user")
 *    public ResponseEntity<User> getUser(@AuthenticationPrincipal SecurityUser user) {
 *    Long id = user.getId();
 *        ...
 *    }
 * }
 * </pre>
 *
 * <p>이 필터는 다음과 같은 경우에 에러를 로그로 출력하고, 추가적인 에러 핸들러를 호출합니다: <br/>
 * - 토큰이 없는 경우 <br/>
 * - 토큰이 금지된 토큰 목록에 있는 경우 <br/>
 * - 토큰이 만료된 경우 <br/>
 *
 * @see org.springframework.security.core.annotation.AuthenticationPrincipal
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final SecurityUserDetailsService securityUserDetailsService;
	private final ForbiddenTokenService forbiddenTokenService;

	private final JwtProvider accessTokenProvider;

	/**
	 * HTTP 요청과 응답을 처리하는 메서드입니다.
	 * 이 메서드는 요청에서 JWT 토큰을 추출하고, 토큰의 유효성을 검사한 후, 사용자를 인증합니다.
	 *
	 * @param request HTTP 요청
	 * @param response HTTP 응답
	 * @param filterChain 필터 체인
	 * @throws ServletException 서블릿 예외
	 * @throws IOException 입출력 예외
	 */
	@Override
	protected void doFilterInternal(
		@NotNull HttpServletRequest request,
		@NotNull HttpServletResponse response,
		@NotNull FilterChain filterChain
	) throws ServletException, IOException {
		String accessToken = resolveAccessToken(request);
		UserDetails userDetails = getUserDetails(accessToken);
		authenticateUser(userDetails, request);

		filterChain.doFilter(request, response);
	}

	/**
	 * HTTP 요청에서 JWT 토큰을 추출하는 메서드입니다.
	 * 이 메서드는 'Authorization' 헤더에서 JWT 토큰을 추출하고, 토큰의 유효성을 검사합니다.
	 *
	 * @param request HTTP 요청
	 * @return JWT 토큰
	 * @throws ServletException 서블릿 예외
	 */
	private String resolveAccessToken(
		HttpServletRequest request
	) throws ServletException {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String token = accessTokenProvider.resolveToken(authHeader);

		if (!StringUtils.hasText(token)) {
			log.error("EMPTY_ACCESS_TOKEN");
			handleAuthException(ErrorStatus.EMPTY_ACCESS_KEY);
		}

		if (forbiddenTokenService.isForbidden(token)) {
			log.error("FORBIDDEN_ACCESS_TOKEN");
			handleAuthException(ErrorStatus.FORBIDDEN_ACCESS_TOKEN);
		}

		if (accessTokenProvider.isTokenExpired(token)) {
			log.error("EXPIRED_TOKEN");
			handleAuthException(ErrorStatus.EXPIRATION_TOKEN);
		}

		return token;
	}

	/**
	 * 주어진 JWT 토큰에서 사용자 정보를 추출하는 메서드입니다. <br/>
	 * 이 메서드는 JWT 토큰에서 사용자 ID를 추출하고, 이를 사용하여 사용자의 상세 정보를 로드합니다.
	 *
	 * @param accessToken JWT 토큰
	 * @return 사용자의 상세 정보. 사용자를 찾을 수 없는 경우 null을 반환합니다.
	 */
	private UserDetails getUserDetails(String accessToken) {
		JwtClaims claims = accessTokenProvider.parseJwtClaimsFromToken(accessToken);
		String userId = (String)claims.getClaims().get(AccessTokenClaimKeys.USER_ID.getValue());
		return securityUserDetailsService.loadUserByUserId(Long.parseLong(userId));
	}

	/**
	 * 주어진 사용자 정보를 사용하여 사용자를 인증하는 메서드입니다. <br/>
	 * 이 메서드는 사용자의 상세 정보를 사용하여 인증 토큰을 생성하고, 이를 {@link SecurityContextHolder}에 설정합니다. <br/>
	 * 이렇게 설정된 인증 정보는 이후 요청 처리 과정에서 사용됩니다.
	 *
	 * @param userDetails 사용자의 상세 정보
	 * @param request HTTP 요청
	 */
	private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			userDetails, null, userDetails.getAuthorities()
		);

		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

	private void handleAuthException(ErrorStatus errorStatus) throws ServletException {
		JwtException exception = new JwtException(errorStatus);
		throw new ServletException(exception);
	}
}
