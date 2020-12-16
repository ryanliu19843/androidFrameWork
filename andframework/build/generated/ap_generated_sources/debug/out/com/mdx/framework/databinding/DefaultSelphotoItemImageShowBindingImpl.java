package com.mdx.framework.databinding;
import com.mdx.framework.R;
import com.mdx.framework.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class DefaultSelphotoItemImageShowBindingImpl extends DefaultSelphotoItemImageShowBinding  {

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
    // values
    // listeners
    // Inverse Binding Event Handlers

    public DefaultSelphotoItemImageShowBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 2, sIncludes, sViewsWithIds));
    }
    private DefaultSelphotoItemImageShowBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.mdx.framework.widget.MImageView) bindings[1]
            , (android.widget.RelativeLayout) bindings[0]
            );
        this.image.setTag(null);
        this.panelContent.setTag(null);
        setRootTag(root);
        // listeners
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
        if (BR.P == variableId) {
            setP((com.mdx.framework.autofit.commons.AutoParams) variable);
        }
        else if (BR.item == variableId) {
            setItem((com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem) variable);
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
    }
    public void setItem(@Nullable com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem Item) {
        this.mItem = Item;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.item);
        super.requestRebind();
    }
    public void setF(@Nullable com.mdx.framework.autofit.AutoFit F) {
        this.mF = F;
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
        java.lang.String javaLangStringFileItemPath = null;
        java.lang.String itemPath = null;
        com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem item = mItem;

        if ((dirtyFlags & 0xaL) != 0) {



                if (item != null) {
                    // read item.path
                    itemPath = item.path;
                }


                // read ("file:") + (item.path)
                javaLangStringFileItemPath = ("file:") + (itemPath);
        }
        // batch finished
        if ((dirtyFlags & 0xaL) != 0) {
            // api target 1

            this.image.setObj(javaLangStringFileItemPath);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): P
        flag 1 (0x2L): item
        flag 2 (0x3L): F
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}