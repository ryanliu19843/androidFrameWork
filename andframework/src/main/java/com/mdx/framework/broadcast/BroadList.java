package com.mdx.framework.broadcast;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BroadList extends Vector<BReceiver> {
    private static final long serialVersionUID = 1L;
    
    public List<BReceiver> mget(String action, Object id) {
        List<BReceiver> retn = new ArrayList<BReceiver>();
        synchronized (this) {
            for (BReceiver br : this) {
                if (action.equals(br.action)) {
                    if (id == null && br.id == null) {
                        retn.add(br);
                    } else if (id != null && br.id != null) {
                        if (br.id.equals(id)) {
                            retn.add(br);
                        }
                    }
                }
            }
        }
        return retn;
        
    }
    
    public void remove(String action, Object id) {
        List<BReceiver> brs = mget(action, id);
        for (BReceiver br : brs) {
            remove(br);
        }
    }

    public List<BReceiver> mgetaction(Object action) {
        List<BReceiver> retn = new ArrayList<>();
        synchronized (this) {
            for (BReceiver br : this) {
                if (action.equals(br.action)) {
                    retn.add(br);
                }
            }
        }
        return retn;
    }
    
    public List<BReceiver> mget(Object obj) {
        List<BReceiver> retn = new ArrayList<BReceiver>();
        synchronized (this) {
            for (BReceiver br : this) {
                if (obj.equals(br.object)) {
                    retn.add(br);
                }
            }
        }
        return retn;
    }
    
    public void mRemove(Context context) {
        List<BReceiver> rl = new ArrayList<BReceiver>();
        synchronized (this) {
            for (BReceiver br : this) {
                if (br.context.equals(context)) {
                    rl.add(br);
                }
            }
            for (BReceiver br : rl) {
                remove(br);
            }
        }
    }
}
