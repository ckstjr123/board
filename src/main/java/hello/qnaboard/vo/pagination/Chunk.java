package hello.qnaboard.vo.pagination;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Chunk<T> implements Slice<T> {

    private final List<T> content = new ArrayList();
    private final Pageable pageable;

    public Chunk(List<T> content, Pageable pageable) {
        Assert.notNull(content, "Content must not be null");
        Assert.notNull(pageable, "Pageable must not be null");
        this.content.addAll(content);
        this.pageable = pageable;
    }

    @Override
    public int getPageNumber() {
        return this.pageable.getPageNumber();
    }

    @Override
    public int getMaxPageSize() {
        return this.pageable.getMaxPageSize();
    }

    @Override
    public boolean hasPrevious() {
        return this.getPageNumber() > 1;
    }

    @Override
    public boolean isFirst() {
        return !this.hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !this.hasNext();
    }

    @Override
    public List<T> getContent() {
        return Collections.unmodifiableList(this.content);
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

}
