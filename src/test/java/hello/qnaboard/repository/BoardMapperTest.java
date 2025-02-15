package hello.qnaboard.repository;

import hello.qnaboard.constant.BoardType;
import hello.qnaboard.controller.dto.MemberJoinForm;
import hello.qnaboard.domain.Board;
import hello.qnaboard.domain.Member;
import hello.qnaboard.repository.dto.BoardSearchCond;
import hello.qnaboard.repository.dto.BoardUpdateForm;
import hello.qnaboard.vo.BoardListItem;
import hello.qnaboard.vo.BoardVO;
import hello.qnaboard.service.BoardWriteForm;
import hello.qnaboard.vo.pagination.PageRequest;
import hello.qnaboard.vo.pagination.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
public class BoardMapperTest {

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private BoardMapper boardMapper;

    @Test
    @DisplayName("게시글 등록")
    void save() {
        //given
        Member writer = this.getWriter(); // 작성자
        BoardWriteForm boardWriteForm = new BoardWriteForm("게시글 제목", "게시글 내용"); // 작성할 게시물
        Board board = Board.createBoard(BoardType.FREE, boardWriteForm.getTitle(), boardWriteForm.getContent(), writer);

        //when
        this.boardMapper.save(board); // 게시글 등록

        //then
        BoardVO boardVO = this.boardMapper.findById(board.getId()).orElseThrow();

        log.info("savedBoard: {}", board);
        log.info("findBoardResultVO: {}", boardVO);

        // 반환된 BoardVO 검증
        assertThat(boardVO.getWriterId()).isEqualTo(writer.getId());
        assertThat(boardVO).usingRecursiveComparison()
                .ignoringFields("writerId")
                .isEqualTo(board);
    }

    @Test
    @DisplayName("게시글 조회 시마다 조회수 1씩 증가")
    void upView() {
        Member writer = this.getWriter();
        BoardWriteForm boardWriteForm = new BoardWriteForm("제목", "내용");
        Board board = Board.createBoard(BoardType.FREE, boardWriteForm.getTitle(), boardWriteForm.getContent(), writer);
        this.boardMapper.save(board);

        BoardVO savedBoardVO = this.boardMapper.findById(board.getId()).orElseThrow();
        assertThat(savedBoardVO.getView()).isEqualTo(0L); // 초기 조회수는 0

        this.boardMapper.upView(board.getId());
        assertThat(this.boardMapper.findById(board.getId()).orElseThrow().getView()).isEqualTo(1L);
        this.boardMapper.upView(board.getId());
        assertThat(this.boardMapper.findById(board.getId()).orElseThrow().getView()).isEqualTo(2L);
    }

    @Test
    @DisplayName("특정 게시판 게시글 수 카운트")
    void countBoards() {
        Member writer = this.getWriter();

        BoardWriteForm boardWriteForm1 = new BoardWriteForm("글1", "내용1");
        Board board1 = Board.createBoard(BoardType.FREE, boardWriteForm1.getTitle(), boardWriteForm1.getContent(), writer);
        this.boardMapper.save(board1);
        BoardWriteForm boardWriteForm2 = new BoardWriteForm("글2", "내용2");
        Board board2 = Board.createBoard(BoardType.FREE, boardWriteForm2.getTitle(), boardWriteForm2.getContent(), writer);
        this.boardMapper.save(board2);

        // 검색 조건 없음
        assertThat(this.boardMapper.countByBoardType(BoardType.FREE)).isEqualTo(2L);
        //'제목+내용'으로 검색
        assertThat(this.boardMapper.countByBoardTypeAndSearchCond(BoardType.FREE, new BoardSearchCond("title_content", "글")))
                .isEqualTo(2L);
        //'제목'으로 검색
        assertThat(this.boardMapper.countByBoardTypeAndSearchCond(BoardType.FREE, new BoardSearchCond("title", board2.getTitle())))
                .isEqualTo(1L);
        //'내용'으로 검색
        assertThat(this.boardMapper.countByBoardTypeAndSearchCond(BoardType.FREE, new BoardSearchCond("content", board1.getContent())))
                .isEqualTo(1L);
        //'작성자'로 검색
        assertThat(this.boardMapper.countByBoardTypeAndSearchCond(BoardType.FREE, new BoardSearchCond("nick_name", writer.getName())))
                .isEqualTo(2L);
        //검색어와 일치하는 게시글이 없음
        assertThat(this.boardMapper.countByBoardTypeAndSearchCond(BoardType.FREE, new BoardSearchCond("title_content", "X")))
                .isEqualTo(0L);
    }

    @Test
    @DisplayName("해당 게시판 게시글 페이징해서 조회")
    void findBoardListWithPaging() {
        Member writer = this.getWriter();
        BoardWriteForm boardWriteForm = new BoardWriteForm("제목", "내용");
        List<Board> boards = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Board board = Board.createBoard(BoardType.FREE, boardWriteForm.getTitle(), boardWriteForm.getContent(), writer);
            this.boardMapper.save(board);
            boards.add(board);
        }


        Pageable pageable = PageRequest.of(1, 10); // 페이지 번호는 1부터 시작
        List<BoardListItem> boardList = this.boardMapper.findBoardListWithPaging(BoardType.FREE, pageable, new BoardSearchCond());


        boardList.forEach((boardListItem) -> {
            assertThat(boardListItem.getWriterId()).isEqualTo(writer.getId());
        });

        Collections.reverse(boards); // 게시글 목록은 최신순 정렬이기 때문에 역순으로 정렬
        assertThat(boardList).usingRecursiveComparison()
                .ignoringFields("writerId")
                .isEqualTo(boards);
    }

    @Test
    @DisplayName("게시글 수정")
    void edit() {
        //given
        Member writer = this.getWriter();
        BoardWriteForm boardWriteForm = new BoardWriteForm("게시글 제목", "게시글 내용");
        Board board = Board.createBoard(BoardType.FREE, boardWriteForm.getTitle(), boardWriteForm.getContent(), writer);
        this.boardMapper.save(board);
        Long boardId = board.getId();

        //when
        BoardUpdateForm updateParam = new BoardUpdateForm();
        updateParam.setTitle("수정된 제목");
        updateParam.setContent("수정된 내용");
        this.boardMapper.update(boardId, updateParam); // 게시글 수정

        //then
        BoardVO boardVO = this.boardMapper.findById(boardId).orElseThrow();
        log.info("editedBoard: {}", boardVO);

        assertThat(boardVO.getTitle()).isEqualTo(updateParam.getTitle());
        assertThat(boardVO.getContent()).isEqualTo(updateParam.getContent());
        assertThat(boardVO.getUpdateTime()).isEqualTo(updateParam.getUpdateTime());
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
}
