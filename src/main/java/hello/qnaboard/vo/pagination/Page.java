package hello.qnaboard.vo.pagination;

import java.util.List;

public class Page<T> extends Chunk<T> {

    private final long total; // 총 데이터 개수
    private final int maxPaginationSize; // 페이지네이션당 출력할 페이지 번호 최대 개수

    public Page(List<T> content, Pageable pageable, long total, int maxPaginationSize) {
        super(content, pageable);
        this.total = total;
        this.maxPaginationSize = maxPaginationSize;
    }

    //페이지네이션 시작 페이지 번호
    public int getStartPage() {
        return ((this.getPageNumber() - 1) / this.getMaxPaginationSize()) * this.getMaxPaginationSize() + 1;
    }

    //페이지네이션 마지막 페이지 번호
    public int getEndPage() {
        // Math.min: 두 int 값 중 더 작은 값을 반환, 인수의 값이 같으면 동일한 값
        return (this.getTotalPages() == 0) ?
                1 : Math.min(this.getStartPage() + (this.getMaxPaginationSize() - 1), this.getTotalPages());
    }

    //총 페이지 수
    public int getTotalPages() {
        // Math.ceil: 소수점 이하 값이 0 초과 시 숫자 올림. ex) 1.1 → 2
        return (int) Math.ceil((double) this.total / (double) this.getMaxPageSize());
    }

    public long getTotalElements() {
        return this.total;
    }

    @Override
    public boolean hasPrevious() {
        // 가장 첫번째 페이지네이션일 때는 이전 비활성화(현재 페이지네이션 시작 번호가 페이지네이션 최대 사이즈보다 커야 이전 활성화)
        return this.getStartPage() > this.getMaxPaginationSize();
    }
    @Override
    public boolean isFirst() {
        return !this.hasPrevious();
    }

    @Override
    public boolean hasNext() {
        // 맨 마지막 페이지네이션에선 다음 버튼 비활성화
        return ((long) this.getEndPage() * (long) this.getMaxPageSize()) < this.total;
    }
    @Override
    public boolean isLast() {
        return !this.hasNext();
    }

    //페이지네이션 최대 사이즈
    public int getMaxPaginationSize() {
        return this.maxPaginationSize;
    }

}
