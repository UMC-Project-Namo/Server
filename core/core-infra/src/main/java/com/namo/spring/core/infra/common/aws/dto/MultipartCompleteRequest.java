package com.namo.spring.core.infra.common.aws.dto;

import java.util.List;

import com.amazonaws.services.s3.model.PartETag;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MultipartCompleteRequest {
    private String fileName;
    private String uploadId;
    private List<PartETag> partETags;
}
