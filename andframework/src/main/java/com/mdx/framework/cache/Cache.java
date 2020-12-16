package com.mdx.framework.cache;

import com.mdx.framework.config.ToolsConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.utility.tools.NetStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache<K, N, T, M> {
    private Map<K, List<CacheItem<N, T, M>>> cacheMap = new ConcurrentHashMap<K, List<CacheItem<N, T, M>>>();

    private long maxCacheSize = 48 * 1024 * 1024;

    private long minFreeSize = 1024 * 1024;

    private int maxSize = 0;

    private long now = 0;

    private long size = 0;

    public Cache(){
        init();
    }

    public void init(){
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        maxCacheSize=cacheMemory;
    }

    public void setMaxCacheSize(long cache) {
        this.maxCacheSize = cache;
    }

    /**
     * 将一个需要缓存的数据存入缓存
     *
     * @author ryan
     * @Title: put
     * @Description: TODO
     * @param key
     * @param value
     * @throws
     */
    public synchronized void put(K key, CacheItem<N, T, M> value) {
        List<CacheItem<N, T, M>> l;
        boolean isin = false;
        if (!cacheMap.containsKey(key)) {
            l = new ArrayList<CacheItem<N, T, M>>();
            l.add(value);
            synchronized (cacheMap) {
                cacheMap.put(key, l);
            }
        } else {
            l = cacheMap.get(key);
            synchronized (l) {
                for (CacheItem<N, T, M> i : l) {
                    if (i.compare(value.getQualification(), value.getDetails()) == 0) {
                        isin = true;
                        break;
                    }
                }
                if (!isin) {
                    size -= l.size();
                    for (CacheItem<N, T, M> ci : l) {
                        now -= ci.getMemSize();
                    }
                    value.clearList(l);
                    l.add(value);
                }
            }
        }
        value.key = key;
        // 缓存中是否已经存在
        if (!isin) {
            now += value.getMemSize();
            size += 1;
            if (maxCacheSize < now || Runtime.getRuntime().freeMemory() < minFreeSize) {
                cleanCache();
            }
            if (maxSize != 0) {
                cleanSizeCache();
            }
        }
    }

    public long getsize() {
        long sizeall = 0;
        for (K obj : cacheMap.keySet()) {
            for (CacheItem<N, T, M> ci : cacheMap.get(obj)) {
                sizeall += ci.getMemSize();
            }
        }
        return sizeall;
    }

    /**
     * 根据条件获取某项缓存
     *
     * @author ryan
     * @Title: get
     * @Description: TODO
     * @param key
     * @param qtion
     * @param detail
     * @return
     * @throws
     */
    public CacheItem<N, T, M> get(K key, T qtion, M detail) {
        CacheItem<N, T, M> retn = null;
        if (cacheMap.containsKey(key)) {
            List<CacheItem<N, T, M>> l = cacheMap.get(key);
            synchronized (l) {
                for (CacheItem<N, T, M> i : l) {
                    if (i.compare(qtion, detail) == 0) {
                        retn = i;
                        break;
                    }
                }
            }
        }
        if (retn != null) {
            retn.setLastUse(System.nanoTime());
            if (ToolsConfig.checkStatistics(retn.tag)) {
                MLog.D(MLog.CACHE_LOAD, retn.tag + ":" + key.toString() + " size:" + size);
                NetStatistics.addStatistics(retn.tag, key.toString().length(), retn.getMemSize());
            }
        }
        return retn;
    }

    /**
     * 根据条件获取某项缓存
     *
     * @author ryan
     * @Title: get
     * @Description: TODO
     * @param key
     * @return
     * @throws
     */
    public CacheItem<N, T, M> get(K key) {
        CacheItem<N, T, M> retn = null;
        if (cacheMap.containsKey(key)) {
            List<CacheItem<N, T, M>> l = cacheMap.get(key);
            if (l != null && l.size() > 0) {
                retn = l.get(0);
            }
        }
        if (retn != null) {
            retn.setLastUse(System.nanoTime());
            if (ToolsConfig.checkStatistics(retn.tag)) {
                MLog.D(MLog.CACHE_LOAD, retn.tag + ": [" + "size:" + size + " mem:" + now + "]");
                MLog.D(MLog.CACHE_LOAD, retn.tag + ":" + key.toString());
                NetStatistics.addStatistics(retn.tag, key.toString().length(), retn.getMemSize());
            }
        }
        return retn;
    }

    /**
     * 删除某项缓存
     *
     * @author ryan
     * @Title: remove
     * @Description: TODO
     * @param key
     * @throws
     */
    public void remove(K key) {
        ArrayList<K> list = new ArrayList<K>();
        for (K k : cacheMap.keySet()) {
            if (k.toString().startsWith(key.toString())) {
                list.add(k);
            }
        }

        for (K k : list) {
            destory(k);
            synchronized (this) {
                cacheMap.remove(k);
            }
        }
    }

    /**
     * 根据详细条件删除某项缓存 锁cachemap和list
     *
     * @author ryan
     * @Title: removeCache
     * @Description: TODO
     * @param key
     * @param qtion
     * @param detail
     * @throws
     */
    public void removeCache(Object key, T qtion, M detail) {
        if (cacheMap.containsKey(key)) {
            List<CacheItem<N, T, M>> list = cacheMap.remove(key);
            synchronized (list) {
                for (CacheItem<N, T, M> i : list) {
                    if (i.compare(qtion, detail) == 0) {
                        i.destory();
                        now -= i.getMemSize();
                        size -= 1;
                        list.remove(i);
                        break;
                    }
                }
            }
            if (list.size() == 0) {
                synchronized (cacheMap) {
                    cacheMap.remove(key);
                }
            }
        }
    }

    /**
     * 缓存失效处理
     *
     * @author ryan
     * @Title: destory
     * @Description: TODO
     * @param key
     * @throws
     */
    private void destory(K key) {
        if (cacheMap.containsKey(key)) {
            List<CacheItem<N, T, M>> list = cacheMap.get(key);
            synchronized (list) {
                for (CacheItem<N, T, M> i : list) {
                    i.destory();
                    now -= i.getMemSize();
                    size -= 1;
                }
            }
        }
    }

    /**
     * 清除脏数据
     *
     * @author ryan
     * @Title: cleanCache
     * @Description: TODO
     * @throws
     */
    public void cleanCache() {
        List<CacheItem<N, T, M>> deleteList = new ArrayList<CacheItem<N, T, M>>();
        synchronized (cacheMap) {
            for (K key : cacheMap.keySet()) {
                List<CacheItem<N, T, M>> l = cacheMap.get(key);
                if (l != null) {
                    synchronized (l) {
                        for (CacheItem<N, T, M> i : l) {
                            if (i.isDirty()) {
                                deleteList.add(i);
                            }
                        }
                    }
                }
            }
            for (CacheItem<N, T, M> item : deleteList) {
                remove(item);
            }
        }
        cleanMemoryCache();
    }

    public void cleanSizeCache() {
        while (cacheMap.size() > maxSize) {
            removeLast();
        }
        cleanNoCache();
    }

    public void cleanMemoryCache() {
        while (now > maxCacheSize) {
            removeLast();
        }
        cleanNoCache();
    }

    public void removeLast() {
        CacheItem<N, T, M> item = getMinLast();
        if (item != null) {
            remove(item);
        }
    }

    /**
     * 移除某一个缓存类 锁cachemap,list
     *
     * @author ryan
     * @Title: remove
     * @Description: TODO
     * @param item
     * @throws
     */
    private void remove(CacheItem<N, T, M> item) {
        if (item.key != null) {
            remove(item.key, item.getQualification(), item.getDetails());
        }
    }

    /**
     * 移除某一个缓存 锁list
     *
     * @author ryan
     * @Title: remove
     * @Description: TODO
     * @param key
     * @param qtion
     * @param detail
     * @throws
     */
    private void remove(Object key, T qtion, M detail) {
        if (cacheMap.containsKey(key)) {
            List<CacheItem<N, T, M>> list = cacheMap.remove(key);
            synchronized (list) {
                for (CacheItem<N, T, M> i : list) {
                    if (i.compare(qtion, detail) == 0) {
                        i.destory();
                        now -= i.getMemSize();
                        size -= 1;
                        list.remove(i);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 清除空的缓存组 锁cachemap
     *
     * @author ryan
     * @Title: cleanNoCache
     * @Description: TODO
     * @throws
     */
    public void cleanNoCache() {
        synchronized (cacheMap) {
            List<K> dell = new ArrayList<K>();
            for (K key : cacheMap.keySet()) {
                if (cacheMap.get(key).size() == 0) {
                    dell.add(key);
                }
            }
            for (K key : dell) {
                cacheMap.remove(key);
            }
        }
    }

    /**
     * 获取最旧的数据
     *
     * @author ryan
     * @Title: getMinLast
     * @Description: TODO
     * @return
     * @throws
     */
    public CacheItem<N, T, M> getMinLast() {
        long now = System.nanoTime(), maxlast = 0;
        CacheItem<N, T, M> retn = null;
        synchronized (cacheMap) {
            for (K key : cacheMap.keySet()) {
                List<CacheItem<N, T, M>> l = cacheMap.get(key);
                synchronized (l) {
                    for (CacheItem<N, T, M> i : l) {
                        if ((now - i.getLastUse()) > maxlast) {
                            maxlast = now - i.getLastUse();
                            retn = i;
                        }
                    }
                }
            }
        }
        return retn;
    }

    public long size() {
        return size;
    }

    public void clean() {
        synchronized (cacheMap) {
            for (K key : cacheMap.keySet()) {
                destory(key);
            }
            cacheMap.clear();
            now = 0;
            size = 0;
        }
    }

    public long getMinFreeSize() {
        return minFreeSize;
    }

    public void setMinFreeSize(long minFreeSize) {
        this.minFreeSize = minFreeSize;
    }
}
