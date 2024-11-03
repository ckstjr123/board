package hello.qnaboard.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 게시글 작성 폼 DTO
 */
@NoArgsConstructor
@Getter @Setter
public class BoardWriteForm {

    @NotBlank(message = "게시글 제목을 작성해주세요. ")
    @Size(max = 40, message = "게시글 제목은 최대 40자입니다.")
    private String title;

    @NotBlank(message = "게시글 내용을 작성해주세요.")
    @Size(max = 10000, message = "게시글 내용은 최대 10,000자입니다.")
    private String content;

    public BoardWriteForm(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
