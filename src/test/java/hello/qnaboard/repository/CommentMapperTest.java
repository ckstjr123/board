package hello.qnaboard.repository;

import hello.qnaboard.constant.BoardType;
import hello.qnaboard.controller.dto.MemberJoinForm;
import hello.qnaboard.domain.Board;
import hello.qnaboard.domain.Comment;
import hello.qnaboard.domain.Member;
import hello.qnaboard.repository.vo.CommentVO;
import hello.qnaboard.service.BoardWriteForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class CommentMapperTest {

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Test
    void save() {
        //given
        Member writer = this.getWriter();
        Long boardId = this.writeBoard(writer);

        //when
        Comment comment = Comment.createComment(boardId, writer, "답글 내용");
        this.commentMapper.save(comment);

        //then
        CommentVO commentVO = this.commentMapper.findById(comment.getId()).orElseThrow();
        log.info("savedComment: {}", comment);
        log.info("findCommentResultVO: {}", commentVO);

        //반환된 CommentVO 검증
        assertThat(commentVO.getWriterId()).isEqualTo(writer.getId()); // 답글 작성자 id
        assertThat(commentVO)
                .usingRecursiveComparison()
                .ignoringFields("writerId")
                .isEqualTo(comment);
    }


    @Test
    @DisplayName("특정 게시물에 달린 모든 답글 조회")
    void findByBoardId() {
        //given
        Member writer = this.getWriter();
        Long boardId = this.writeBoard(writer);
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = Comment.createComment(boardId, writer, "답글1");
        Comment comment2 = Comment.createComment(boardId, writer, "답글2");
        Comment comment3 = Comment.createComment(boardId, writer, "답글3");
        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);
        this.commentMapper.save(comment1);
        this.commentMapper.save(comment2);
        this.commentMapper.save(comment3);

        //when
        List<CommentVO> commentVos = this.commentMapper.findByBoardId(boardId);

        //then
        commentVos.forEach((commentVO) -> {
            assertThat(commentVO.getWriterId()).isEqualTo(writer.getId());
        });

        // 조회한 commentVo 리스트의 size 및 동등성 비교까지 수행됨
        assertThat(commentVos).usingRecursiveComparison()
                .ignoringFields("writerId")
                .isEqualTo(comments);
    }


    private Member getWriter() {
        MemberJoinForm memberJoinForm = new MemberJoinForm();
        memberJoinForm.setName("작성자");
        memberJoinForm.setEmail("test@gmail.com");
        memberJoinForm.setPassword("password1234!");

        Member member = Member.createMember(memberJoinForm.getName(), memberJoinForm.getEmail(), memberJoinForm.getPassword());
        this.memberMapper.save(member);
        return member;
    }

    private Long writeBoard(Member writer) {
        BoardWriteForm boardWriteForm = new BoardWriteForm("게시글 제목", "게시글 내용");
        Board board = Board.createBoard(BoardType.FREE, boardWriteForm.getTitle(), boardWriteForm.getContent(), writer);
        this.boardMapper.save(board);
        return board.getId();
    }

}