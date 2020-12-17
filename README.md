"# androidFrameWork" 
## 功能介绍
整合常用控件，下拉刷新列表，轮播图，图片选择器，图片加载，api加载等
有效减少代码开发量。
- MimageView支持(支持加载本地，网络图片，支持gif，支持圆形图片，圆形变，模糊，变色，提取颜色）
- 支持databinding，使用AuotFit类，支持弹出pop，dialog，自动填充列表功能。

#### MImage test
![图片](https://github.com/ryanliu19843/androidFrameWork/blob/main/imgtest.jpg)

利用autofit类减少数据和控件的绑定下面是一个监控设备的list的一项  名称为item_device
```Java 
<?xml version="1.0" encoding="UTF-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="F"
            type="com.mdx.framework.autofit.AutoFit" />

        <variable
            name="item"
            type="org.speedbird.dataset.DataDevice.Device" />

        <variable
            name="P"
            type="com.mdx.framework.autofit.commons.AutoParams" />

        <import type="android.view.View" />
    </data>

    <com.mdx.framework.autofit.layout.FitRelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#fff"
        app:loadApi="">

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerVertical="true"
            android:layout_marginLeft="10dp"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@{item.name}"
                android:textColor="@{item.state==1?@color/FA:@color/FB}"
                android:textSize="16sp" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@{item.username}"
                android:textColor="@color/FB"
                android:textSize="14sp" />
        </LinearLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="0.5dp"
            android:layout_marginTop="71.5dp"
            android:background="@color/F" />
    </com.mdx.framework.autofit.layout.FitRelativeLayout>
</layout>
```
利用autofit进行绑定数据，不需再要写adapter，viewhold
```Java 
        Fit.listSet(
                R.id.jklist,   //MFRecyclerView的实例或id
                ApisFactory.getApiDevices().set(PhoneUtil.username(), PhoneUtil.token()),   //绑定api
                R.layout.item_device);  //项目的layout
```

- 如果项目中有任何问题请邮箱联系我 youyexianhe@126.com
