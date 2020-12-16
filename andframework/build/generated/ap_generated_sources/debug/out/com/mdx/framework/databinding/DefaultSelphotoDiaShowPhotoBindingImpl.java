package com.mdx.framework.databinding;
import com.mdx.framework.R;
import com.mdx.framework.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class DefaultSelphotoDiaShowPhotoBindingImpl extends DefaultSelphotoDiaShowPhotoBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.actionbar, 1);
        sViewsWithIds.put(R.id.back, 2);
        sViewsWithIds.put(R.id.submit, 3);
        sViewsWithIds.put(R.id.pager, 4);
        sViewsWithIds.put(R.id.recycleview, 5);
        sViewsWithIds.put(R.id.indicator, 6);
        sViewsWithIds.put(R.id.navbar, 7);
        sViewsWithIds.put(R.id.checkbox, 8);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public DefaultSelphotoDiaShowPhotoBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 9, sIncludes, sViewsWithIds));
    }
    private DefaultSelphotoDiaShowPhotoBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.RelativeLayout) bindings[1]
            , (android.widget.ImageButton) bindings[2]
            , (android.widget.CheckBox) bindings[8]
            , (com.mdx.framework.autofit.layout.FitLinearLayout) bindings[0]
            , (com.mdx.framework.widget.viewpagerindicator.UnderlinePageIndicator) bindings[6]
            , (android.widget.LinearLayout) bindings[7]
            , (com.mdx.framework.widget.MViewPager) bindings[4]
            , (androidx.recyclerview.widget.RecyclerView) bindings[5]
            , (android.widget.TextView) bindings[3]
            );
        this.contextview.setTag(null);
        setRootTag(root);
        // listeners
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
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): P
        flag 1 (0x2L): F
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}