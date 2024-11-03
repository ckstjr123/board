package hello.qnaboard.domain;

import hello.qnaboard.constant.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

/**
 * 게시글(Article)
 */
@Getter
@NoArgsConstructor
@ToString
public class Board {

    private Long id;

    private Long memberId; // 해당 게시글을 작성한 멤버 id

    private String title; // 제목

    private long view; // 조회수(게시물 등록 시 default 0)

    private String content; // 게시물 내용

    private BoardType boardType; // board_type(게시판 유형 구분 컬럼)

    private long recommendCount; // 게시물 추천 수(반정규화, default 0)
    private long commentCount; // 게시물 댓글 수(반정규화, default 0)

    private LocalDateTime regTime; // 작성 시각
    private LocalDateTime updateTime; // 수정 시각

    /**
     * Board 생성 메서드
     * @param boardType
     * @param title
     * @param content
     * @param memberId
     * @return Board
     */
    public static Board createBoard(BoardType boardType, String title, String content, Long memberId) {
        Board board = new Board();
        board.boardType = boardType;
        board.title = title;
        board.content = content;
        board.memberId = memberId; // 작성자

        // 년-월-일 시:분:초:fractional seconds(timestamp(6)으로 지정했으므로 MySQL DB에는 yyyy-MM-dd HH:mm:ss:SSSSSS 형식으로 저장됨)
        board.regTime = LocalDateTime.now(); // 게시물 등록 시각
        return board;
    }

}
