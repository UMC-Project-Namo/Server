package com.namo.spring.core.infra.common.aws.s3;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.UtilsException;
import com.namo.spring.core.infra.common.constant.FilePath;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtils {
    private final S3Uploader s3Uploader;
    private static final int TARGET_HEIGHT = 800;

    private String createFileName(String originalFileName, FilePath filePath) {
        return filePath.getPath() + UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new UtilsException(ErrorStatus.FILE_NAME_EXCEPTION);
        }
    }

    public List<String> uploadImages(List<MultipartFile> files, FilePath filePath) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (Optional.ofNullable(file).isPresent()) {
                urls.add(uploadImage(file, filePath));
            }
        }
        return urls;
    }

    public String uploadImage(MultipartFile file, FilePath filePath) {
        String fileName = createFileName(file.getOriginalFilename(), filePath);
        ObjectMetadata objectMetadata = new ObjectMetadata();

        try {
            BufferedImage resizedImage = resizeImage(file);
            ByteArrayInputStream byteArrayInputStream = convertImage(resizedImage, file.getContentType(),
                    getFileExtension(file.getOriginalFilename()), objectMetadata);
            s3Uploader.uploadFile(byteArrayInputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new UtilsException(ErrorStatus.S3_UPLOAD_FAILURE);
        }

        return s3Uploader.getFileUrl(fileName);
    }

    private BufferedImage resizeImage(MultipartFile multipartFile) throws IOException {
        BufferedImage sourceImage = ImageIO.read(multipartFile.getInputStream());

        if (sourceImage.getHeight() <= TARGET_HEIGHT) {
            return sourceImage;
        }

        double sourceImageRatio = (double)sourceImage.getWidth() / sourceImage.getHeight();
        int newWidth = (int)(TARGET_HEIGHT * sourceImageRatio);

        return Scalr.resize(sourceImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, newWidth, TARGET_HEIGHT);
    }

    private ByteArrayInputStream convertImage(BufferedImage croppedImage, String contentType, String fileFormat,
            ObjectMetadata objectMetadata) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(croppedImage, fileFormat.replace(".", ""), byteArrayOutputStream);

        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(byteArrayOutputStream.size());

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public void deleteImages(List<String> urls, FilePath filePath) {
        try {
            for (String url : urls) {
                delete(url, filePath);
            }
        } catch (SdkClientException e) {
            throw new UtilsException(ErrorStatus.S3_DELETE_FAILURE);
        }
    }

    public void delete(String url, FilePath filePath) {
        try {
            String key = url.substring(url.lastIndexOf(filePath.getPath()));
            if (!key.isEmpty()) {
                s3Uploader.delete(key);
            }

            String resizedKey = url.replace("origin", "resized/origin");
            if (!resizedKey.isEmpty()) {
                // 리사이즈된 이미지 삭제
                s3Uploader.delete(resizedKey);
            }
        } catch (SdkClientException e) {
            throw new UtilsException(ErrorStatus.S3_DELETE_FAILURE);
        }
    }
}
