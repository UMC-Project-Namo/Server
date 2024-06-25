package com.namo.spring.core.infra.common.aws.s3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.UtilsException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtils {
	private final S3Uploader s3Uploader;

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
		ObjectMetadata objectMetadata = getObjectMetadata(file);

		try (InputStream inputStream = file.getInputStream()) {
			s3Uploader.uploadFile(inputStream, objectMetadata, fileName);
		} catch (IOException e) {
			throw new UtilsException(ErrorStatus.S3_FAILURE);
		}

		return s3Uploader.getFileUrl(fileName);
	}

	private static ObjectMetadata getObjectMetadata(MultipartFile file) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(file.getSize());
		objectMetadata.setContentType(file.getContentType());
		return objectMetadata;
	}

	public void deleteImages(List<String> urls, FilePath filePath) {
		try {
			for (String url : urls) {
				delete(url, filePath);
			}
		} catch (SdkClientException e) {
			throw new UtilsException(ErrorStatus.S3_FAILURE);
		}
	}

	private void delete(String url, FilePath filePath) {
		try {
			String key = url.substring(url.lastIndexOf(filePath.getPath()));
			if (!key.isEmpty()) {
				s3Uploader.delete(key);
			}
		} catch (SdkClientException e) {
			throw new UtilsException(ErrorStatus.S3_FAILURE);
		}
	}
}
