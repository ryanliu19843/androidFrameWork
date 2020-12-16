package com.mdx.framework.server.file.read;

import java.io.File;
import java.util.HashMap;

import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.file.impl.FileRead;
import com.mdx.framework.utility.Util;

public class FileFileLoad implements FileRead {
    
    private String mUrl;
    
    private Object mObj;
    
    private int mImageType;
    
    private HashMap<String, String> mParamsMap = new HashMap<String, String>();
    
    /**
     * 开始加载图片<BR>
     * [功能详细描述]
     * 
     * @param width 控件宽度
     * @param height 控件高度
     * @return
     * @throws MException
     * @see com.mdx.framework.server.image.impl.ImageRead#loadImageFromUrl(int,
     *      int)
     */
    @Override
    public File loadFileFromUrl() throws MException {
        String path = ParamsManager.getString(mUrl, mParamsMap);
        MLog.D(MLog.SYS_RUN,"read:" + path);
        path = path.substring(Util.FILE_START.length());
        return new File(path);
    }
    
    /**
     * 
     * 初始化图片加载<BR>
     * 
     * @param url
     * @param object
     * @param imageType
     * @param reload
     * @return
     * @see com.mdx.framework.server.image.impl.ImageRead#createRead(String,
     *      Object, int, boolean)
     */
    @Override
    public void createRead(String url, Object object, int imageType, boolean useCache) {
        mParamsMap.clear();
        this.mUrl = url;
        this.mObj = object;
        this.mImageType = imageType;
        mParamsMap.put(ParamsManager.PARAM_DATA_OBJ, this.mObj.toString());
        mParamsMap.put("loadType", String.valueOf(imageType));
        mParamsMap.put("reload", String.valueOf(this.mImageType));
    }
    
    /**
     * 中断加载<BR>
     * 
     * @see com.mdx.framework.commons.CanIntermit#intermit()
     */
    @Override
    public void intermit() {
    }
}
