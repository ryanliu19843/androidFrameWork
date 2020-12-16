package com.mdx.framework.databinding;
import com.mdx.framework.R;
import com.mdx.framework.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class DefaultSelphotoFrgSelectPhotoBindingImpl extends DefaultSelphotoFrgSelectPhotoBinding implements com.mdx.framework.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.submit, 4);
        sViewsWithIds.put(R.id.recycleview, 5);
    }
    // views
    @NonNull
    private final com.mdx.framework.autofit.layout.FitLinearLayout mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView2;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback7;
    @Nullable
    private final android.view.View.OnClickListener mCallback6;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public DefaultSelphotoFrgSelectPhotoBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds));
    }
    private DefaultSelphotoFrgSelectPhotoBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.ImageButton) bindings[1]
            , (com.mdx.framework.widget.pagerecycleview.MRecyclerView) bindings[5]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[3]
            );
        this.back.setTag(null);
        this.mboundView0 = (com.mdx.framework.autofit.layout.FitLinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView2 = (android.widget.LinearLayout) bindings[2];
        this.mboundView2.setTag(null);
        this.tvTitle.setTag(null);
        setRootTag(root);
        // listeners
        mCallback7 = new com.mdx.framework.generated.callback.OnClickListener(this, 2);
        mCallback6 = new com.mdx.framework.generated.callback.OnClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
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
        if (BR.P == variableId) {
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

    public void setP(@Nullable com.mdx.framework.autofit.commons.AutoParams P) {
        this.mP = P;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.P);
        super.requestRebind();
    }
    public void setF(@Nullable com.mdx.framework.autofit.AutoFit F) {
        this.mF = F;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
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
        com.mdx.framework.widget.selectphotos.utils.PhotosUtil.Folder folderPFolder = null;
        com.mdx.framework.autofit.AutoFit fSetAdaAndroidIdRecycleviewAndroidLayoutDefaultSelphotoItemImageSelPFolder = null;
        com.mdx.framework.autofit.commons.AutoParams p = mP;
        java.lang.Object pFolder = null;
        java.lang.String folderPFolderName = null;
        com.mdx.framework.autofit.AutoFit f = mF;

        if ((dirtyFlags & 0x7L) != 0) {



                if (p != null) {
                    // read P["folder"]
                    pFolder = p.get("folder");
                }

            if ((dirtyFlags & 0x5L) != 0) {

                    if (pFolder != null) {
                        // read (Folder) P["folder"]
                        folderPFolder = ((com.mdx.framework.widget.selectphotos.utils.PhotosUtil.Folder) (pFolder));
                    }


                    if (folderPFolder != null) {
                        // read (Folder) P["folder"].name
                        folderPFolderName = folderPFolder.getName();
                    }
            }

                if (f != null) {
                    // read F.setAda(@android:id/recycleview, @android:layout/default_selphoto_item_image_sel, P["folder"])
                    fSetAdaAndroidIdRecycleviewAndroidLayoutDefaultSelphotoItemImageSelPFolder = f.setAda(R.id.recycleview, R.layout.default_selphoto_item_image_sel, pFolder);
                }
        }
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.back.setOnClickListener(mCallback6);
            this.mboundView2.setOnClickListener(mCallback7);
        }
        if ((dirtyFlags & 0x7L) != 0) {
            // api target 1

            this.mboundView0.setLoadApi(fSetAdaAndroidIdRecycleviewAndroidLayoutDefaultSelphotoItemImageSelPFolder);
        }
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.tvTitle, folderPFolderName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 2: {
                // localize variables for thread safety
                // F != null
                boolean fJavaLangObjectNull = false;
                // F.pop(v, @android:layout/default_selphoto_pop_dir_select)
                com.mdx.framework.autofit.AutoFit fPopCallbackArg0AndroidLayoutDefaultSelphotoPopDirSelect = null;
                // F
                com.mdx.framework.autofit.AutoFit f = mF;



                fJavaLangObjectNull = (f) != (null);
                if (fJavaLangObjectNull) {




                    fPopCallbackArg0AndroidLayoutDefaultSelphotoPopDirSelect = f.pop(callbackArg_0, R.layout.default_selphoto_pop_dir_select);
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // F != null
                boolean fJavaLangObjectNull = false;
                // F
                com.mdx.framework.autofit.AutoFit f = mF;
                // F.close()
                com.mdx.framework.autofit.AutoFit fClose = null;



                fJavaLangObjectNull = (f) != (null);
                if (fJavaLangObjectNull) {


                    fClose = f.close();
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): P
        flag 1 (0x2L): F
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}