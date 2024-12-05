package com.namo.spring.core.infra.config;

import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.Getter;

@Configuration
public class AwsS3Config {
    private final String accessKey;
    private final String secretKey;
    private final String region;
    @Getter
    private final String bucketName;
    @Getter
    private final String cdnPath;

    public AwsS3Config(
            @Value("${cloud.aws.credentials.access-key}") String accessKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.region.static}") String region,
            @Value("${cloud.aws.s3.bucket}") String bucketName,
            @Value("${cloud.aws.cloudfront}") String cdnName
    ) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.bucketName = bucketName;
        this.cdnPath = cdnName;
    }

    @Bean
    public AWSCredentials awsS3Credentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    public AmazonS3Client amazonS3Client() {
        return (AmazonS3Client)AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsS3Credentials()))
                .build();
    }

    public String getS3Domain(){
        return String.format("%s.s3.%s.amazonaws.com", bucketName, region);
    }
}


