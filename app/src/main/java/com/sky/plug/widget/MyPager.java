package com.sky.plug.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 17-7-26.
 */
public class MyPager extends ViewPager {
    /**
     * Constructor
     *
     * @param context
     *            the context
     */
    public MyPager(Context context) {
        super(context);
    }
    /**
     * Constructor
     *
     * @param context
     *            the context
     * @param attrs
     *            the attribute set
     */
    public MyPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int index = getCurrentItem();
        int height = 0;

        View v = ((Fragment) getAdapter().instantiateItem(this, index)).getView();
        if (v != null) {
            v.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height = v.getMeasuredHeight();
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}