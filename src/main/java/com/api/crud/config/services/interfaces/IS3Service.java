package com.api.crud.config.services.interfaces;

import java.util.List;

public interface IS3Service {

    String createBucket(String bucketName);

    String checkIfBucketExist(String bucketName);

    List<String> listAllBuckets();

    Boolean uploadFile(String bucketName, String key, String fileLocation);

}
