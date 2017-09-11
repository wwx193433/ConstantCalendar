package com.sky.plug.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 17-7-31.
 */
public class FangzhengFontTextView extends TextView {

    public FangzhengFontTextView(Context context) {
        super(context);
        init(context);
    }

    public FangzhengFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FangzhengFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/fzqkbysjt.ttf");
        setTypeface(iconfont);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

