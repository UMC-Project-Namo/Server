package com.namo.spring.application.external.global.config.swagger;

import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCode;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.config.properties.ExampleHolder;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class SwaggerConfig {

    private static final String API_NAME = "Namo Api V2";
    private static final String API_VERSION = "2.0.0";
    private static final String API_DESCRIPTION = "나모 프로젝트 명세서 ";
    public static final String AUTHORIZATION = "Authorization";

    @Value("${swagger.server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI getOpenAPI() {
        return new OpenAPI()
                .components(getComponents())
                .servers(List.of(getServer()))
                .security(getSecurity())
                .info(getInfo());
    }

    private static List<SecurityRequirement> getSecurity() {
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(AUTHORIZATION);

        return Collections.singletonList(securityRequirement);
    }

    /**
     * Swagger ApiErrorCodes, ApiErrorCode Annotation을 통해 에러코드를 설정할 수 있도록 하는 Customizer
     *
     * @return
     */
    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCodes apiErrorCodes = handlerMethod.getMethodAnnotation(ApiErrorCodes.class);

            // @ApiErrorCodes가 존재할 경우 해당 에러코드를 설정
            if (apiErrorCodes != null) {
                generateErrorCodeResponse(operation, apiErrorCodes.value());
            } else {
                ApiErrorCode apiErrorCode = handlerMethod.getMethodAnnotation(ApiErrorCode.class);

                // @ApiErrorCodes가 존재하지 않으며, @ApiErrorCode가 존재할 경우 해당 에러코드를 설정
                if (apiErrorCode != null) {
                    generateErrorCodeResponse(operation, apiErrorCode.value());
                }
            }

            return operation;
        };
    }

    /**
     * {@code @ApiErrorCodes} 어노테이션이 존재할 경우 Operation에 에러코드를 설정하는 메소드
     *
     * @param operation
     * @param errorCodes
     */
    private void generateErrorCodeResponse(Operation operation, ErrorStatus[] errorCodes) {
        ApiResponses responses = operation.getResponses();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(errorCodes)
                .map(
                        errorCode -> ExampleHolder.builder()
                                .example(getSwaggerExample(errorCode))
                                .name(errorCode.name())
                                .code(errorCode.getCode())
                                .build()
                )
                .collect(Collectors.groupingBy(ExampleHolder::getCode));

        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    /**
     * {@code @ApiErrorCode} 어노테이션이 존재할 경우 Operation에 에러코드를 설정하는 메소드
     *
     * @param operation
     * @param value
     */
    private void generateErrorCodeResponse(Operation operation, ErrorStatus value) {
        ApiResponses responses = operation.getResponses();
        ExampleHolder exampleHolder = ExampleHolder.builder()
                .example(getSwaggerExample(value))
                .name(value.name())
                .code(value.getCode())
                .build();
        addExamplesToResponses(responses, exampleHolder);
    }

    /**
     * {@code @ApiErrorCodes} 어노테이션이 존재할 경우 {@code ApiResponses}에 {@code Example}를 추가하는 메소드
     *
     * @param responses
     * @param statusWithExampleHolders
     */
    private void addExamplesToResponses(
            ApiResponses responses,
            Map<Integer, List<ExampleHolder>> statusWithExampleHolders
    ) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();

                    v.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.getName(),
                                    exampleHolder.getExample()
                            )
                    );

                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);
                    responses.addApiResponse(String.valueOf(status), apiResponse);
                });
    }

    /**
     * {@code @ApiErrorCode} 어노테이션이 존재할 경우 {@code ApiResponses}에 {@code Example}를 추가하는 메소드
     *
     * @param responses
     * @param exampleHolder
     */
    private void addExamplesToResponses(ApiResponses responses, ExampleHolder exampleHolder) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        ApiResponse apiResponse = new ApiResponse();

        mediaType.addExamples(exampleHolder.getName(), exampleHolder.getExample());
        content.addMediaType("application/json", mediaType);
        apiResponse.setContent(content);
        responses.addApiResponse(String.valueOf(exampleHolder.getCode()), apiResponse);
    }

    /**
     * {@code ErrorStatus}를 통해 {@code Example}를 생성하는 메소드
     *
     * @param errorCode
     * @return
     */
    private Example getSwaggerExample(ErrorStatus errorCode) {
        ResponseDto<ErrorStatus> response = ResponseDto.onFailure(errorCode.getCode(), errorCode.getMessage(), null);
        Example example = new Example();
        example.setValue(response);

        return example;
    }

    private static Components getComponents() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").name(AUTHORIZATION)
                .in(SecurityScheme.In.HEADER).name(AUTHORIZATION);

        return new Components()
                .addSecuritySchemes(AUTHORIZATION, securityScheme);
    }

    private Info getInfo() {
        return new Info()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION);
    }

    private Server getServer() {
        return new Server()
                .url(serverUrl);
    }
}
