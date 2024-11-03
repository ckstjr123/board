package hello.qnaboard.vo.pagination;

public class PageRequest implements Pageable {

    private final int page; // 요청한 페이지 번호(1부터 시작)
    private final int maxPageSize; // 페이지당 담을 수 있는 최대 데이터 개수

    protected PageRequest(int page, int maxPageSize) {
        if (page < 1) {
            if (maxPageSize < 1) {
                throw new IllegalArgumentException("Page size must not be less than one");
            }
            this.page = 1;
            this.maxPageSize = maxPageSize;

        } else if (maxPageSize < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        }
         else {
            this.page = page;
            this.maxPageSize = maxPageSize;
        }
    }
    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }


    @Override
    public int getPageNumber() {
        return this.page;
    }

    @Override
    public int getMaxPageSize() {
        return this.maxPageSize;
    }
    @Override
    public long getOffset() {
        // offset은 0부터 시작하므로 page 번호에서 1을 뺀 뒤 계산
        return (long) (this.page - 1) * (long) this.maxPageSize;
    }

}
