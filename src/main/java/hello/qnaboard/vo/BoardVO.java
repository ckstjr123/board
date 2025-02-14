package hello.qnaboard.vo;

import hello.qnaboard.constant.BoardType;
import lombok.*;

import java.time.LocalDateTime;

/** 게시물 VO */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class BoardVO {

    private final Long id; // 게시글 id
    private final String title; // 제목
    private final Long writerId; // 작성자 id
    private final String writerName; // 작성자 명
    private final long view; // 조회수
    private final String content; // 게시물 내용
    private final BoardType boardType; // 게시판 유형
    private final long recommendCount; // 게시물 추천 수
    private final long commentCount; // 게시물 댓글 수

    private final LocalDateTime regTime; // 작성 날짜
    private final LocalDateTime updateTime; // 수정 날짜
}