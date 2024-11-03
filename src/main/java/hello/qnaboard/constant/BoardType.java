package hello.qnaboard.constant;

/**
 * 게시판 유형 [자유, 질문 & 답변, 지식 공유].
 * · HOT(인기 게시물): 각 게시판의 게시물 조회수와 추천 수를 기준으로 선정
 */
public enum BoardType {

    FREE("자유"), QUESTIONS("질문 & 답변"), SHARE("지식 공유");

    private String title; // 게시판 명

    BoardType(String title) {
        this.title = title;
    }


    public String getTitle() {
        return this.title;
    }

    /** url에선 소문자를 사용 */
    public String toLowerCase() {
        return this.name().toLowerCase(); // ex) QUESTIONS → questions
    }
}