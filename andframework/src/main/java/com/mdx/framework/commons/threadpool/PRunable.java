package com.mdx.framework.commons.threadpool;

import com.mdx.framework.commons.CanIntermit;

public abstract class PRunable implements Runnable, CanIntermit {
    
    public PRunable() {
        stoped = false;
    }
    
    public void intermit() {
        onIntermit();
        if (thread != null) {
            thread.intermit();
            stoped = true;
        }
        if (threadpool != null) {
            threadpool.remove(this);
        }
    }
    
    public void onIntermit() {
    }
    
    public CanIntermit addIntermit(CanIntermit can) {
        if (thread != null) {
            thread.addIntermit(can);
        }
        return can;
    }
    
    public void setThread(ThreadPool.PThread thread) {
        this.thread = thread;
    }
    
    public void setThreadpool(ThreadPool threadpool) {
        this.threadpool = threadpool;
    }
    
    private ThreadPool.PThread thread;
    
    private ThreadPool threadpool;
    
    protected boolean stoped;
}
