package com.mdx.framework.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.mdx.framework.R;
import com.mdx.framework.prompt.MDialog;
import com.mdx.framework.utility.commons.DataChoose;
import com.mdx.framework.widget.spinnerwheel.AbstractWheel;
import com.mdx.framework.widget.spinnerwheel.OnWheelChangedListener;
import com.mdx.framework.widget.spinnerwheel.adapters.ArrayWheelAdapter;

public class DataSelectDialog extends MDialog {
    private AbstractWheel firstWheel, secondWheel, theedWheel;
    
    private TextView title;
    
    private Button submit;
    
    private OnSelected onSelected;
    
    private DataChoose mDataChoose;;
    
    private String div = " ";
    
    @SuppressLint("SimpleDateFormat")
    public DataSelectDialog(Context context, DataChoose dataChoose) {
        super(context, R.style.Dialog);
        this.mDataChoose = dataChoose;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        getWindow().setGravity(Gravity.CENTER | Gravity.BOTTOM);
        getWindow().setAttributes(lp);
    }
    
    public DataSelectDialog(Context context, int theme) {
        super(context, theme);
    }
    
    @Override
    protected void create(Bundle savedInstanceState) {
        this.setContentView(R.layout.default_dialog_sel);
        title = (TextView) findViewById(R.id.title);
        submit = (Button) findViewById(R.id.submit);
        firstWheel = (AbstractWheel) findViewById(R.id.first);
        secondWheel = (AbstractWheel) findViewById(R.id.second);
        theedWheel = (AbstractWheel) findViewById(R.id.threed);
        
        {
            select(firstWheel, mDataChoose.getFirst());
            select(secondWheel, mDataChoose.getsecond(0, firstWheel.getViewAdapter().getItem(0)));
            select(theedWheel, mDataChoose.gettheed(0,
                    firstWheel.getViewAdapter().getItem(0),
                    0,
                    secondWheel.getViewAdapter().getItem(0)));
        }
        
        firstWheel.addChangingListener(new OnWheelChangedListener() {
            
            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                select(secondWheel, mDataChoose.getsecond(newValue, firstWheel.getViewAdapter().getItem(newValue)));
                select(theedWheel,
                        mDataChoose.gettheed(firstWheel.getCurrentItem(),
                                firstWheel.getViewAdapter().getItem(firstWheel.getCurrentItem()),
                                0,
                                secondWheel.getViewAdapter().getItem(0)));
                title.setText(getText());
            }
        });
        
        secondWheel.addChangingListener(new OnWheelChangedListener() {
            
            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                select(theedWheel,
                        mDataChoose.gettheed(firstWheel.getCurrentItem(),
                                firstWheel.getViewAdapter().getItem(firstWheel.getCurrentItem()),
                                newValue,
                                secondWheel.getViewAdapter().getItem(newValue)));
                title.setText(getText());
            }
        });
        
        theedWheel.addChangingListener(new OnWheelChangedListener() {
            
            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                title.setText(getText());
            }
        });
        title.setText(getText());
        submit.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (onSelected != null) {
                    onSelected.onSelected(DataSelectDialog.this,
                            firstWheel.getViewAdapter().getItem(firstWheel.getCurrentItem()).toString(),
                            secondWheel.getViewAdapter().getItem(secondWheel.getCurrentItem()).toString(),
                            theedWheel.getViewAdapter().getItem(theedWheel.getCurrentItem()).toString());
                    
                }
                dismiss();
            }
        });
    }
    
    public OnSelected getOnSelected() {
        return onSelected;
    }
    
    public DataSelectDialog setOnSelected(OnSelected onSelected) {
        this.onSelected = onSelected;
        return this;
    }
    
    public String getDiv() {
        return div;
    }
    
    public DataSelectDialog setDiv(String div) {
        this.div = div;
        return this;
    }
    
    private String getText() {
        return firstWheel.getViewAdapter().getItem(firstWheel.getCurrentItem()).toString() + div
                + secondWheel.getViewAdapter().getItem(secondWheel.getCurrentItem()).toString() + div
                + theedWheel.getViewAdapter().getItem(theedWheel.getCurrentItem()).toString();
        
    }
    
    private void select(AbstractWheel wheel, int arry) {
        String[] array = getContext().getResources().getStringArray(arry);
        if (array.length == 0) {
            return;
        }
        ArrayWheelAdapter<String> ampmAdapter = new ArrayWheelAdapter<String>(getContext(), array);
        ampmAdapter.setItemResource(R.layout.item_wheel_text_centered);
        ampmAdapter.setItemTextResource(R.id.text);
        wheel.setCurrentItem(0);
        wheel.setViewAdapter(ampmAdapter);
    }
    
    public interface OnSelected {
        public void onSelected(Dialog dia, String first, String second, String thread);
    }
}
