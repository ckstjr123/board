package hello.qnaboard.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 추천 / 비추천
 */
@Getter
@NoArgsConstructor
public class BoardRecommend {
    //복합 키
    private Long boardId; // 추천된 게시물
    private Long memberId; // 해당 게시글을 추천한 멤버

    private int vote; // 추천 or 비추천(1 or -1)
}
