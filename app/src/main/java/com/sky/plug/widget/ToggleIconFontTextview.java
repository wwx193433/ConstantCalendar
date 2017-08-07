package com.sky.plug.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sky.constantcalendar.R;

/**
 * Created by Administrator on 17-7-31.
 */
public class ToggleIconFontTextview extends TextView {


    private String iconOpen, iconClose;
    private String content = "";
    private int start=0, end = 0;
    float iconSize = 0.0f;
    private boolean open;
    private int textColor, openColor;

    public ToggleIconFontTextview(Context context) {
        super(context);
        init(context);
    }

    public ToggleIconFontTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        formatTextview(context, attrs, 0);
    }

    public void setOpen(boolean open) {
        this.open = open;
        setText();
    }

    public boolean isOpen(){
        //获取文字
        String text = this.getText().toString();
        if(text.contains(iconOpen)){
            return true;
        }
        return false;
    }

    private void formatTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取自定义属性的值

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SkyText, defStyleAttr, 0);

        if(iconOpen == null){
            //获取图标
            iconOpen = a.getString(R.styleable.SkyText_iconOpen);
        }
        openColor = a.getInt(R.styleable.SkyText_openColor, 0);
        textColor = this.getCurrentTextColor();

        if(iconClose == null){
            //获取图标
            iconClose = a.getString(R.styleable.SkyText_iconClose);
        }
        open = a.getBoolean(R.styleable.SkyText_open, false);
        iconSize = a.getDimension(R.styleable.SkyText_iconSize, -1);
        if(iconSize == -1.0){
            iconSize = this.getTextSize();
        }

        setText();
        a.recycle();  //注意回收
    }

    private void setText() {

        //获取文字
        String text = this.getText().toString().replace(iconClose,"").replace(iconOpen, "").replace(" ", "");
        if(!text.equals("")){
            text = text+" ";
        }
        if(!open){
            content = text+iconClose;
            this.setTextColor(textColor);
        }else{
            content = text+iconOpen;
            this.setTextColor(openColor);
        }
        start = text.length();
        end = content.length();

        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(new AbsoluteSizeSpan((int) iconSize), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.setText(builder);
    }

    public ToggleIconFontTextview(Context context, AttributeSet attrs, int defStyleAttr) {
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

