package com.mdx.framework.broadcast;

/**
 * @author ryan
 */
public class BIntent {
    public String action;
    public Object id;
    public Object object;
    public int type;
    public Object data, obj1, obj2;
    public long size, lenth;
    public int num1, num2;

    public BIntent(String action, Object id, Object obj, int type, Object data) {
        this.action = action;
        this.id = id;
        this.object = obj;
        this.type = type;
        this.data = data;
    }


    public <T> T getData() {
        return (T) data;
    }

    public <T> T getObject() {
        return (T) object;
    }

    public <T> T getObj1() {
        return (T) obj1;
    }

    public <T> T getObj2() {
        return (T) obj2;
    }
}