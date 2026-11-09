package com.api.crud.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3Config {
    @Value("${aws.access.key}")
    private String awsAccessKey;
    @Value("${aws.secret.key}")
    private String secretKey;
    @Value("${aws.region}")
    private String region;

    @Bean
    public S3Client getS3Client() {
        AwsCredentials credentials = AwsBasicCredentials.create(awsAccessKey, secretKey);
        Region awsRegion = Region.of(region);
        return S3Client.builder()
                .region(awsRegion)
                .endpointOverride(URI.create("https://s3." + region + ".amazonaws.com"))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
