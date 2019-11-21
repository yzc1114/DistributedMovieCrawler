package com.yzchnb.productidextractor.ProductIdUtils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductId {
    private String id;
    private AtomicInteger remainedfailedTime = new AtomicInteger(10);
    private long createTime = System.currentTimeMillis();

    public long getCreateTime(){
        return createTime;
    }

    public ProductId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getRemainedfailedTime() {
        return remainedfailedTime.get();
    }

    public int decreaseAndGetRemainedFailedTime() {
        return this.remainedfailedTime.decrementAndGet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId = (ProductId) o;
        return id.equals(productId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
