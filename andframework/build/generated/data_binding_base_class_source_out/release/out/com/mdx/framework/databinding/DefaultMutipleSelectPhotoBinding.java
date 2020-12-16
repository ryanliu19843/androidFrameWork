// Generated by data binding compiler. Do not edit!
package com.mdx.framework.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.mdx.framework.R;
import com.mdx.framework.autofit.AutoFit;
import com.mdx.framework.autofit.commons.AutoParams;
import com.mdx.framework.widget.pagerecycleview.MRecyclerView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class DefaultMutipleSelectPhotoBinding extends ViewDataBinding {
  @NonNull
  public final RelativeLayout control;

  @NonNull
  public final MRecyclerView recycleview;

  @NonNull
  public final Button submit;

  @Bindable
  protected AutoFit mF;

  @Bindable
  protected AutoParams mP;

  protected DefaultMutipleSelectPhotoBinding(Object _bindingComponent, View _root,
      int _localFieldCount, RelativeLayout control, MRecyclerView recycleview, Button submit) {
    super(_bindingComponent, _root, _localFieldCount);
    this.control = control;
    this.recycleview = recycleview;
    this.submit = submit;
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
  public static DefaultMutipleSelectPhotoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.default_mutiple_select_photo, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static DefaultMutipleSelectPhotoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<DefaultMutipleSelectPhotoBinding>inflateInternal(inflater, R.layout.default_mutiple_select_photo, root, attachToRoot, component);
  }

  @NonNull
  public static DefaultMutipleSelectPhotoBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.default_mutiple_select_photo, null, false, component)
   */
  @NonNull
  @Deprecated
  public static DefaultMutipleSelectPhotoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<DefaultMutipleSelectPhotoBinding>inflateInternal(inflater, R.layout.default_mutiple_select_photo, null, false, component);
  }

  public static DefaultMutipleSelectPhotoBinding bind(@NonNull View view) {
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
  public static DefaultMutipleSelectPhotoBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (DefaultMutipleSelectPhotoBinding)bind(component, view, R.layout.default_mutiple_select_photo);
  }
}