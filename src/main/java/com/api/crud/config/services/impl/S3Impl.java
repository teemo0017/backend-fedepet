package com.api.crud.config.services.impl;

import com.api.crud.config.services.interfaces.IS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Base64;
import java.util.List;

@Service
public class S3Impl implements IS3Service {

    @Autowired
    private S3Client s3Client;

    @Override
    public String createBucket(String bucketName) {
        boolean error = false;
        try {
            CreateBucketResponse createBucketResponse = this.s3Client.createBucket(bucketBilder -> bucketBilder.bucket(bucketName));
            error = !createBucketResponse.sdkHttpResponse().isSuccessful();
        } catch (Exception e) {
            System.out.println("Error al crear el bucket -> " + e);
            error = true;
        }
        return !error ? "Bucket Creado con exito!" : "Error al Crear Bucket";
    }

    @Override
    public String checkIfBucketExist(String bucketName) {

        try {
            this.s3Client.headBucket(headBucket -> headBucket.bucket(bucketName));
            return "El bucket " + bucketName + "Si existe";
        } catch (S3Exception exception) {
            return "El bucket " + bucketName + "No existe";
        }
    }

    @Override
    public List<String> listAllBuckets() {
        ListBucketsResponse bucketsResponse = this.s3Client.listBuckets();
        if (bucketsResponse.hasBuckets()) {
            return bucketsResponse.buckets()
                    .stream()
                    .map(Bucket::name)
                    .toList();
        }

        return List.of();
    }

    @Override
    public Boolean uploadFile(String bucketName, String key, String base64) {
        byte[] imageBytes = Base64.getDecoder().decode(base64);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("image/png")
                .build();

        PutObjectResponse putObjectResponse = this.s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageBytes));

        return putObjectResponse.sdkHttpResponse().isSuccessful();
    }
}
