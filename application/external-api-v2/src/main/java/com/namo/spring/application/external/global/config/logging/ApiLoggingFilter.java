package com.namo.spring.application.external.global.config.logging;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		if (isAsyncDispatch(request)) {
			filterChain.doFilter(request, response);
		} else {
			doFilterWrapped(
				new ContentCachingRequestWrapper(request),
				new ContentCachingResponseWrapper(response),
				filterChain
			);
		}
	}

	protected void doFilterWrapped(
		ContentCachingRequestWrapper request,
		ContentCachingResponseWrapper response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
			logRequest(request);
		} finally {
			logResponse(response);
			response.copyBodyToResponse();
		}
	}

	private static String getQueryString(ContentCachingRequestWrapper request) {
		String queryString = request.getQueryString();
		return queryString == null ? request.getRequestURI() : request.getRequestURI() + queryString;
	}

	private static void logRequest(ContentCachingRequestWrapper request) throws IOException {
		String queryString = getQueryString(request);
		String contentType = request.getContentType();
		String checkedContentType = contentTypeCheck(contentType);

		log.info("Request : {} uri=[{}] content-type[{}]", request.getMethod(), queryString, checkedContentType);

		if (checkedContentType.split(";")[0].equals("multipart/form-data")) {
			String multipartContent = getMultipartContent(request);
			logPayload("Request", checkedContentType, multipartContent.getBytes());
		} else {
			logPayload("Request", checkedContentType, request.getContentAsByteArray());
		}

	}

	private static String getMultipartContent(ContentCachingRequestWrapper request) {
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(
			request.getServletContext());
		MultipartResolver multipartResolver = context.getBean(MultipartResolver.class);
		MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart(request);
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		StringBuilder mapToString = new StringBuilder("{");

		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile file = entry.getValue();
			mapToString.append(
				String.format(
					"\"%s\": \"%s (size: %s)\",",
					entry.getKey(), file.getOriginalFilename(), file.getSize()
				)
			);
		}

		return mapToString.substring(0, mapToString.length() - 1) + "}";
	}

	private static void logResponse(ContentCachingResponseWrapper response) throws IOException {
		String contentType = contentTypeCheck(response.getContentType());
		logPayload("Response", contentType, response.getContentAsByteArray());
	}

	private static void logPayload(String prefix, String contentType, byte[] rowData) throws IOException {
		boolean visible = isVisible(MediaType.valueOf(contentType));

		if (visible) {
			if (rowData.length > 0) {
				String json = getJsonString(rowData);
				log.info("{} Payload: {}", prefix, json);
			}
		} else {
			log.info("{} Payload: Binary Content", prefix);
		}
	}

	private static String contentTypeCheck(String contentType) {
		String nullCheck = contentType == null ? "application/json" : contentType;
		return nullCheck.isEmpty() ? "application/json" : nullCheck;
	}

	private static String getJsonString(byte[] rowData) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		Object jsonObject = objectMapper.readValue(new String(rowData), Object.class);
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
	}

	private static boolean isVisible(MediaType mediaType) {
		final List<MediaType> visibleTypes = Arrays.asList(
			MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML,
			MediaType.valueOf("application/*+json"),
			MediaType.valueOf("application/*+xml"),
			MediaType.MULTIPART_FORM_DATA
		);

		return visibleTypes.stream()
			.anyMatch(visibleType -> visibleType.includes(mediaType));
	}
}
