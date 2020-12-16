package com.mdx.framework.databinding;
import com.mdx.framework.R;
import com.mdx.framework.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class DefaultSelphotoPopDirSelectBindingImpl extends DefaultSelphotoPopDirSelectBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.list, 1);
    }
    // views
    @NonNull
    private final com.mdx.framework.autofit.layout.FitLinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public DefaultSelphotoPopDirSelectBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 2, sIncludes, sViewsWithIds));
    }
    private DefaultSelphotoPopDirSelectBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.mdx.framework.widget.pagerecycleview.MRecyclerView) bindings[1]
            );
        this.mboundView0 = (com.mdx.framework.autofit.layout.FitLinearLayout) bindings[0];
        this.mboundView0.setTag(null);
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
        java.lang.Object pDirects = null;
        com.mdx.framework.autofit.commons.AutoParams p = mP;
        com.mdx.framework.autofit.AutoFit fSetAdaAndroidIdListAndroidLayoutDefaultSelphotoItemDirSelectPDirects = null;
        com.mdx.framework.autofit.AutoFit f = mF;

        if ((dirtyFlags & 0x7L) != 0) {



                if (p != null) {
                    // read P["directs"]
                    pDirects = p.get("directs");
                }


                if (f != null) {
                    // read F.setAda(@android:id/list, @android:layout/default_selphoto_item_dir_select, P["directs"])
                    fSetAdaAndroidIdListAndroidLayoutDefaultSelphotoItemDirSelectPDirects = f.setAda(R.id.list, R.layout.default_selphoto_item_dir_select, pDirects);
                }
        }
        // batch finished
        if ((dirtyFlags & 0x7L) != 0) {
            // api target 1

            this.mboundView0.setLoadApi(fSetAdaAndroidIdListAndroidLayoutDefaultSelphotoItemDirSelectPDirects);
        }
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