package hello.qnaboard.service;

import hello.qnaboard.constant.BoardType;
import hello.qnaboard.domain.Board;
import hello.qnaboard.domain.Member;
import hello.qnaboard.exception.AccessDeniedException;
import hello.qnaboard.exception.IllegalMemberAccessException;
import hello.qnaboard.repository.BoardMapper;
import hello.qnaboard.repository.CommentMapper;
import hello.qnaboard.repository.dto.BoardSearchCond;
import hello.qnaboard.repository.dto.BoardUpdateForm;
import hello.qnaboard.vo.BoardListItem;
import hello.qnaboard.vo.BoardVO;
import hello.qnaboard.vo.CommentVO;
import hello.qnaboard.vo.BoardWithCommentsVO;
import hello.qnaboard.vo.pagination.Page;
import hello.qnaboard.vo.pagination.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final MemberService memberService;
    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;


    /**
     * 게시글 목록 페이지
     * @param boardType
     * @param pageable
     * @param boardSearchCond
     * @return {@code Page<BoardListItem>}
     */
    @Transactional
    public Page<BoardListItem> boardListPage(BoardType boardType, Pageable pageable, BoardSearchCond boardSearchCond) {

        // 해당 게시판 게시글 수(검색 타겟 및 검색 키워드가 존재하면 검색 조건에 부합하는 게시글)
        long total = StringUtils.hasText(boardSearchCond.getSearchTarget()) && StringUtils.hasText(boardSearchCond.getSearchKeyword()) ?
                this.boardMapper.countByBoardTypeAndSearchCond(boardType, boardSearchCond) : this.boardMapper.countByBoardType(boardType);

        // 해당 게시판 게시글 목록 페이지
        List<BoardListItem> content = this.boardMapper.findBoardListWithPaging(boardType, pageable, boardSearchCond);

        return new Page<>(content, pageable, total, 3);
    }

    /**
     * 게시글 등록
     * @param boardType
     * @param boardWriteForm
     * @param memberId
     * @return {@code Long} boardId
     */
    @Transactional(readOnly = false)
    public Long post(BoardType boardType, BoardWriteForm boardWriteForm, Long memberId) {
        Member writer = this.memberService.findById(memberId)
                .orElseThrow(IllegalMemberAccessException::new);

        Board board = Board.createBoard(boardType, boardWriteForm.getTitle(), boardWriteForm.getContent(), writer);
        this.boardMapper.save(board); // 게시글 등록
        return board.getId(); // 작성된 게시글 id
    }

    /**
     * 게시물 상세(게시글, 댓글)
     * @param boardId
     * @return {@code BoardWithCommentsVO} or 게시글이 존재하지 않으면 null
     */
    @Transactional(readOnly = false)
    public BoardWithCommentsVO boardDetail(Long boardId) {
        BoardVO boardVO = this.boardMapper.findById(boardId).orElseThrow(() -> new EmptyResultDataAccessException("게시물이 존재하지 않거나 삭제되었습니다.", 1));
        this.boardMapper.upView(boardId); // 조회수 up
        List<CommentVO> commentVOList = this.commentMapper.findByBoardId(boardId);

        return new BoardWithCommentsVO(boardVO, commentVOList);
    }

    /**
     * 게시물 검증 통과 시 UPDATE
     * @param boardId 수정할 게시물 id
     * @param updateParam
     * @param memberId 게시물 권한 검증 용도
     */
    @Transactional(readOnly = false)
    public void edit(Long boardId, BoardUpdateForm updateParam, Long memberId) {
        this.validateBoard(boardId, memberId);
        this.boardMapper.update(boardId, updateParam);
    }


    /**
     * 게시글 존재 여부 및 권한 검증.
     * 로그인 사용자가 해당 게시글 작성자면 게시글 VO 반환
     * @param boardId
     * @param memberId
     * @return {@code BoardVO}
     * @throws IllegalMemberAccessException
     * @throws EmptyResultDataAccessException 대상 게시물이 존재하지 않음
     * @throws AccessDeniedException 해당 게시물에 대한 권한 없음
     */
    @Transactional
    public BoardVO validateBoard(Long boardId, Long memberId) {
        Member member = this.memberService.findById(memberId).orElseThrow(IllegalMemberAccessException::new);

        BoardVO boardVO = this.boardMapper.findById(boardId)
                .orElseThrow(() -> new EmptyResultDataAccessException("게시물을 찾을 수 없습니다.", 1));

        if (boardVO.getWriterId().equals(member.getId())) {
            return boardVO;
        } else {
            throw new AccessDeniedException("해당 게시물에 대한 권한이 없습니다.");
        }
    }

}
