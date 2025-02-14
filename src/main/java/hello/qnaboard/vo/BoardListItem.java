package hello.qnaboard.vo;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 게시글 목록용 VO
 */
@AllArgsConstructor
@Getter
//@EqualsAndHashCode
@ToString
public class BoardListItem {

    private final Long id; // 게시글 id

    private final String title; // 게시글 제목
    private final long commentCount; // 게시물 댓글 수

    private final Long writerId; // 글쓴이 id
    private final String writerName; // 글쓴이

    private final LocalDateTime regTime; // 작성일

    private final long view; // 조회 수
    private final long recommendCount; // 추천 수
}