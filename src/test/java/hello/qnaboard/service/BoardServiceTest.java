package hello.qnaboard.service;

import hello.qnaboard.constant.BoardType;
import hello.qnaboard.controller.dto.MemberJoinForm;
import hello.qnaboard.domain.Board;
import hello.qnaboard.domain.Comment;
import hello.qnaboard.domain.Member;
import hello.qnaboard.exception.AccessDeniedException;
import hello.qnaboard.repository.BoardMapper;
import hello.qnaboard.repository.CommentMapper;
import hello.qnaboard.repository.MemberMapper;
import hello.qnaboard.repository.dto.BoardUpdateForm;
import hello.qnaboard.repository.vo.BoardVO;
import hello.qnaboard.repository.vo.CommentVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("게시물 상세 조회")
    void boardDetail() {
        //given
        Member writer = this.createWriter();
        Long boardId = this.postBoard(writer);
        List<Long> commentIds = new ArrayList<>();
        commentIds.add(this.leaveComment(boardId, writer));
        commentIds.add(this.leaveComment(boardId, writer));

        //when
        BoardWithCommentsVO boardWithCommentsVO = this.boardService.boardDetail(boardId);

        //then
        BoardVO boardVO = boardWithCommentsVO.getBoardVO();
        assertThat(boardVO.getId()).isEqualTo(boardId);
        assertThat(boardVO.getView() + 1L).isEqualTo(1L); // 먼저 게시물이 있는지 조회해서 boardVO를 생성한 뒤 조회수를 올리기 때문

        List<CommentVO> commentVoList = boardWithCommentsVO.getCommentVoList();
        List<Long> commentVoIds = new ArrayList<>();
        for (CommentVO commentVO : commentVoList) {
            commentVoIds.add(commentVO.getId());
        }
        assertThat(commentVoIds).isEqualTo(commentIds);
    }

    @Test
    @DisplayName("찾는 게시글이 존재하지 않으면 예외")
    void boardNotExists() {
        assertThatThrownBy(() -> this.boardService.boardDetail(null))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("게시물 권한 검증")
    void validateBoard() {
        //given
        Member writer = this.createWriter();
        Long boardId = this.postBoard(writer);
        Member member = Member.createMember("비작성자", "user@gmail.com", "password4321!");
        this.memberMapper.save(member);

        // 검증을 통과하면 boardVO 객체 반환
        assertThat(this.boardService.validateBoard(boardId, writer.getId())).isInstanceOf(BoardVO.class);

        Long memberId = member.getId();
        assertThatThrownBy(() -> this.boardService.validateBoard(boardId, memberId))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("게시글 수정")
    void edit() {
        //given
        Member writer = this.createWriter();
        Long boardId = this.postBoard(writer);

        //when
        BoardUpdateForm updateParam = new BoardUpdateForm();
        updateParam.setTitle("수정한 제목");
        updateParam.setContent("수정한 내용");
        this.boardService.edit(boardId, updateParam, writer.getId());

        //then
        BoardVO editBoardVO = this.boardMapper.findById(boardId).orElseThrow();
        assertThat(editBoardVO.getTitle()).isEqualTo(updateParam.getTitle());
        assertThat(editBoardVO.getContent()).isEqualTo(updateParam.getContent());
        assertThat(editBoardVO.getUpdateTime()).isEqualTo(updateParam.getUpdateTime());
    }


    private Member createWriter() {
        MemberJoinForm memberJoinForm = new MemberJoinForm();
        memberJoinForm.setName("작성자");
        memberJoinForm.setEmail("test@gmail.com");
        memberJoinForm.setPassword("password1234!");

        Member member = Member.createMember(memberJoinForm.getName(), memberJoinForm.getEmail(), memberJoinForm.getPassword());
        this.memberMapper.save(member);
        return member;
    }

    private Long postBoard(Member writer) {
        BoardWriteForm boardWriteForm = new BoardWriteForm("게시글 제목", "게시글 내용"); // 작성할 게시물
        Board board = Board.createBoard(BoardType.FREE, boardWriteForm.getTitle(), boardWriteForm.getContent(), writer);
        this.boardMapper.save(board);
        return board.getId();
    }

    private Long leaveComment(Long boardId, Member writer) {
        Comment comment = Comment.createComment(boardId, writer, "답글 내용");
        this.commentMapper.save(comment);
        return comment.getId();
    }
}