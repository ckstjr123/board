package hello.qnaboard.repository;

import hello.qnaboard.domain.Comment;
import hello.qnaboard.repository.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * comment(댓글 or 답변) DAO
 */
@Mapper
public interface CommentMapper {

    void save(Comment comment);

    Optional<CommentVO> findById(Long id); // Comment 단일 조회

    /**
     * 특정 게시물에 달린 모든 댓글 조회
     * @param boardId
     * @return commentVoList
     */
    List<CommentVO> findByBoardId(Long boardId);
}
