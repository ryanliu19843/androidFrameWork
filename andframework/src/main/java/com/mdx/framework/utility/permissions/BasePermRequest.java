package com.mdx.framework.utility.permissions;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by ryan on 2016/8/12.
 */
public class BasePermRequest extends PermissionRequest implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    public View view;
    public CompoundButton compoundButton;
    public AdapterView adapterView;
    public boolean checked = false;
    public int i;
    public long l;
    public View.OnClickListener onClickListener;
    public CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    public AdapterView.OnItemSelectedListener onItemSelectedListener;
    public String[] permissions;
    public ArrayList<Integer> run = new ArrayList<>();


    public BasePermRequest(String[] permissions, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.permissions = permissions;
    }

    public BasePermRequest(String[] permissions, CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
        this.permissions = permissions;
    }

    public BasePermRequest(String[] permissions, AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
        this.permissions = permissions;
    }

    @Override
    public void onGrant(String[] permissions, int[] grantResults) {
        while (run.size() > 0) {
            int type = run.remove(0);
            switch (type) {
                case 1:
                    if (onCheckedChangeListener != null) {
                        onCheckedChangeListener.onCheckedChanged(compoundButton, checked);
                    }
                    break;
                case 2:
                    if (onClickListener != null) {
                        onClickListener.onClick(view);
                    }
                    break;
                case 3:
                    if (onItemSelectedListener != null) {
                        onItemSelectedListener.onItemSelected(adapterView, view, i, l);
                    }
                    break;
                case 4:
                    if (onItemSelectedListener != null) {
                        onItemSelectedListener.onNothingSelected(adapterView);
                    }
                    break;
            }
        }
    }

    @Override
    public void onUngrant(String[] permissions, int[] grantResults) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        run.add(1);
        this.compoundButton = compoundButton;
        this.checked = b;
        PermissionsHelper.requestPermissions(permissions, this);
    }

    @Override
    public void onClick(View view) {
        run.add(2);
        this.view = view;
        PermissionsHelper.requestPermissions(permissions, this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        run.add(3);
        this.adapterView = adapterView;
        this.view = view;
        this.i = i;
        this.l = l;
        PermissionsHelper.requestPermissions(permissions, this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        run.add(4);
        this.adapterView = adapterView;
        PermissionsHelper.requestPermissions(permissions, this);
    }
}
