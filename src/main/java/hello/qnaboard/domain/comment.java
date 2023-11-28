package hello.qnaboard.domain;

import java.time.LocalDateTime;

/** 댓글 */
public class comment {

    private Long id;

    private Long boardId; // 해당 댓글이 달린 게시물
    private Long memberId; // 해당 댓글을 단 멤버

    private LocalDateTime regTime;

    private String content; // 댓글 내용

    private String modifiedBy;

    private LocalDateTime updateTime;
}
