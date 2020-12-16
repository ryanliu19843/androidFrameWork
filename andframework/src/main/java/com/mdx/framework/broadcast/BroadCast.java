package com.mdx.framework.broadcast;

import android.content.Context;
import android.view.ContextThemeWrapper;

import com.mdx.framework.commons.threadpool.PRunable;
import com.mdx.framework.commons.threadpool.ThreadPool;
import com.mdx.framework.utility.Util;

import java.util.ArrayList;
import java.util.List;

public class BroadCast {
    public static BroadList BROADLIST = new BroadList();

    public static final String BROADLIST_IMAGEDOWNLOAD = "com.mdx.download.image";

    public static final String BROADLIST_FILEDOWNLOAD = "com.mdx.download.file";

    public static final String BROADLIST_APIMANAGER = "broadcast_apiManager";

    public static ThreadPool threadPool = new ThreadPool(20);

    public static void PostBroad(final BIntent intent) {
        if (intent.action != null) {
            List<BReceiver> list = BROADLIST.mget(intent.action, intent.id);
            for (BReceiver br : list) {
                onReceive(br, intent);
            }
        }
        if (intent.object != null) {
            List<BReceiver> list = BROADLIST.mget(intent.object);
            for (BReceiver br : list) {
                onReceive(br, intent);
            }
        }

    }

    public static void PostBroadAction(final BIntent intent) {
        PRunable pRunable = new PRunable() {
            @Override
            public void run() {
                if (intent.action != null) {
                    List<BReceiver> list = BROADLIST.mgetaction(intent.action);
                    for (BReceiver br : list) {
                        onReceive(br, intent);
                    }
                }
            }
        };
        threadPool.execute(pRunable);
    }

    public static void addReceiver(Context context, String action, Object id, Object parent,
                                   OnReceiverListener onReceiverListener) {
        CReceiver cre = new CReceiver(action, id, null, context, onReceiverListener);
        cre.parentMark = parent;
        cre.regedit();
    }

    private static void onReceive(final BReceiver br, final BIntent intent) {
        Util.HANDLER.post(new Runnable() {
            @Override
            public void run() {
                try {
                    br.onReceive(br.context, intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void remove(Context context) {
        List<BReceiver> removelist = new ArrayList<BReceiver>();
        for (int i = 0; i < BROADLIST.size(); i++) {
            BReceiver breceiver = BROADLIST.get(i);
            if (breceiver.context == context) {
                removelist.add(breceiver);
            } else if (breceiver.context instanceof ContextThemeWrapper) {
                ContextThemeWrapper cont = (ContextThemeWrapper) breceiver.context;
                if (cont.getBaseContext() == context) {
                    removelist.add(breceiver);
                }
            }
        }
        for (BReceiver br : removelist) {
            br.unRegedit();
        }
    }

    public static void remove(Object id) {
        List<BReceiver> removelist = new ArrayList<BReceiver>();
        for (int i = 0; i < BROADLIST.size(); i++) {
            BReceiver breceiver = BROADLIST.get(i);
            if (breceiver.id != null && breceiver.id.equals(id)) {
                removelist.add(breceiver);
            }
        }
        for (BReceiver br : removelist) {
            br.unRegedit();
        }
    }

    public static void remove(String action, Object id) {
        BROADLIST.remove(action, id);
    }

    public static void removeByParent(Object parent) {
        List<BReceiver> removelist = new ArrayList<BReceiver>();
        for (int i = 0; i < BROADLIST.size(); i++) {
            BReceiver breceiver = BROADLIST.get(i);
            if (breceiver.parentMark != null && breceiver.parentMark.equals(parent)) {
                removelist.add(breceiver);
            }
        }
        for (BReceiver br : removelist) {
            br.unRegedit();
        }
    }

    public static void remove(String action) {
        List<BReceiver> removelist = new ArrayList<BReceiver>();
        for (int i = 0; i < BROADLIST.size(); i++) {
            BReceiver breceiver = BROADLIST.get(i);
            if (breceiver.action != null && breceiver.action.equals(action)) {
                removelist.add(breceiver);
            }
        }
        for (BReceiver br : removelist) {
            br.unRegedit();
        }
    }
}
