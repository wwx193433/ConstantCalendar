package com.sky.plug.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sky.constantcalendar.R;

/**
 * Created by Administrator on 17-7-31.
 */
public class IconFontTextview extends TextView {

    int num = 0;//0 1 2 3  上右下左

    private String icon;
    private String content = "";
    private int start=0, end = 0;
    private int iconColor, textColor;
    float iconSize = 0.0f;

    public IconFontTextview(Context context) {
        super(context);
        init(context);
    }

    public IconFontTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        formatTextview(context, attrs, 0);
    }

    private void formatTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取自定义属性的值

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SkyText, defStyleAttr, 0);

        if(icon == null){
            //获取图标
            icon = a.getString(R.styleable.SkyText_iconTop);
        }

        if(iconColor == 0){
            iconColor = a.getColor(R.styleable.SkyText_iconColor, 0);
            if(iconColor == 0){
                iconColor = this.getCurrentTextColor();
            }
        }

        if(icon == null){
            num = 1;
            icon = a.getString(R.styleable.SkyText_iconRight);
            if(icon == null){
                num = 2;
                icon = a.getString(R.styleable.SkyText_iconBottom);
                if(icon == null){
                    num = 3;
                    icon = a.getString(R.styleable.SkyText_iconLeft);
                    if(icon == null){
                        num = 4;
                        icon = this.getText().toString();
                    }
                }
            }
        }

        textColor = this.getCurrentTextColor();
        iconSize = a.getDimension(R.styleable.SkyText_iconSize, -1);
        if(iconSize == -1.0){
            iconSize = this.getTextSize();
        }

        //获取文字
        String text = this.getText().toString();

        switch(num){
            case 0:// 上
                content = icon+"\n"+text;
                start = 0;
                end = icon.length();
                break;
            case 1:// 右
                content = text+" "+icon;
                start = text.length()+1;
                end = content.length();
                break;
            case 2:// 下
                content = text+"\n"+icon;
                start = text.length();
                end = content.length();
                break;
            case 3:// 左
                content = icon+" "+text;
                start = 0;
                end = icon.length();
                break;
            case 4:// 左
                content = icon;
                start = 0;
                end = icon.length();
                break;
            default:
                break;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(new AbsoluteSizeSpan((int) iconSize), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(iconColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.setText(builder);
        a.recycle();  //注意回收
    }

    public IconFontTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        setTypeface(iconfont);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

