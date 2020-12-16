package com.mdx.framework.databinding;
import com.mdx.framework.R;
import com.mdx.framework.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class DefaultSelphotoItemImageSelBindingImpl extends DefaultSelphotoItemImageSelBinding implements com.mdx.framework.generated.callback.OnCheckedChangeListener.Listener, com.mdx.framework.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.maxview, 5);
    }
    // views
    @NonNull
    private final com.mdx.framework.widget.MImageView mboundView1;
    @NonNull
    private final com.mdx.framework.widget.MImageView mboundView2;
    @NonNull
    private final android.widget.Button mboundView4;
    // variables
    @Nullable
    private final android.widget.CompoundButton.OnCheckedChangeListener mCallback2;
    @Nullable
    private final android.view.View.OnClickListener mCallback3;
    @Nullable
    private final android.view.View.OnClickListener mCallback1;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public DefaultSelphotoItemImageSelBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds));
    }
    private DefaultSelphotoItemImageSelBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.CheckBox) bindings[3]
            , (android.view.View) bindings[5]
            , (android.widget.RelativeLayout) bindings[0]
            );
        this.checkbox.setTag(null);
        this.mboundView1 = (com.mdx.framework.widget.MImageView) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView2 = (com.mdx.framework.widget.MImageView) bindings[2];
        this.mboundView2.setTag(null);
        this.mboundView4 = (android.widget.Button) bindings[4];
        this.mboundView4.setTag(null);
        this.panelContent.setTag(null);
        setRootTag(root);
        // listeners
        mCallback2 = new com.mdx.framework.generated.callback.OnCheckedChangeListener(this, 2);
        mCallback3 = new com.mdx.framework.generated.callback.OnClickListener(this, 3);
        mCallback1 = new com.mdx.framework.generated.callback.OnClickListener(this, 1);
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
        java.lang.String javaLangStringFileItemPath = null;
        com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem item = mItem;
        int itemIscamViewGONEViewVISIBLE = 0;
        int itemIscamViewVISIBLEViewGONE = 0;
        boolean itemPathJavaLangObjectNull = false;
        int itemPathJavaLangObjectNullViewGONEViewVISIBLE = 0;
        com.mdx.framework.autofit.AutoFit f = mF;
        java.lang.String itemPath = null;
        boolean itemIscam = false;
        int itemIscamItemPathJavaLangObjectNullViewGONEViewVISIBLEViewGONE = 0;
        boolean itemIschecked = false;

        if ((dirtyFlags & 0x9L) != 0) {



                if (item != null) {
                    // read item.path
                    itemPath = item.path;
                    // read item.iscam
                    itemIscam = item.iscam;
                    // read item.ischecked
                    itemIschecked = item.ischecked;
                }
            if((dirtyFlags & 0x9L) != 0) {
                if(itemIscam) {
                        dirtyFlags |= 0x20L;
                        dirtyFlags |= 0x80L;
                        dirtyFlags |= 0x800L;
                }
                else {
                        dirtyFlags |= 0x10L;
                        dirtyFlags |= 0x40L;
                        dirtyFlags |= 0x400L;
                }
            }


                // read ("file:") + (item.path)
                javaLangStringFileItemPath = ("file:") + (itemPath);
                // read item.iscam ? View.GONE : View.VISIBLE
                itemIscamViewGONEViewVISIBLE = ((itemIscam) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read item.iscam ? View.VISIBLE : View.GONE
                itemIscamViewVISIBLEViewGONE = ((itemIscam) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
        }
        // batch finished

        if ((dirtyFlags & 0x800L) != 0) {

                // read item.path == null
                itemPathJavaLangObjectNull = (itemPath) == (null);
            if((dirtyFlags & 0x800L) != 0) {
                if(itemPathJavaLangObjectNull) {
                        dirtyFlags |= 0x200L;
                }
                else {
                        dirtyFlags |= 0x100L;
                }
            }


                // read item.path == null ? View.GONE : View.VISIBLE
                itemPathJavaLangObjectNullViewGONEViewVISIBLE = ((itemPathJavaLangObjectNull) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }

        if ((dirtyFlags & 0x9L) != 0) {

                // read item.iscam ? item.path == null ? View.GONE : View.VISIBLE : View.GONE
                itemIscamItemPathJavaLangObjectNullViewGONEViewVISIBLEViewGONE = ((itemIscam) ? (itemPathJavaLangObjectNullViewGONEViewVISIBLE) : (android.view.View.GONE));
        }
        // batch finished
        if ((dirtyFlags & 0x9L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkbox, itemIschecked);
            this.checkbox.setVisibility(itemIscamViewGONEViewVISIBLE);
            this.mboundView1.setVisibility(itemIscamViewGONEViewVISIBLE);
            this.mboundView1.setObj(javaLangStringFileItemPath);
            this.mboundView2.setVisibility(itemIscamViewVISIBLEViewGONE);
            this.mboundView2.setObj(javaLangStringFileItemPath);
            this.mboundView4.setVisibility(itemIscamItemPathJavaLangObjectNullViewGONEViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkbox, mCallback2, (androidx.databinding.InverseBindingListener)null);
            this.mboundView4.setOnClickListener(mCallback3);
            this.panelContent.setOnClickListener(mCallback1);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnCheckedChanged(int sourceId , android.widget.CompoundButton callbackArg_0, boolean callbackArg_1) {
        // localize variables for thread safety
        // item
        com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem item = mItem;
        // item != null
        boolean itemJavaLangObjectNull = false;



        itemJavaLangObjectNull = (item) != (null);
        if (itemJavaLangObjectNull) {




            item.setCheck(callbackArg_0, callbackArg_1);
        }
    }
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 3: {
                // localize variables for thread safety
                // F != null
                boolean fJavaLangObjectNull = false;
                // F.send("SelectPhoto", 65)
                com.mdx.framework.autofit.AutoFit fSendJavaLangStringSelectPhotoInt65 = null;
                // F
                com.mdx.framework.autofit.AutoFit f = mF;



                fJavaLangObjectNull = (f) != (null);
                if (fJavaLangObjectNull) {




                    fSendJavaLangStringSelectPhotoInt65 = f.send("SelectPhoto", 65);
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // item.path
                java.lang.String itemPath = null;
                // item.path == null ? F.send("SelectPhoto", 65) : item.showPhoto(F)
                com.mdx.framework.autofit.AutoFit itemPathJavaLangObjectNullFSendJavaLangStringSelectPhotoInt65ItemShowPhotoF = null;
                // item
                com.mdx.framework.widget.selectphotos.utils.PhotosUtil.ImageItem item = mItem;
                // item != null
                boolean itemJavaLangObjectNull = false;
                // F.send("SelectPhoto", 65)
                com.mdx.framework.autofit.AutoFit fSendJavaLangStringSelectPhotoInt65 = null;
                // item.iscam
                boolean itemIscam = false;
                // item.path == null
                boolean itemPathJavaLangObjectNull = false;
                // F != null
                boolean fJavaLangObjectNull = false;
                // item.showPhoto(F)
                com.mdx.framework.autofit.AutoFit itemShowPhotoF = null;
                // F
                com.mdx.framework.autofit.AutoFit f = mF;
                // item.iscam ? item.path == null ? F.send("SelectPhoto", 65) : item.showPhoto(F) : item.showPhoto(F)
                com.mdx.framework.autofit.AutoFit itemIscamItemPathJavaLangObjectNullFSendJavaLangStringSelectPhotoInt65ItemShowPhotoFItemShowPhotoF = null;



                itemJavaLangObjectNull = (item) != (null);
                if (itemJavaLangObjectNull) {


                    itemIscam = item.iscam;
                    if (itemIscam) {




                        itemPath = item.path;


                        itemPathJavaLangObjectNull = (itemPath) == (null);
                        if (itemPathJavaLangObjectNull) {



                            fJavaLangObjectNull = (f) != (null);
                            if (fJavaLangObjectNull) {




                                fSendJavaLangStringSelectPhotoInt65 = f.send("SelectPhoto", 65);

                                itemPathJavaLangObjectNullFSendJavaLangStringSelectPhotoInt65ItemShowPhotoF = fSendJavaLangStringSelectPhotoInt65;

                                itemIscamItemPathJavaLangObjectNullFSendJavaLangStringSelectPhotoInt65ItemShowPhotoFItemShowPhotoF = itemPathJavaLangObjectNullFSendJavaLangStringSelectPhotoInt65ItemShowPhotoF;
                            }
                        }
                        else {





                            itemShowPhotoF = item.showPhoto(f);

                            itemPathJavaLangObjectNullFSendJavaLangStringSelectPhotoInt65ItemShowPhotoF = itemShowPhotoF;

                            itemIscamItemPathJavaLangObjectNullFSendJavaLangStringSelectPhotoInt65ItemShowPhotoFItemShowPhotoF = itemPathJavaLangObjectNullFSendJavaLangStringSelectPhotoInt65ItemShowPhotoF;
                        }
                    }
                    else {





                        itemShowPhotoF = item.showPhoto(f);

                        itemIscamItemPathJavaLangObjectNullFSendJavaLangStringSelectPhotoInt65ItemShowPhotoFItemShowPhotoF = itemShowPhotoF;
                    }
                }
                break;
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
        flag 4 (0x5L): item.iscam ? View.GONE : View.VISIBLE
        flag 5 (0x6L): item.iscam ? View.GONE : View.VISIBLE
        flag 6 (0x7L): item.iscam ? View.VISIBLE : View.GONE
        flag 7 (0x8L): item.iscam ? View.VISIBLE : View.GONE
        flag 8 (0x9L): item.path == null ? View.GONE : View.VISIBLE
        flag 9 (0xaL): item.path == null ? View.GONE : View.VISIBLE
        flag 10 (0xbL): item.iscam ? item.path == null ? View.GONE : View.VISIBLE : View.GONE
        flag 11 (0xcL): item.iscam ? item.path == null ? View.GONE : View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}