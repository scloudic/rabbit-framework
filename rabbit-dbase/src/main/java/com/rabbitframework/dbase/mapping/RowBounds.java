package com.rabbitframework.dbase.mapping;

public class RowBounds {
    public final static long NO_ROW_OFFSET = 0L;
    public final static long NO_ROW_LIMIT = 25L;
    private long offset;
    private long limit;

    public RowBounds() {
        offset = NO_ROW_OFFSET;
        limit = NO_ROW_LIMIT;
    }

    public RowBounds(long offset) {
        this.offset = offset;
    }

    public RowBounds(long offset, long limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLimit() {
        return limit;
    }

    public long getOffset() {
        return offset;
    }
}
