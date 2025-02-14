package hello.qnaboard.modelmapper;

import hello.qnaboard.constant.BoardType;
import hello.qnaboard.repository.dto.BoardUpdateForm;
import hello.qnaboard.vo.BoardVO;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/** 주의: ModelMapper는 변환할 클래스(destinationType)의 setter(세터)가 없는 필드에 대해서는 매핑이 안됨 */
public class ModelMapperTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    void map() {
        BoardVO boardVO = new BoardVO(1L, "제목", 1L, "작성자", 0L, "내용", BoardType.QUESTIONS, 0L, 0L, LocalDateTime.now(), LocalDateTime.now());

        BoardUpdateForm boardUpdateForm = BoardUpdateForm.of(boardVO);

        assertThat(boardUpdateForm.getTitle()).isEqualTo(boardVO.getTitle());
        assertThat(boardUpdateForm.getContent()).isEqualTo(boardVO.getContent());
        assertThat(boardUpdateForm.getUpdateTime()).isEqualTo(boardVO.getUpdateTime());
    }
}
