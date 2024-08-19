package com.namo.spring.core.infra.common.aws.s3;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
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

	/**
	 * presigned url 발급
	 *
	 * @param prefix   버킷 디렉토리 이름
	 * @param fileName 클라이언트가 전달한 파일명 파라미터
	 * @return presigned url
	 */
	public String getPreSignedUrl(String prefix, String fileName) {
		if (!prefix.isEmpty()) {
			fileName = createPath(prefix, fileName);
		}

		GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(
			awsS3Config.getBucketName(), fileName);
		URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}

	/**
	 * 파일 업로드용(PUT) presigned url 생성
	 *
	 * @param bucket   버킷 이름
	 * @param fileName S3 업로드용 파일 이름
	 * @return presigned url
	 */
	private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest =
			new GeneratePresignedUrlRequest(bucket, fileName)
				.withMethod(HttpMethod.PUT)
				.withExpiration(getPreSignedUrlExpiration());
		generatePresignedUrlRequest.addRequestParameter(
			Headers.S3_CANNED_ACL,
			CannedAccessControlList.PublicRead.toString());
		return generatePresignedUrlRequest;
	}

	/**
	 * presigned url 유효 기간 설정
	 *
	 * @return 유효기간
	 */
	private Date getPreSignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 2;
		expiration.setTime(expTimeMillis);
		return expiration;
	}

	/**
	 * 파일 고유 ID를 생성
	 *
	 * @return 36자리의 UUID
	 */
	private String createFileId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 파일의 전체 경로를 생성
	 *
	 * @param prefix 디렉토리 경로
	 * @return 파일의 전체 경로
	 */
	private String createPath(String prefix, String fileName) {
		String fileId = createFileId();
		return String.format("%s/%s", prefix, fileId + fileName);
	}
}


