package com.mdx.framework.databinding;
import com.mdx.framework.R;
import com.mdx.framework.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class DefaultSelphotoItemDirSelectBindingImpl extends DefaultSelphotoItemDirSelectBinding implements com.mdx.framework.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback5;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public DefaultSelphotoItemDirSelectBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds));
    }
    private DefaultSelphotoItemDirSelectBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.mdx.framework.widget.MImageView) bindings[1]
            , (android.widget.FrameLayout) bindings[0]
            , (android.widget.TextView) bindings[2]
            );
        this.ivDir.setTag(null);
        this.llRoot.setTag(null);
        this.tvDirname.setTag(null);
        setRootTag(root);
        // listeners
        mCallback5 = new com.mdx.framework.generated.callback.OnClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.item == variableId) {
            setItem((com.mdx.framework.widget.selectphotos.utils.PhotosUtil.Folder) variable);
        }
        else if (BR.P == variableId) {
            setP((com.mdx.framework.autofit.commons.AutoParams) variable);
        }
        else if (BR.F == variableId) {
            setF((com.mdx.framework.autofit.AutoFit) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setItem(@Nullable com.mdx.framework.widget.selectphotos.utils.PhotosUtil.Folder Item) {
        this.mItem = Item;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.item);
        super.requestRebind();
    }
    public void setP(@Nullable com.mdx.framework.autofit.commons.AutoParams P) {
        this.mP = P;
    }
    public void setF(@Nullable com.mdx.framework.autofit.AutoFit F) {
        this.mF = F;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.F);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String itemFirstImagePath = null;
        com.mdx.framework.widget.selectphotos.utils.PhotosUtil.Folder item = mItem;
        java.lang.String javaLangStringFileItemFirstImagePath = null;
        java.util.List<com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem> itemImages = null;
        int itemImagesSize = 0;
        java.lang.String itemNameJavaLangStringItemImagesSizeInt1JavaLangString = null;
        java.lang.String itemNameJavaLangStringItemImagesSizeInt1 = null;
        java.lang.String itemName = null;
        java.lang.String itemNameJavaLangString = null;
        int itemImagesSizeInt1 = 0;
        com.mdx.framework.autofit.AutoFit f = mF;

        if ((dirtyFlags & 0x9L) != 0) {



                if (item != null) {
                    // read item.firstImagePath
                    itemFirstImagePath = item.getFirstImagePath();
                    // read item.images
                    itemImages = item.images;
                    // read item.name
                    itemName = item.getName();
                }


                // read ("file:") + (item.firstImagePath)
                javaLangStringFileItemFirstImagePath = ("file:") + (itemFirstImagePath);
                // read (item.name) + ("(")
                itemNameJavaLangString = (itemName) + ("(");
                if (itemImages != null) {
                    // read item.images.size()
                    itemImagesSize = itemImages.size();
                }


                // read (item.images.size()) - (1)
                itemImagesSizeInt1 = (itemImagesSize) - (1);


                // read ((item.name) + ("(")) + ((item.images.size()) - (1))
                itemNameJavaLangStringItemImagesSizeInt1 = (itemNameJavaLangString) + (itemImagesSizeInt1);


                // read (((item.name) + ("(")) + ((item.images.size()) - (1))) + (")")
                itemNameJavaLangStringItemImagesSizeInt1JavaLangString = (itemNameJavaLangStringItemImagesSizeInt1) + (")");
        }
        // batch finished
        if ((dirtyFlags & 0x9L) != 0) {
            // api target 1

            this.ivDir.setObj(javaLangStringFileItemFirstImagePath);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvDirname, itemNameJavaLangStringItemImagesSizeInt1JavaLangString);
        }
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            this.llRoot.setOnClickListener(mCallback5);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // F.setParams("SelectPhoto", @android:layout/default_selphoto_frg_select_photo, "folder", item) != null
        boolean fSetParamsJavaLangStringSelectPhotoAndroidLayoutDefaultSelphotoFrgSelectPhotoJavaLangStringFolderItemJavaLangObjectNull = false;
        // F != null
        boolean fJavaLangObjectNull = false;
        // item
        com.mdx.framework.widget.selectphotos.utils.PhotosUtil.Folder item = mItem;
        // F.setParams("SelectPhoto", @android:layout/default_selphoto_frg_select_photo, "folder", item).close()
        com.mdx.framework.autofit.AutoFit fSetParamsJavaLangStringSelectPhotoAndroidLayoutDefaultSelphotoFrgSelectPhotoJavaLangStringFolderItemClose = null;
        // F.setParams("SelectPhoto", @android:layout/default_selphoto_frg_select_photo, "folder", item)
        com.mdx.framework.autofit.AutoFit fSetParamsJavaLangStringSelectPhotoAndroidLayoutDefaultSelphotoFrgSelectPhotoJavaLangStringFolderItem = null;
        // F
        com.mdx.framework.autofit.AutoFit f = mF;



        fJavaLangObjectNull = (f) != (null);
        if (fJavaLangObjectNull) {






            fSetParamsJavaLangStringSelectPhotoAndroidLayoutDefaultSelphotoFrgSelectPhotoJavaLangStringFolderItem = f.setParams("SelectPhoto", R.layout.default_selphoto_frg_select_photo, "folder", item);

            fSetParamsJavaLangStringSelectPhotoAndroidLayoutDefaultSelphotoFrgSelectPhotoJavaLangStringFolderItemJavaLangObjectNull = (fSetParamsJavaLangStringSelectPhotoAndroidLayoutDefaultSelphotoFrgSelectPhotoJavaLangStringFolderItem) != (null);
            if (fSetParamsJavaLangStringSelectPhotoAndroidLayoutDefaultSelphotoFrgSelectPhotoJavaLangStringFolderItemJavaLangObjectNull) {


                fSetParamsJavaLangStringSelectPhotoAndroidLayoutDefaultSelphotoFrgSelectPhotoJavaLangStringFolderItemClose = fSetParamsJavaLangStringSelectPhotoAndroidLayoutDefaultSelphotoFrgSelectPhotoJavaLangStringFolderItem.close();
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): item
        flag 1 (0x2L): P
        flag 2 (0x3L): F
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}