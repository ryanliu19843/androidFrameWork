package com.mdx.framework.broadcast;

import android.content.Context;

public abstract class BReceiver {
    public String action;
    
    public Object id;
    
    public Object object;
    
    public Object parentMark;
    
    public Context context;
    
    public BReceiver(Context context) {
        this.context = context;
    }
    
    public BReceiver set(String action, Object id, Object obj) {
        this.action = action;
        this.id = id;
        this.object = obj;
        return this;
    }
    
    public BReceiver(String action, Object id, Object obj, Context context) {
        set(action, id, obj);
        this.context = context;
    }
    
    public void regedit() {
        BroadCast.BROADLIST.add(this);
    }
    
    public void unRegedit() {
        context = null;
        BroadCast.BROADLIST.remove(this);
    }
    
    public abstract void onReceive(Context context, BIntent intent);
}
