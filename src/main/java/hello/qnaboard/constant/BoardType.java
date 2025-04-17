package hello.qnaboard.constant;

/**
 * 게시판 유형 [자유, 질문 & 답변, 지식 공유]
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