package com.sky.plug.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 17-9-8.
 */
public class ResizeableImageView extends ImageView {
    public ResizeableImageView(Context context) {
        super(context);
    }

    public ResizeableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if(d!=null){
            int width = MeasureSpec.getSize(widthMeasureSpec);
            setMeasuredDimension(width, width);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
