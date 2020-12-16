// Generated by data binding compiler. Do not edit!
package com.mdx.framework.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.mdx.framework.R;
import com.mdx.framework.autofit.AutoFit;
import com.mdx.framework.autofit.commons.AutoParams;
import com.mdx.framework.widget.selectphotos.utils.PhotosUtil;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class DefaultSelphotoItemImageSelBinding extends ViewDataBinding {
  @NonNull
  public final CheckBox checkbox;

  @NonNull
  public final View maxview;

  @NonNull
  public final RelativeLayout panelContent;

  @Bindable
  protected PhotosUtil.ImageItem mItem;

  @Bindable
  protected AutoFit mF;

  @Bindable
  protected AutoParams mP;

  protected DefaultSelphotoItemImageSelBinding(Object _bindingComponent, View _root,
      int _localFieldCount, CheckBox checkbox, View maxview, RelativeLayout panelContent) {
    super(_bindingComponent, _root, _localFieldCount);
    this.checkbox = checkbox;
    this.maxview = maxview;
    this.panelContent = panelContent;
  }

  public abstract void setItem(@Nullable PhotosUtil.ImageItem item);

  @Nullable
  public PhotosUtil.ImageItem getItem() {
    return mItem;
  }

  public abstract void setF(@Nullable AutoFit F);

  @Nullable
  public AutoFit getF() {
    return mF;
  }

  public abstract void setP(@Nullable AutoParams P);

  @Nullable
  public AutoParams getP() {
    return mP;
  }

  @NonNull
  public static DefaultSelphotoItemImageSelBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.default_selphoto_item_image_sel, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static DefaultSelphotoItemImageSelBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<DefaultSelphotoItemImageSelBinding>inflateInternal(inflater, R.layout.default_selphoto_item_image_sel, root, attachToRoot, component);
  }

  @NonNull
  public static DefaultSelphotoItemImageSelBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.default_selphoto_item_image_sel, null, false, component)
   */
  @NonNull
  @Deprecated
  public static DefaultSelphotoItemImageSelBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<DefaultSelphotoItemImageSelBinding>inflateInternal(inflater, R.layout.default_selphoto_item_image_sel, null, false, component);
  }

  public static DefaultSelphotoItemImageSelBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static DefaultSelphotoItemImageSelBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (DefaultSelphotoItemImageSelBinding)bind(component, view, R.layout.default_selphoto_item_image_sel);
  }
}
