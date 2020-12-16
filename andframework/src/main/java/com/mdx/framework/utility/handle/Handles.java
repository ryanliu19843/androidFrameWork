package com.mdx.framework.utility.handle;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Handler;

import com.mdx.framework.autofit.AutoFitUtil;

public class Handles {
    private ArrayList<MHandler> HANDLES = new ArrayList<MHandler>();

    public Bitmap[][] MenuIcon;

    public String[] RadioListImg = {};

    public void add(MHandler handler) {
        HANDLES.add(handler);
    }

    public void remove(MHandler handler) {
        HANDLES.remove(handler);
    }

    public int size() {
        return HANDLES.size();
    }

    public Handler get(int ind) {
        return HANDLES.get(ind);
    }

    public ArrayList<MHandler> get(String id) {
        ArrayList<MHandler> retn = new ArrayList<MHandler>();
        for (int i = 0; i < HANDLES.size(); i++) {
            if (AutoFitUtil.FITNAME.equals(id) || HANDLES.get(i).id.equals(id)) {
                retn.add(HANDLES.get(i));
            }
        }
        return retn;
    }


    public ArrayList<MHandler> getEnd(String id) {
        ArrayList<MHandler> retn = new ArrayList<MHandler>();
        for (int i = 0; i < HANDLES.size(); i++) {
            if (HANDLES.get(i).id.endsWith(id)) {
                retn.add(HANDLES.get(i));
            }
        }
        return retn;
    }

    public void closeOne(String id) {
        for (MHandler handle : this.get(id)) {
            handle.sendEmptyMessage(0);
            break;
        }
    }

    public void close(String id) {
        for (MHandler fhand : get(id)) {
            fhand.sendEmptyMessage(0);
        }
    }

    public void closeAll() {
        for (MHandler fhand : HANDLES) {
            fhand.sendEmptyMessage(0);
        }
    }

    public void closeIds(String ids) {
        ids = "," + ids + ",";
        for (MHandler fhand : HANDLES) {
            if (ids != null && ids.indexOf("," + fhand.getId() + ",") >= 0) {
                fhand.sendEmptyMessage(0);
            }
        }
    }

    public void reloadAll(String ids, int[] typs) {
        String[] ides = ids.split(",");
        for (int i = 0; i < ides.length; i++) {
            if (ides[i] != null && ides[i].length() > 0) {
                for (MHandler hand : get(ides[i])) {
                    hand.reload(typs);
                }
            }
        }
    }

    public void reloadAll(String ids) {
        reloadAll(ids, null);
    }

    public void reloadOne(String id) {
        reloadOne(id, null);
    }

    public void reloadOne(String id, int[] typs) {
        for (MHandler fhand : get(id)) {
            fhand.reload(typs);
        }
    }

//	public void sendAll(String ids,int type,Object obj){
//		String[] ides=ids.split(",");
//		for(int i=0;i<ides.length;i++){
//			if(ides[i]!=null && ides[i].length()>0){
//				for(MHandler hand:get(ides[i])){
//					hand.send(type, obj);
//				}
//			}
//		}
//	}

    /**
     * @param ids
     * @param type
     * @param obj
     */
    public void sentAll(String ids, int type, Object obj) {
        String[] ides = ids.split(",");
        for (int i = 0; i < ides.length; i++) {
            if (ides[i] != null && ides[i].length() > 0) {
                for (MHandler hand : get(ides[i])) {
                    hand.sent(type, obj);
                }
            }
        }
    }

    public void close2one(String id) {
        List<MHandler> list = get(id);
        for (int i = 0; i < list.size() - 1; i++) {
            MHandler mh = list.get(i);
            mh.close();
        }
    }

    public void closeWidthOut(String ids) {
        ids = "," + ids + ",";
        for (MHandler fhand : HANDLES) {
            if (ids != null && ids.indexOf("," + fhand.getId() + ",") < 0) {
                fhand.sendEmptyMessage(0);
            }
        }
    }
}
