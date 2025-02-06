package hello.qnaboard.controller;

import hello.qnaboard.domain.UploadFile;
import hello.qnaboard.service.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 첨부파일 업로드 처리
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FilesController {

    private final FileStore fileStore;

    @GetMapping("/{storeFilename}")
    public Resource file(@PathVariable String storeFilename) throws MalformedURLException {
        //ex) "file:C:/files/767f6f7b-9dc1-44e1-8335-7b9323b60967.png"
        return new UrlResource("file:" + this.fileStore.getFullPath(storeFilename)); // 실제 파일 경로에 접근해서 파일 찾아옴
    }

    @PostMapping("/upload")
    public List<String> saveFiles(@RequestParam List<MultipartFile> file) throws IOException {
        log.info("MultipartFile={}", file);

        List<UploadFile> attachFiles = this.fileStore.storeFiles(file);

        List<String> attachFileUrls = new ArrayList<>();
        for (UploadFile attachFile : attachFiles) {
            attachFileUrls.add("/files/" + attachFile.getStoreFileName());
        }

        return attachFileUrls;
    }

}
