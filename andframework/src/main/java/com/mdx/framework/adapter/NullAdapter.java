package com.mdx.framework.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class NullAdapter extends CardAdapter {
    
    public NullAdapter(Context context) {
        super(context, getArrayString());
    }
    
    public NullAdapter(Context context, List<Card<?>> list) {
        super(context, list);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return new View(getContext());
    }
    
    public static List<Card<?>> getArrayString() {
        
        ArrayList<Card<?>> array = new ArrayList<Card<?>>();
        array.add(new NullCard());
        return array;
    }
}
