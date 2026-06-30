package com.api.crud.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Base64;
import java.util.List;

@Service
public class S3ServiceImpl implements IS3Service {

    @Autowired
    private S3Client s3Client;

    @Override
    public String createBucket(String bucketName) {
        boolean error = false;
        try {
            CreateBucketResponse response = s3Client.createBucket(b -> b.bucket(bucketName));
            error = !response.sdkHttpResponse().isSuccessful();
        } catch (Exception e) {
            error = true;
        }
        return !error ? "Bucket creado con éxito" : "Error al crear el bucket";
    }

    @Override
    public String checkIfBucketExist(String bucketName) {
        try {
            s3Client.headBucket(b -> b.bucket(bucketName));
            return "El bucket " + bucketName + " existe";
        } catch (S3Exception e) {
            return "El bucket " + bucketName + " no existe";
        }
    }

    @Override
    public List<String> listAllBuckets() {
        ListBucketsResponse response = s3Client.listBuckets();
        if (response.hasBuckets()) {
            return response.buckets().stream().map(Bucket::name).toList();
        }
        return List.of();
    }

    @Override
    public Boolean uploadFile(String bucketName, String key, String base64) {
        byte[] imageBytes = Base64.getDecoder().decode(base64);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("image/png")
                .build();
        PutObjectResponse response = s3Client.putObject(request, RequestBody.fromBytes(imageBytes));
        return response.sdkHttpResponse().isSuccessful();
    }
}
