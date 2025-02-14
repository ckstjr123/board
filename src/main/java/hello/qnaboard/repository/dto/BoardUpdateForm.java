package hello.qnaboard.repository.dto;

import hello.qnaboard.vo.BoardVO;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 게시물 수정용 dto
 */
@Getter @Setter
public class BoardUpdateForm {

    @NotBlank(message = "게시글 제목을 작성해주세요. ")
    @Size(max = 40, message = "게시글 제목은 최대 40자입니다.")
    private String title;

    @NotBlank(message = "게시글 내용을 작성해주세요.")
    @Size(max = 10000, message = "게시글 내용은 최대 10,000자입니다.")
    private String content;

    private LocalDateTime updateTime; // update_time

    public BoardUpdateForm() {
        this.updateTime = LocalDateTime.now();
    }


    private static final ModelMapper modelMapper = new ModelMapper();

    /**
     * BoardVO → BoardUpdateForm
     * @param boardVO
     * @return boardUpdateForm
     */
    public static BoardUpdateForm of(BoardVO boardVO) {
        return modelMapper.map(boardVO, BoardUpdateForm.class);
    }
}
