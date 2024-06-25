package com.namo.spring.core.infra.common.aws.s3;

import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.namo.spring.core.infra.config.AwsS3Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {
	private final AmazonS3Client amazonS3Client;
	private final AwsS3Config awsS3Config;

	public void uploadFile(InputStream inputStream, ObjectMetadata objectMeTadata, String fileName) {
		amazonS3Client.putObject(
			new PutObjectRequest(awsS3Config.getBucketName(), fileName, inputStream, objectMeTadata)
				.withCannedAcl(CannedAccessControlList.PublicRead)
		);
	}

	public String getFileUrl(String fileName) {
		return amazonS3Client.getUrl(awsS3Config.getBucketName(), fileName).toString();
	}

	public void delete(String key) {
		//Delete 객체 생성
		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(awsS3Config.getBucketName(), key);
		//Delete
		amazonS3Client.deleteObject(deleteObjectRequest);
	}
}
