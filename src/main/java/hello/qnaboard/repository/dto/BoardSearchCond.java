package hello.qnaboard.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글 리스트 조회 시 검색 조건
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardSearchCond {

    private String searchTarget; // "title_content"(제목+내용), "title"(제목), "content"(내용), "nick_name"(작성자)

    private String searchKeyword; // 검색어
}
