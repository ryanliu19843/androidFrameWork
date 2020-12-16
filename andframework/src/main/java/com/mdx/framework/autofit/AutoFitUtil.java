package com.mdx.framework.autofit;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.ViewDataBinding;

import android.os.Parcelable;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

//import com.google.protobuf.GeneratedMessage;
import com.mdx.framework.Frame;
import com.mdx.framework.activity.IndexAct;
import com.mdx.framework.activity.LoadingAct;
import com.mdx.framework.activity.NoTitleAct;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.activity.TitleTransStatusAct;
import com.mdx.framework.activity.TitleTransparentAct;
import com.mdx.framework.config.LogConfig;
import com.mdx.framework.log.MLog;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Created by ryan on 2017/10/18.
 */

public class AutoFitUtil {
    public static final int GRID = 0;
    public static final int LINEAR = 0;
    public static final int STAGGERED = 0;
    public static final String FITNAME = "AutoFitContent";


    public static Class<?> getActclass(String actname) {
        if (actname.toUpperCase().equals("NoTitleAct".toUpperCase())) {
            return NoTitleAct.class;
        } else if (actname.toUpperCase().equals("TitleAct".toUpperCase())) {
            return TitleAct.class;
        } else if (actname.toUpperCase().equals("TitleTransparentAct".toUpperCase())) {
            return TitleTransparentAct.class;
        } else if (actname.toUpperCase().equals("TitleTransStatusAct".toUpperCase())) {
            return TitleTransStatusAct.class;
        } else if (actname.toUpperCase().equals("LoadingAct".toUpperCase())) {
            return LoadingAct.class;
        } else if (actname.toUpperCase().equals("IndexAct".toUpperCase())) {
            return IndexAct.class;
        }
        return NoTitleAct.class;
    }

    public static void startActivity(Context context, int flag, Class<?> fragment, Class<?> activity, Integer resid, Object... params) {
        Class<?> useAction = activity;
        Intent i = new Intent(context, useAction);
        i.setFlags(flag);
        i.putExtra("className", fragment.getName());
        if (resid != null && resid != 0) {
            i.putExtra("layout", resid);
        }
        if (params != null) {
            for (int ind = 0; ind < params.length; ind++) {
                String key = params[ind].toString();
                if (params.length > ind + 1) {
                    Object obj = params[(ind + 1)];
                    if ((obj instanceof Boolean)) {
                        i.putExtra(key, (Boolean) obj);
                    } else if ((obj instanceof Integer)) {
                        i.putExtra(key, (Integer) obj);
                    } else if ((obj instanceof Float)) {
                        i.putExtra(key, (Float) obj);
                    } else if ((obj instanceof Double)) {
                        i.putExtra(key, (Double) obj);
                    } else if ((obj instanceof Long)) {
                        i.putExtra(key, (Long) obj);
                    } else if ((obj instanceof String)) {
                        i.putExtra(key, (String) obj);
                    } else if ((obj instanceof Serializable)) {
                        i.putExtra(key, (Serializable) obj);
                    } else if ((obj instanceof Byte[])) {
                        i.putExtra(key, (Byte[]) obj);
                    } else if ((obj instanceof String[])) {
                        i.putExtra(key, (String[]) obj);
                    } else if ((obj instanceof Parcelable)) {
                        i.putExtra(key, (Parcelable) obj);
                    } /*else if ((obj instanceof GeneratedMessage.Builder)) {
                        try {
                            GeneratedMessage msg = (GeneratedMessage) ((GeneratedMessage.Builder<?>) obj).build();
                            i.putExtra(key, msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } */ else {
                        MLog.D(obj.getClass().getName() + " unsuppt class type");
                    }
                }
                ind++;
            }
        }
        context.startActivity(i);
    }


    /**
     * 获取值 转换类型
     *
     * @param clas
     * @param value
     * @return
     */

    public static Object getvalue(Class<?> clas, Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().isAssignableFrom(clas)) {
            return value;
        } else {
            if (clas.isAssignableFrom(String.class)) {
                return value.toString();
            } else if (clas.isAssignableFrom(Double.class) || clas.isAssignableFrom(double.class)) {
                return Double.parseDouble(value.toString());
            } else if (clas.isAssignableFrom(Boolean.class) || clas.isAssignableFrom(boolean.class)) {
                return Boolean.parseBoolean(value.toString());
            } else if (clas.isAssignableFrom(Integer.class) || clas.isAssignableFrom(int.class)) {
                return Integer.parseInt(value.toString());
            } else if (clas.isAssignableFrom(Float.class) || clas.isAssignableFrom(float.class)) {
                return Float.parseFloat(value.toString());
            }
        }
        return value;
    }


    /**
     * 添加获取bind
     *
     * @param methodname
     * @param obj
     * @throws Exception
     */
    public static boolean setBind(ViewDataBinding dataBinding, String methodname, Object obj) {
        String n = methodname;
        String bindname = ("set" + n.substring(0, 1).toUpperCase() + n.substring(1));
        try {
            Class clas = obj.getClass();
            Method setmethod = null;
            try {
                setmethod = dataBinding.getClass().getMethod(bindname, obj.getClass());
            } catch (Exception e) {
                setmethod = null;
            }
            if (setmethod == null) {
                for (Method method : dataBinding.getClass().getMethods()) {
                    if (bindname.equals(method.getName())) {
                        Class paramcls[] = method.getParameterTypes();
                        if (paramcls.length == 1) {
                            Class paramclas = paramcls[0];
                            if (paramclas.isAssignableFrom(clas)) {
                                setmethod = method;
                                break;
                            }
                        }
                    }
                }
            }
            if (setmethod != null) {
                setmethod.invoke(dataBinding, obj);
                return true;
            }
        } catch (Exception e) {
            if (LogConfig.getLoglev() >= 5) {
                MLog.E(e);
            }
        }
        if (LogConfig.getLoglev() >= 5) {
            MLog.E("bind " + bindname + "(" + obj.getClass().getName() + ") error");
        }
        return false;
    }


    public static RecyclerView.LayoutManager getLayoutManager(int lp, int column, int orientation) {
        if (lp == 0) {
            return new GridLayoutManager(Frame.CONTEXT, column, orientation, false);
        } else if (lp == 1) {
            return new LinearLayoutManager(Frame.CONTEXT);
        } else if (lp == 2) {
            return new StaggeredGridLayoutManager(column, orientation);
        } else {
            return new LinearLayoutManager(Frame.CONTEXT);
        }
    }
}
