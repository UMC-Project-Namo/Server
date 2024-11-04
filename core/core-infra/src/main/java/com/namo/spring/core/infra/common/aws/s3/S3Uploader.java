package com.namo.spring.core.infra.common.aws.s3;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.namo.spring.core.infra.common.aws.dto.MultipartCompleteRequest;
import com.namo.spring.core.infra.common.aws.dto.MultipartStartResponse;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.core.infra.config.AwsS3Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;
    private final AwsS3Config awsS3Config;

    // v1
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
     * Part 업로드를 위한 PresignedURLs 발급
     * @param prefix
     * @param fileName
     * @param partCount
     * @return
     */
    public MultipartStartResponse getPreSignedUrls(String prefix, String fileName, int partCount) {
        String uniqueFileName = createPath(prefix, fileName);
        String uploadId = initiateMultipartUpload(uniqueFileName);
        List<URL> presignedUrls = generatePartPresignedUrls(uniqueFileName, uploadId, partCount);
        return new MultipartStartResponse(uploadId, presignedUrls, uniqueFileName);
    }

    /**
     * S3에 Multipart Upload를 초기화하고 uploadId를 반환
     */
    private String initiateMultipartUpload(String fileName) {
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(awsS3Config.getBucketName(), fileName)
                .withCannedACL(CannedAccessControlList.PublicRead);
        InitiateMultipartUploadResult initResult = amazonS3Client.initiateMultipartUpload(initRequest);

        return initResult.getUploadId();
    }

    /**
     * 각 파트별 업로드를 위한 presigned URL 목록 생성
     */
    private List<URL> generatePartPresignedUrls(String fileName, String uploadId, int partCount) {
        return IntStream.rangeClosed(1, partCount)
                .mapToObj(partNumber -> generatePresignedUrl(fileName, uploadId, partNumber))
                .collect(Collectors.toList());
    }

    /**
     * 단일 파트에 대한 presigned URL 생성
     */
    private URL generatePresignedUrl(String fileName, String uploadId, int partNumber) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(
                awsS3Config.getBucketName(), fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPreSignedUrlExpiration());

        request.addRequestParameter("partNumber", String.valueOf(partNumber));
        request.addRequestParameter("uploadId", uploadId);

        return amazonS3Client.generatePresignedUrl(request);
    }

    /**
     * presigned url 발급
     *
     * @param prefix   버킷 디렉토리 이름
     * @param fileName 클라이언트가 전달한 파일명 파라미터
     * @return presigned url
     */
    public String getPreSignedUrl(String prefix, String fileName) {
        if (!prefix.isEmpty())
            fileName = createPath(prefix, fileName);

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
        String filepath = FilePath.getPathForPrefix(prefix);

        return String.format("%s/%s", filepath, fileId + fileName);
    }

    /**
     * part 업로드 완료 처리 (합침, 태깅으로 삭제 방지)
     * @return
     */
    public String completeMultipartUpload(MultipartCompleteRequest request) {
        CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest(
                awsS3Config.getBucketName(),
                request.getFileName(),
                request.getUploadId(),
                request.getPartETags()
        );

        return amazonS3Client.completeMultipartUpload(completeRequest).getLocation();
    }
}


