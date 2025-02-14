package hello.qnaboard.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/** 특정 게시물에 달린 답글 VO */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class CommentVO {
    private final Long id; // 답글 id
    private final Long boardId; // 해당 답글이 달린 게시물 id
    private final Long writerId; // 답글 작성자 id
    private final String writerName; // 답글 작성자 명
    private final String content; // 답글 내용
    private final LocalDateTime regTime; // 답글 등록 시각
}