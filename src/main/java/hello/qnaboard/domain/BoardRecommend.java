package hello.qnaboard.domain;

/**
 * 게시글 추천 수(복합 키)
 */
public class BoardRecommend {
    private Long boardId; // 추천된 게시물
    private Long memberId; // 해당 게시글을 추천한 멤버
}
