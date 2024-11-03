package hello.qnaboard.vo.pagination;

public interface Pageable {
    int getPageNumber();

    int getMaxPageSize();

    long getOffset();
}
