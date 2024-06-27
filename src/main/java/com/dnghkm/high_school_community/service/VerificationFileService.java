package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.UserRegisterDto;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.entity.VerificationFile;
import com.dnghkm.high_school_community.repository.VerificationFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationFileService {
    private final VerificationFileRepository verificationFileRepository;

    private static final String FILE_DIRECTORY = "D:/Project/test_upload_file"; // 파일을 저장할 디렉토리 경로

    public void register(User user, UserRegisterDto userRegisterDto) {
        MultipartFile file = userRegisterDto.getVerificationFile();
        if (file != null && !file.isEmpty()) {
            try {
                // 파일 시스템에 저장
                String originalFileName = file.getOriginalFilename();
                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "_" + originalFileName;
                Path filePath = Paths.get(FILE_DIRECTORY, fileName);
                Files.write(filePath, file.getBytes());

                // DB에 파일 정보 저장
                VerificationFile verificationFile = VerificationFile.builder()
                        .user(user)
                        .filePath(filePath.toString())
                        .fileNameOriginal(originalFileName)
                        .fileNameUUID(fileName)
                        .fileType(file.getContentType()).build();
                verificationFileRepository.save(verificationFile);
            } catch (IOException e) {
                log.error("Failed to store file", e);
                throw new RuntimeException("Failed to store file", e);
            }
        }
    }
}