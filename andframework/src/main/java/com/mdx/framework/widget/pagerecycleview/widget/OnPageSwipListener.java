package com.mdx.framework.widget.pagerecycleview.widget;

import android.content.Context;

import com.mdx.framework.server.api.ApiUpdate;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.widget.pagerecycleview.MFRecyclerView;
import com.mdx.framework.widget.pagerecycleview.MRecyclerView;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.util.DataFormat;

import java.util.UUID;

/**
 * Created by ryan on 2016/6/21.
 */
public class OnPageSwipListener extends OnSwipLoadListener {

    protected ApiUpdate mApiUpdate;       //网络请求类
    protected DataFormat mDataFormat;     //数据解释类
    protected int page = 1;               //当前页码
    private boolean useCache = true /*是否使用缓存*/, oneUseCaches = true /*第一次使用缓存*/;
    private boolean isLoading   /*是否正在加载*/, isReload /*是否是重新加载*/, ispageLoading /*是否在加载下一页*/;
    private String loadingId = UUID.randomUUID().toString()  /*请求id （防止多次请求数据错误）*/;
    private int errortype = 0;   //错误类型
    private boolean updateonly = false;  //是否只是更新数据。（下拉刷新默认是重新加载，可以实行只是更新）

    /**
     * 初始化
     *
     * @param context
     * @param api
     * @param dataFormat
     */
    public OnPageSwipListener(Context context, ApiUpdate api, DataFormat dataFormat) {
        this.context = context;
        setApiUpdate(api);
        setDataFormat(dataFormat);
    }

    /**
     * 设置更新接口类
     *
     * @param api
     */
    public void setApiUpdate(ApiUpdate api) {
        this.mApiUpdate = api;
        this.mApiUpdate.setContext(context);
        this.mApiUpdate.setParent(this);
        this.mApiUpdate.setHavaPage(true);
        errortype = this.mApiUpdate.getErrorType();
        this.mApiUpdate.setErrorType(111);
        useCache = this.mApiUpdate.isSaveAble();
        this.mApiUpdate.setMethod("getMessage");
    }


    /**
     * 设置数据解释类
     *
     * @param dataFormat
     */
    public void setDataFormat(DataFormat dataFormat) {
        this.mDataFormat = dataFormat;
    }


    @Override
    public void setRecyclerView(MRecyclerView recyclerView) {
        super.setRecyclerView(recyclerView);
        this.mDataFormat.mrecyclerView=recyclerView;
    }

    /**
     * 开始加载数据
     *
     * @param page 页码
     */
    public void loadApiFrom(long page) {
        if (isReload) {
            loadingId = UUID.randomUUID().toString();  //设置请求id
        }
        if (this.mApiUpdate == null) {   //判断是否存在请求类
            return;
        }
        if (!havepage) {                  //判断是否有下一页
            oneUseCaches = useCache;
        }
        this.mApiUpdate.setPage(page);     //设置页码
        String[][] params = this.mDataFormat.getPageNext();   //设置自定义分页参数
        if (params != null) {
            this.mApiUpdate.setPageParams(params);
        }
        this.mApiUpdate.setSaveAble(oneUseCaches);    //设置是否允许保存数据
        this.mApiUpdate.setSonId(loadingId);          //设置接口id
        if (this.mApiUpdate.getUpdateOne() == null) {    //判断接口参数
            throw new IllegalAccessError("no updateone exit");
        } else {
            this.mApiUpdate.setPostdelay(400);  //延时加载，防止影响展示动画
            this.mApiUpdate.loadUpdateOne();        //加载数据
        }
    }


    /**
     * 清理状态
     */
    public void clear() {
        mApiUpdate.intermit();
        recyclerView.endPullLoad();
        recyclerView = null;
        onDataLoaded = null;
    }


    //接口处理类，主要处理接口返回值
    public void getMessage(Son son) {
        if (!loadingId.equals(son.getSonId())) {  //判断请求id是否一致
            return;
        }
        if (recyclerView == null) {   //判断是否存在view
            return;
        }
        if (isReload && !updateonly) {    //是否需要清理数据
            recyclerView.clearAdapter();
        }
        isReload = false;
        isLoading = false;
        ispageLoading = false;
        if (son.getError() == 0 || son.getType() % 1000 / 100 == 1 || errortype % 1000 / 100 == 1) {   //判断错误类型
            try {
                if (!updateonly) {     //
                    CardAdapter cardAdapter = (CardAdapter) mDataFormat.getCardAdapter(context, son, page);
                    havepage = mDataFormat.hasNext();
                    if (!havepage) {
                        recyclerView.endPage();
                    } else {
                        recyclerView.showPage();
                    }
                    recyclerView.addAdapter(cardAdapter);
                } else {
                    mDataFormat.updateCardAdapter(context, son, recyclerView.getMAdapter());
                }
            } catch (Exception e) {
                havepage = false;
                recyclerView.endPage();
            }
            if (son.getError() == 0) {
                page++;
            }
        }
        if (!recyclerView.isCanPull()) {
            recyclerView.setCanPull(true);
        }
        /*接口错误处理*/

        if (recyclerView.getMAdapter() == null || recyclerView.getMAdapter().getItemCount() == 0) {
            this.onDataState(3, son.getError(), son.msg);  //全屏显示错误
        } else {
            this.onDataState(0, son.getError(), son.msg);  //页尾显示错误
        }
        if (son.getError() != 0) {
            this.havepage = false;
        }
        /*加载完成监听*/
        if (onDataLoaded != null) {
            onDataLoaded.onDataLoaded(son, page);
            onDataLoaded.onDataLoaded(recyclerView.getMAdapter());
        }
        /*播放结束动画*/
        recyclerView.endPullLoad();
    }


    /**
     * 设置数据加载状态
     *
     * @param state
     * @param error
     * @param msg
     */
    @Override
    public void onDataState(int state, int error, String msg) {
        if (recyclerView.getParent() instanceof MFRecyclerView) {
            MFRecyclerView mfRecyclerView = (MFRecyclerView) recyclerView.getParent();
            mfRecyclerView.setLoadingState(state, error, msg);
        }
    }


    /**
     * 下拉加载监听
     *
     * @param type
     */
    @Override
    public void onSwipLoad(int type) {
        if (!isLoading) {
            this.updateonly = false;
        }
        swipload(type);
    }


    /**
     * 下拉加载
     *
     * @param type
     */

    public void swipload(int type) {
        if (!isLoading) {
            isReload = true;
            isLoading = true;
            havepage = false;
            page = 1;
            havepage = false;
            if (type == 0) {
                if (recyclerView.getMAdapter() == null || recyclerView.getMAdapter().getItemCount() == 0) {
                    this.onDataState(1, 0, "");
                }
            } else {
                this.onDataState(0, 0, "");
            }
            if (mDataFormat != null) {
                mDataFormat.reload();
                loadApiFrom(page);
            }
            if (onDataLoaded != null) {
                onDataLoaded.onReload(page);
            }
        }
    }


    @Override
    public void onUpdateLoad(int type) {
        if (!isLoading) {
            this.updateonly = true;
        }
        swipload(type);
    }

    @Override
    public void onSwipStateChange(int state, float mv, float mt) {

    }

    @Override
    public void onPageLoad() {
        if (havepage && !ispageLoading) {
            ispageLoading = true;
            recyclerView.swipRefreshLoadingView.setState(0, "");
//            recyclerView.canPull = false;
            loadApiFrom(page);
            if (onDataLoaded != null) {
                onDataLoaded.onPageLoad(page);
            }
        }
        if(!havepage){
            recyclerView.endPage();
        }
    }

}
