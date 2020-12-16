// Generated by data binding compiler. Do not edit!
package com.mdx.framework.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.mdx.framework.R;
import com.mdx.framework.autofit.AutoFit;
import com.mdx.framework.autofit.commons.AutoParams;
import com.mdx.framework.widget.MImageView;
import com.mdx.framework.widget.selectphotos.utils.PhotosUtil;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class DefaultSelphotoItemDirSelectBinding extends ViewDataBinding {
  @NonNull
  public final MImageView ivDir;

  @NonNull
  public final FrameLayout llRoot;

  @NonNull
  public final TextView tvDirname;

  @Bindable
  protected AutoFit mF;

  @Bindable
  protected AutoParams mP;

  @Bindable
  protected PhotosUtil.Folder mItem;

  protected DefaultSelphotoItemDirSelectBinding(Object _bindingComponent, View _root,
      int _localFieldCount, MImageView ivDir, FrameLayout llRoot, TextView tvDirname) {
    super(_bindingComponent, _root, _localFieldCount);
    this.ivDir = ivDir;
    this.llRoot = llRoot;
    this.tvDirname = tvDirname;
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

  public abstract void setItem(@Nullable PhotosUtil.Folder item);

  @Nullable
  public PhotosUtil.Folder getItem() {
    return mItem;
  }

  @NonNull
  public static DefaultSelphotoItemDirSelectBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.default_selphoto_item_dir_select, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static DefaultSelphotoItemDirSelectBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<DefaultSelphotoItemDirSelectBinding>inflateInternal(inflater, R.layout.default_selphoto_item_dir_select, root, attachToRoot, component);
  }

  @NonNull
  public static DefaultSelphotoItemDirSelectBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.default_selphoto_item_dir_select, null, false, component)
   */
  @NonNull
  @Deprecated
  public static DefaultSelphotoItemDirSelectBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<DefaultSelphotoItemDirSelectBinding>inflateInternal(inflater, R.layout.default_selphoto_item_dir_select, null, false, component);
  }

  public static DefaultSelphotoItemDirSelectBinding bind(@NonNull View view) {
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
  public static DefaultSelphotoItemDirSelectBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (DefaultSelphotoItemDirSelectBinding)bind(component, view, R.layout.default_selphoto_item_dir_select);
  }
}
