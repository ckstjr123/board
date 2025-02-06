package hello.qnaboard.service;

import hello.qnaboard.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 파일 업로드 처리
 */
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;


    public String getFullPath(String filename) {
        return this.fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {

        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(this.storeFile(multipartFile));
            }
        }

        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();

        String storeFileName = this.createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName))); // 서버에 파일 업로드
        return new UploadFile(originalFilename, storeFileName);
    }

    /**
     * @param originalFilename
     * @return 서버에 저장하는 파일명(UUID + 파일 확장자)
     */
    private String createStoreFileName(String originalFilename) {
        String ext = this.extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    /**
     * 파일 확장명 추출
     * @param originalFilename
     * @return ex) "png", "txt"
     */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
