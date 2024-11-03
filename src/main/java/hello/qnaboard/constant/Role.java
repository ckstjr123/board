package hello.qnaboard.constant;

public enum Role {
    USER("회원"), ADMIN("관리자");

    private String name;
    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
