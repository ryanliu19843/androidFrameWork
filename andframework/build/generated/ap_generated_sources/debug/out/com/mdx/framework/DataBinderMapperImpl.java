package com.mdx.framework;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.mdx.framework.databinding.DefaultMutipleSelectPhotoBindingImpl;
import com.mdx.framework.databinding.DefaultSelphotoDiaShowPhotoBindingImpl;
import com.mdx.framework.databinding.DefaultSelphotoFrgSelectPhotoBindingImpl;
import com.mdx.framework.databinding.DefaultSelphotoItemDirSelectBindingImpl;
import com.mdx.framework.databinding.DefaultSelphotoItemImageSelBindingImpl;
import com.mdx.framework.databinding.DefaultSelphotoItemImageShowBindingImpl;
import com.mdx.framework.databinding.DefaultSelphotoItemSelimageShowBindingImpl;
import com.mdx.framework.databinding.DefaultSelphotoPopDirSelectBindingImpl;
import com.mdx.framework.databinding.FrgLoadBindingImpl;
import com.mdx.framework.databinding.PagerNavigatorImagebarBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_DEFAULTMUTIPLESELECTPHOTO = 1;

  private static final int LAYOUT_DEFAULTSELPHOTODIASHOWPHOTO = 2;

  private static final int LAYOUT_DEFAULTSELPHOTOFRGSELECTPHOTO = 3;

  private static final int LAYOUT_DEFAULTSELPHOTOITEMDIRSELECT = 4;

  private static final int LAYOUT_DEFAULTSELPHOTOITEMIMAGESEL = 5;

  private static final int LAYOUT_DEFAULTSELPHOTOITEMIMAGESHOW = 6;

  private static final int LAYOUT_DEFAULTSELPHOTOITEMSELIMAGESHOW = 7;

  private static final int LAYOUT_DEFAULTSELPHOTOPOPDIRSELECT = 8;

  private static final int LAYOUT_FRGLOAD = 9;

  private static final int LAYOUT_PAGERNAVIGATORIMAGEBAR = 10;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(10);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.mdx.framework.R.layout.default_mutiple_select_photo, LAYOUT_DEFAULTMUTIPLESELECTPHOTO);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.mdx.framework.R.layout.default_selphoto_dia_show_photo, LAYOUT_DEFAULTSELPHOTODIASHOWPHOTO);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.mdx.framework.R.layout.default_selphoto_frg_select_photo, LAYOUT_DEFAULTSELPHOTOFRGSELECTPHOTO);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.mdx.framework.R.layout.default_selphoto_item_dir_select, LAYOUT_DEFAULTSELPHOTOITEMDIRSELECT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.mdx.framework.R.layout.default_selphoto_item_image_sel, LAYOUT_DEFAULTSELPHOTOITEMIMAGESEL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.mdx.framework.R.layout.default_selphoto_item_image_show, LAYOUT_DEFAULTSELPHOTOITEMIMAGESHOW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.mdx.framework.R.layout.default_selphoto_item_selimage_show, LAYOUT_DEFAULTSELPHOTOITEMSELIMAGESHOW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.mdx.framework.R.layout.default_selphoto_pop_dir_select, LAYOUT_DEFAULTSELPHOTOPOPDIRSELECT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.mdx.framework.R.layout.frg_load, LAYOUT_FRGLOAD);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.mdx.framework.R.layout.pager_navigator_imagebar, LAYOUT_PAGERNAVIGATORIMAGEBAR);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_DEFAULTMUTIPLESELECTPHOTO: {
          if ("layout/default_mutiple_select_photo_0".equals(tag)) {
            return new DefaultMutipleSelectPhotoBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for default_mutiple_select_photo is invalid. Received: " + tag);
        }
        case  LAYOUT_DEFAULTSELPHOTODIASHOWPHOTO: {
          if ("layout/default_selphoto_dia_show_photo_0".equals(tag)) {
            return new DefaultSelphotoDiaShowPhotoBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for default_selphoto_dia_show_photo is invalid. Received: " + tag);
        }
        case  LAYOUT_DEFAULTSELPHOTOFRGSELECTPHOTO: {
          if ("layout/default_selphoto_frg_select_photo_0".equals(tag)) {
            return new DefaultSelphotoFrgSelectPhotoBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for default_selphoto_frg_select_photo is invalid. Received: " + tag);
        }
        case  LAYOUT_DEFAULTSELPHOTOITEMDIRSELECT: {
          if ("layout/default_selphoto_item_dir_select_0".equals(tag)) {
            return new DefaultSelphotoItemDirSelectBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for default_selphoto_item_dir_select is invalid. Received: " + tag);
        }
        case  LAYOUT_DEFAULTSELPHOTOITEMIMAGESEL: {
          if ("layout/default_selphoto_item_image_sel_0".equals(tag)) {
            return new DefaultSelphotoItemImageSelBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for default_selphoto_item_image_sel is invalid. Received: " + tag);
        }
        case  LAYOUT_DEFAULTSELPHOTOITEMIMAGESHOW: {
          if ("layout/default_selphoto_item_image_show_0".equals(tag)) {
            return new DefaultSelphotoItemImageShowBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for default_selphoto_item_image_show is invalid. Received: " + tag);
        }
        case  LAYOUT_DEFAULTSELPHOTOITEMSELIMAGESHOW: {
          if ("layout/default_selphoto_item_selimage_show_0".equals(tag)) {
            return new DefaultSelphotoItemSelimageShowBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for default_selphoto_item_selimage_show is invalid. Received: " + tag);
        }
        case  LAYOUT_DEFAULTSELPHOTOPOPDIRSELECT: {
          if ("layout/default_selphoto_pop_dir_select_0".equals(tag)) {
            return new DefaultSelphotoPopDirSelectBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for default_selphoto_pop_dir_select is invalid. Received: " + tag);
        }
        case  LAYOUT_FRGLOAD: {
          if ("layout/frg_load_0".equals(tag)) {
            return new FrgLoadBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for frg_load is invalid. Received: " + tag);
        }
        case  LAYOUT_PAGERNAVIGATORIMAGEBAR: {
          if ("layout/pager_navigator_imagebar_0".equals(tag)) {
            return new PagerNavigatorImagebarBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for pager_navigator_imagebar is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(4);

    static {
      sKeys.put(1, "F");
      sKeys.put(2, "P");
      sKeys.put(0, "_all");
      sKeys.put(3, "item");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(10);

    static {
      sKeys.put("layout/default_mutiple_select_photo_0", com.mdx.framework.R.layout.default_mutiple_select_photo);
      sKeys.put("layout/default_selphoto_dia_show_photo_0", com.mdx.framework.R.layout.default_selphoto_dia_show_photo);
      sKeys.put("layout/default_selphoto_frg_select_photo_0", com.mdx.framework.R.layout.default_selphoto_frg_select_photo);
      sKeys.put("layout/default_selphoto_item_dir_select_0", com.mdx.framework.R.layout.default_selphoto_item_dir_select);
      sKeys.put("layout/default_selphoto_item_image_sel_0", com.mdx.framework.R.layout.default_selphoto_item_image_sel);
      sKeys.put("layout/default_selphoto_item_image_show_0", com.mdx.framework.R.layout.default_selphoto_item_image_show);
      sKeys.put("layout/default_selphoto_item_selimage_show_0", com.mdx.framework.R.layout.default_selphoto_item_selimage_show);
      sKeys.put("layout/default_selphoto_pop_dir_select_0", com.mdx.framework.R.layout.default_selphoto_pop_dir_select);
      sKeys.put("layout/frg_load_0", com.mdx.framework.R.layout.frg_load);
      sKeys.put("layout/pager_navigator_imagebar_0", com.mdx.framework.R.layout.pager_navigator_imagebar);
    }
  }
}
