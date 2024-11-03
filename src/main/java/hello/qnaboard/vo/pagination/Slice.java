package hello.qnaboard.vo.pagination;

import java.util.List;

public interface Slice<T> {
    int getPageNumber();

    int getMaxPageSize();

    List<T> getContent();

    boolean isFirst();

    boolean isLast();

    boolean hasPrevious();

    boolean hasNext();

    default Pageable getPageable() {
        return PageRequest.of(this.getPageNumber(), this.getMaxPageSize());
    }
}
