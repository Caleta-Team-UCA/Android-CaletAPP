package com.caletateam.caletapp.app.utils;

import android.content.Context;

import com.caletateam.caletapp.R;
import com.github.mikephil.charting.components.MarkerView;

public class MyMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param
     */
    public MyMarkerView(Context context) {
        super(context, R.layout.layout_dot);
    }
}
