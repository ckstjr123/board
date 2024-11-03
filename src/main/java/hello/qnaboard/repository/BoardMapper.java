package hello.qnaboard.repository;

import hello.qnaboard.constant.BoardType;
import hello.qnaboard.domain.Board;
import hello.qnaboard.repository.dto.BoardSearchCond;
import hello.qnaboard.repository.dto.BoardUpdateForm;
import hello.qnaboard.repository.vo.BoardListItem;
import hello.qnaboard.repository.vo.BoardVO;
import hello.qnaboard.vo.pagination.Pageable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/** BoardDAO */
@Mapper
public interface BoardMapper {

    void save(Board board);

    //글 수정
    void update(@Param("id") Long id, @Param("updateParam") BoardUpdateForm updateParam);

    long countByBoardType(BoardType boardType);

    //검색 조건에 따른 해당 게시판 내 게시글 수 카운트
    long countByBoardTypeAndSearchCond(@Param("boardType") BoardType boardType, @Param("boardSearchCond") BoardSearchCond boardSearchCond);

    //해당 게시판 게시글을 특정 offset부터 limit만큼 조회(검색 조건 있으면 적용)
    List<BoardListItem> findBoardListWithPaging(@Param("boardType") BoardType boardType, @Param("pageable") Pageable pageable,
                                                @Param("boardSearchCond") BoardSearchCond boardSearchCond);

    //게시물 id & 게시판 유형과 일치하는 단일 게시물 조회
    Optional<BoardVO> findById(Long id);

    //해당 게시물 조회수 up
    void upView(Long id);
}
