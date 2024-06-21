package com.namo.spring.application.external.global.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.google.gson.Gson;
import com.namo.spring.application.external.api.user.helper.JwtAuthHelper;
import com.namo.spring.application.external.global.common.security.jwt.JwtClaimsParserUtil;
import com.namo.spring.application.external.global.common.security.jwt.access.AccessTokenClaimKeys;
import com.namo.spring.core.common.exception.AuthException;
import com.namo.spring.core.infra.common.jwt.JwtClaims;
import com.namo.spring.core.infra.common.jwt.JwtProvider;
import com.namo.spring.db.redis.cache.forbidden.ForbiddenTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

	private final JwtProvider accessTokenProvider;
	private final JwtAuthHelper jwtAuthHelper;
	private final ForbiddenTokenService forbiddenTokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		String accessToken = jwtAuthHelper.getAccessToken(request);
		JwtClaims accessTokenClaims = accessTokenProvider.parseJwtClaimsFromToken(accessToken);
		Long userId = JwtClaimsParserUtil.getClaimValue(accessTokenClaims, AccessTokenClaimKeys.USER_ID.getValue(), Long.class);

		try {
			validateLogout(accessToken);
			request.setAttribute("userId", userId);
			return true;
		} catch (AuthException e) {
			Gson gson = new Gson();
			String exception = gson.toJson(e.getErrorReasonHttpStatus());
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(403);
			response.getWriter().print(exception);
			return false;
		}
	}

	private void validateLogout(String accessToken) {
		forbiddenTokenService.isForbidden(accessToken);
	}
}
