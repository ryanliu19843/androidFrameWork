package com.mdx.framework.databinding;
import com.mdx.framework.R;
import com.mdx.framework.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class DefaultSelphotoItemSelimageShowBindingImpl extends DefaultSelphotoItemSelimageShowBinding implements com.mdx.framework.generated.callback.OnClickListener.Listener {

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
    private final android.view.View.OnClickListener mCallback4;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public DefaultSelphotoItemSelimageShowBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 2, sIncludes, sViewsWithIds));
    }
    private DefaultSelphotoItemSelimageShowBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.mdx.framework.widget.MImageView) bindings[1]
            , (android.widget.RelativeLayout) bindings[0]
            );
        this.image.setTag(null);
        this.panelContent.setTag(null);
        setRootTag(root);
        // listeners
        mCallback4 = new com.mdx.framework.generated.callback.OnClickListener(this, 1);
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
            setItem((com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem) variable);
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

    public void setItem(@Nullable com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem Item) {
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
        com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem itemPhotosUtilCheckedImage = null;
        java.lang.String javaLangStringFileItemPath = null;
        com.mdx.framework.widget.selectphotos.utils.PhotosUtil itemPhotosUtil = null;
        java.lang.String itemPath = null;
        com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem item = mItem;
        boolean itemPhotosUtilCheckedImageItem = false;
        int itemPhotosUtilCheckedImageItemPanelContentAndroidColorBPanelContentAndroidColorA = 0;
        com.mdx.framework.autofit.AutoFit f = mF;

        if ((dirtyFlags & 0x9L) != 0) {



                if (item != null) {
                    // read item.photosUtil
                    itemPhotosUtil = item.photosUtil;
                    // read item.path
                    itemPath = item.path;
                }


                if (itemPhotosUtil != null) {
                    // read item.photosUtil.checkedImage
                    itemPhotosUtilCheckedImage = itemPhotosUtil.checkedImage;
                }
                // read ("file:") + (item.path)
                javaLangStringFileItemPath = ("file:") + (itemPath);


                // read item.photosUtil.checkedImage == item
                itemPhotosUtilCheckedImageItem = (itemPhotosUtilCheckedImage) == (item);
            if((dirtyFlags & 0x9L) != 0) {
                if(itemPhotosUtilCheckedImageItem) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }


                // read item.photosUtil.checkedImage == item ? @android:color/B : @android:color/A
                itemPhotosUtilCheckedImageItemPanelContentAndroidColorBPanelContentAndroidColorA = ((itemPhotosUtilCheckedImageItem) ? (getColorFromResource(panelContent, R.color.B)) : (getColorFromResource(panelContent, R.color.A)));
        }
        // batch finished
        if ((dirtyFlags & 0x9L) != 0) {
            // api target 1

            this.image.setObj(javaLangStringFileItemPath);
            androidx.databinding.adapters.ViewBindingAdapter.setBackground(this.panelContent, androidx.databinding.adapters.Converters.convertColorToDrawable(itemPhotosUtilCheckedImageItemPanelContentAndroidColorBPanelContentAndroidColorA));
        }
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            this.panelContent.setOnClickListener(mCallback4);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        // localize variables for thread safety
        // F != null
        boolean fJavaLangObjectNull = false;
        // item
        com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem item = mItem;
        // F.send("showphoto", 33, "item", item)
        com.mdx.framework.autofit.AutoFit fSendJavaLangStringShowphotoInt33JavaLangStringItemItem = null;
        // F
        com.mdx.framework.autofit.AutoFit f = mF;



        fJavaLangObjectNull = (f) != (null);
        if (fJavaLangObjectNull) {






            fSendJavaLangStringShowphotoInt33JavaLangStringItemItem = f.send("showphoto", 33, "item", item);
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): item
        flag 1 (0x2L): P
        flag 2 (0x3L): F
        flag 3 (0x4L): null
        flag 4 (0x5L): item.photosUtil.checkedImage == item ? @android:color/B : @android:color/A
        flag 5 (0x6L): item.photosUtil.checkedImage == item ? @android:color/B : @android:color/A
    flag mapping end*/
    //end
}