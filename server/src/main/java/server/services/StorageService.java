package server.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StorageService {

    @Value("${jsa.s3.bucket}")
    private String bucketName;

    private final AmazonS3 client;

    public StorageService(AmazonS3 client) {
        this.client = client;
    }

    public void uploadFile(@NotNull String keyName, @NotNull String data) {
        try {
            Path uploadFilePath = Files.createTempFile(keyName, "jpg");
            byte[] bytes = DatatypeConverter.parseBase64Binary(data);
            Files.write(uploadFilePath, bytes);
            client.putObject(bucketName, keyName, uploadFilePath.toFile());
        } catch (IOException ignored) {}
    }

    public byte[] downloadFile(@NotNull String keyName) {
        S3Object object = client.getObject(bucketName, keyName);
        InputStream data = object.getObjectContent();
        try {
            return data.readAllBytes();
        } catch (IOException ignored) {}
        return null;
    }

    public void removeFile(@NotNull String keyName) {
        client.deleteObject(bucketName, keyName);
    }
}
