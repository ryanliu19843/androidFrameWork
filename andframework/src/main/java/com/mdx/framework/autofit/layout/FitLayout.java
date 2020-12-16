package com.mdx.framework.autofit.layout;

import android.graphics.Point;
import android.view.View;

/**
 * Created by ryan on 2017/10/19.
 */

public interface FitLayout {

    Object getLoadApi();

    Object getSaveApi();

    Object getUpdateApi();

    Point getTouchPoint();

    void setLoadApi(Object loadApi);

    void setSaveApi(Object saveApi);

    void setUpdateApi(Object updateApi);

    void setOnLongClick(View.OnClickListener longClick);
//    public void setOnLongClick(View.OnClickListener longClick);


}
