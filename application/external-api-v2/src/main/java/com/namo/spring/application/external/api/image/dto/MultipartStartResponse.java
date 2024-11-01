package com.namo.spring.application.external.api.image.dto;

import java.net.URL;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MultipartStartResponse {
    private String uploadId;
    private List<URL> presignedUrls;
    private String fileName;
}
