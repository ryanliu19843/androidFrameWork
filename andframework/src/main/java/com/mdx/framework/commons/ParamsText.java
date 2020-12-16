/*
 * 文件名: ParamsText.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描 述: [该类的简要描述] 创建人: ryan 创建时间:2013-12-2
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2013-12-2 下午1:28:52]
 */
public class ParamsText {
    public static final int TYPE_URL = 0;
    
    public static final int TYPE_STRING = 1;
    
    private ArrayList<Param> strsList = new ArrayList<Param>();
    
    private HashMap<String, String> paramsMap = new HashMap<String, String>();
    
    private int mType = 0;
    
    public boolean setText(String str) {
        boolean bol=false;
        strsList.clear();
        Pattern pattern = Pattern.compile("[\\\\&\\?]{0,1}\\[([A-Za-z0-9=\\-_]*?)\\]");
        Matcher matcher = pattern.matcher(str);
        int start = 0;
        while (matcher.find()) {
            bol=true;
            if (!matcher.group().startsWith("\\")) {
                Param p = new Param();
                p.type = 0;
                p.value = str.substring(start, matcher.start());
                strsList.add(p);
                if (matcher.groupCount() > 0) {
                    Param mp = new Param();
                    if (matcher.group().startsWith("&")) {
                        mp.div = "&";
                    }
                    if (matcher.group().startsWith("?")) {
                        mp.div = "?";
                    }
                    mp.type = 1;
                    mp.value = matcher.group(1);
                    if (mp.value.indexOf("=") >= 0) {
                        String[] ss = mp.value.split("=");
                        mp.key = ss[0];
                        mp.value = ss[1];
                    }
                    strsList.add(mp);
                }
                start = matcher.end();
            }
        }
        Param p = new Param();
        p.type = 0;
        p.value = str.substring(start, str.length());
        strsList.add(p);
        return bol;
    }
    
    public void putParam(String key, String value) {
        paramsMap.put(key, value);
    }
    
    public void clearParam() {
        paramsMap.clear();
    }
    
    public String toString(){
        int toolsize=0;
        String str=toOneString();
        while(setText(str)){
            str=toOneString();
            toolsize++;
            if(toolsize>=4){
                break;
            }
        }
        return str;
    }
    
    private String toOneString() {
        StringBuffer sb = new StringBuffer();
        for (Param p : strsList) {
            if (p.type == 0) {
                sb.append(p.value);
            } else {
                if (paramsMap.containsKey(p.value)) {
                    if (mType == TYPE_URL && p.div != null && (p.div.equals("&") || p.div.equals("?"))) {
                        if (sb.toString().indexOf("?") > 0) {
                            sb.append("&");
                        } else {
                            sb.append("?");
                        }
                    } else if (p.div != null) {
                        sb.append(p.div);
                    }
                    sb.append(p.toString(paramsMap.get(p.value)));
                } else {
                    sb.append("");
                }
            }
        }
        return sb.toString();
    }
    
    public int getType() {
        return mType;
    }
    
    public void setType(int type) {
        this.mType = type;
    }
    
    private class Param {
        public int type;
        
        public String key;
        
        public String div;
        
        public String value;
        
        public String toString(String value) {
            if (key != null) {
                return key + "=" + value;
            } else {
                return value;
            }
        }
        
        public String toString() {
            return value;
        }
    }
}
