package hello.qnaboard.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 업로드 파일 정보
 */
@AllArgsConstructor
@Getter
public class UploadFile {
    
    private String uploadFileName;
    private String storeFileName; // 서버에 저장하는 파일명
}
