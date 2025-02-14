package hello.qnaboard.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 게시물 상세 페이지에 전달할 게시물 및 답글 VO.
 * 각각 조회한 BoardVO와 List<CommentVO>를 합쳐서 반환하는 용도
 */
@Getter
@EqualsAndHashCode
@ToString
public final class BoardWithCommentsVO {

    private final BoardVO boardVO;
    private final List<CommentVO> commentVoList;

    public BoardWithCommentsVO(BoardVO boardVO, List<CommentVO> commentVoList) {
        this.boardVO = boardVO;
        this.commentVoList = commentVoList;
    }
}
