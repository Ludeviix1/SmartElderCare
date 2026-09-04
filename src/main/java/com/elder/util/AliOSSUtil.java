package com.elder.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public final class AliOSSUtil {

    private AliOSSUtil() {
    }

    public static String uploadFile(String objectName, InputStream inputStream) {
        String endpoint = required("OSS_ENDPOINT");
        String accessKeyId = required("OSS_ACCESS_KEY_ID");
        String accessKeySecret = required("OSS_ACCESS_KEY_SECRET");
        String bucketName = required("OSS_BUCKET_NAME");

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(new PutObjectRequest(bucketName, objectName, inputStream));
            return "https://" + bucketName + "." + endpoint.substring(endpoint.lastIndexOf('/') + 1) + "/" + objectName;
        } catch (Exception e) {
            log.error("OSS upload failed for object={}", objectName, e);
            throw new IllegalStateException("文件上传失败，请稍后重试", e);
        } finally {
            ossClient.shutdown();
        }
    }

    public static void deleteFile(String url) {
        String endpoint = required("OSS_ENDPOINT");
        String accessKeyId = required("OSS_ACCESS_KEY_ID");
        String accessKeySecret = required("OSS_ACCESS_KEY_SECRET");
        String bucketName = required("OSS_BUCKET_NAME");
        String objectName = url.substring(url.lastIndexOf('/') + 1);

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.deleteObject(bucketName, objectName);
        } catch (Exception e) {
            log.error("OSS delete failed for object={}", objectName, e);
            throw new IllegalStateException("文件删除失败，请稍后重试", e);
        } finally {
            ossClient.shutdown();
        }
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(name + " must be configured");
        }
        return value.trim();
    }
}
