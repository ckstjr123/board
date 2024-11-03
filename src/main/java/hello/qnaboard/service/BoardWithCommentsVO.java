package hello.qnaboard.service;

import hello.qnaboard.repository.vo.BoardVO;
import hello.qnaboard.repository.vo.CommentVO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 게시물 상세 페이지에 전달할 게시물 및 답글 VO.
 * Nested Select 방식으로 각각 조회한 BoardVO와 List<CommentVO>를 합쳐서 반환하기 위한 용도
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
