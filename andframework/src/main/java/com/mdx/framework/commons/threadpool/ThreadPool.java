package com.mdx.framework.commons.threadpool;

import com.mdx.framework.commons.CanIntermit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadPool {
    private List<PRunable> runing = Collections.synchronizedList(new ArrayList<PRunable>());

    private List<PRunable> watrun = Collections.synchronizedList(new ArrayList<PRunable>());

    private List<Thread> threads = Collections.synchronizedList(new ArrayList<Thread>());

    private int maxThreadSize = 5;

    private OnThreadEmpty onThreadEmpty;

    public boolean doable = true;

    public ThreadPool() {
    }

    public ThreadPool(int maxThreadSize) {
        this.maxThreadSize = maxThreadSize;
    }


    public void setDoable(boolean bool) {
        this.doable = bool;
    }

    public class PThread extends Thread implements CanIntermit {

        private PRunable runable;

        private ArrayList<CanIntermit> canIntermits = new ArrayList<CanIntermit>();

        public PThread(PRunable runable) {
            this.runable = runable;
            if (this.runable != null) {
                this.runable.setThread(this);
            }
        }

        public void addIntermit(CanIntermit can) {
            canIntermits.add(can);
        }

        public void intermit() {
            interrupt();
            for (CanIntermit can : canIntermits) {
                can.intermit();
            }
        }


        public void run() {
            runRunAble(runable);
            while (watrun.size() > 0) {
                try {
                    runable = (PRunable) watrun.remove(0);
                } catch (Exception e) {
                }
                runing.add(runable);
                runRunAble(runable);
            }
            threads.remove(this);
        }

        public void runRunAble(Runnable runable) {
            try {
                if (runable != null && doable) {
                    runable.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                canIntermits.clear();
                runing.remove(runable);
            }
        }
    }

    public void setMaxThreadSize(int size) {
        maxThreadSize = size;
        initThread();
    }

    public synchronized void execute(PRunable run) {
        watrun.add(run);
        run.setThreadpool(this);
        initThread();
    }

    public synchronized void intermitAll() {
        watrun.clear();
        synchronized (runing) {
            for (PRunable run : runing) {
                run.intermit();
            }
        }
        initThread();
    }

    public synchronized void submit(PRunable run) {
        execute(run);
    }

    private synchronized void initThread() {
        while (watrun.size() > 0 && threads.size() < maxThreadSize) {
            try {
                PRunable pr = (PRunable) watrun.remove(0);
                runing.add(pr);
                PThread pt = new PThread(pr);
                threads.add(pt);
                pt.start();
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        if (watrun.size() == 0 && runing.size() == 0 && onThreadEmpty != null) {
            onThreadEmpty.onThreadEmpty();
        }
    }

    public void remove(PRunable run) {
        if (watrun.contains(run)) {
            watrun.remove(run);
        }
    }

    public void clear() {
        watrun.clear();
    }

    public List<PRunable> getRuning() {
        return runing;
    }

    public List<PRunable> getWatrun() {
        return watrun;
    }

    public int size() {
        return watrun.size() + runing.size();
    }

    public int runingSize() {
        return runing.size();
    }

    public void setOnThreadEmpty(OnThreadEmpty onThreadEmpty) {
        this.onThreadEmpty = onThreadEmpty;
    }

    public static interface OnThreadEmpty {
        public abstract void onThreadEmpty();
    }
}
