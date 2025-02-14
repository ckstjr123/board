package hello.qnaboard.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/** 댓글 or 답변 */
@Getter
@NoArgsConstructor
@ToString
public class Comment {

    private Long id;

    private Long boardId; // 해당 답글이 달린 게시물

    private Long memberId; // 해당 답글을 단 멤버 id
    private String writerName; // 답글 작성자 명

    private String content; // 답글 내용

    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    /**
     * Comment 생성 메서드
     * @param boardId
     * @param writer
     * @param content
     * @return Comment
     */
    public static Comment createComment(Long boardId, Member writer, String content) {
        Comment comment = new Comment();
        comment.boardId = boardId;
        comment.memberId = writer.getId();
        comment.writerName = writer.getName();
        comment.content = content;
        comment.regTime = LocalDateTime.now(); // timestamp(6): yyyy-MM-dd HH:mm:ss:SSSSSS
        return comment;
    }

}
