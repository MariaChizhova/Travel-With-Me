package com.example.demo.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@Service
public class StorageService {

    @Value("${jsa.s3.bucket}")
    private String bucketName;

    private final AmazonS3 client;

    public StorageService(AmazonS3 client) {
        this.client = client;
    }

    public void uploadFile(String keyName, Path uploadFilePath) {
        client.putObject(bucketName, keyName, uploadFilePath.toFile());
    }

    public byte[] downloadFile(String keyName) {
        S3Object object = client.getObject(bucketName, keyName);
        InputStream data = object.getObjectContent();
        try {
            return data.readAllBytes();
        } catch (IOException ignored) {}
        return null;
    }

    public void removeFile(String keyName) {
        client.deleteObject(bucketName, keyName);
    }
}
