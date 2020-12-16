package com.mdx.framework.cache;

import java.util.List;

public abstract class CacheItem<T, N, M> {
    public String tag = "cache";
    public Object data,obj;
    public Object key;
    private T mItem;
    
    private N mQualification;
    
    private M mDetails;
    
    private long memSize = 1000;
    
    private long lastUse = 0;
    
    public CacheItem(T item) {
        this.mItem = item;
    }
    
    public CacheItem(T item, N qualification) {
        this.mItem = item;
        this.mQualification = qualification;
    }
    
    public CacheItem(T item, N qualification,M details) {
        this.mItem = item;
        this.mQualification = qualification;
        this.mDetails=details;
    }
    
    public T getItem() {
        return mItem;
    }
    
    public void setItem(T item) {
        this.mItem = item;
    }
    
    public void setDetails(M details) {
        this.mDetails = details;
    }
    
    public M getDetails() {
        return mDetails;
    }
    
    public int compare(N qualification,M details){
        if(this.mDetails==null || this.mDetails.equals(details)){
            if(this.mQualification==null){
                return 0;
            }else if(this.mQualification.equals(qualification)){
                return 0;
            }else{
                return 1;
            }
        }else{
            return 1;
        }
    }
    
    public N getQualification() {
        return mQualification;
    }
    
    public void setQualification(N qualification) {
        this.mQualification = qualification;
    }
    
    public void setMemSize(long memSize) {
        this.memSize = memSize;
    }
    
    public long getLastUse() {
        return lastUse;
    }
    
    public void setLastUse(long lastUse) {
        this.lastUse = lastUse;
    }
    
    public abstract boolean isDirty();
    
    public abstract void destory();
    
    protected abstract long calcMemSize();
    
    public void clearList(List<?> l){
        
    }
    
    public long getMemSize() {
        return memSize;
    }
    
    public void init() {
        this.memSize = calcMemSize();
        if(this.memSize==0){
            destory();
        }
        this.lastUse = System.nanoTime();
    }
    
}
