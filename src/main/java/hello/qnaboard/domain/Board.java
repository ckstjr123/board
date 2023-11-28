package hello.qnaboard.domain;

import hello.qnaboard.constant.BoardType;

import java.time.LocalDateTime;

/**
 * 게시글(Article)
 */
public class Board {

    private Long id;

    private Long memberId; // 해당 게시글을 작성한 멤버 id

    private String title; // 제목

    private Long view; // 조회수

    private String content; // 게시물 내용

    private BoardType category; // board_category(게시판 유형 구분 컬럼)

    private LocalDateTime regTime;

    private String modifiedBy;

    private LocalDateTime updateTime;
}
