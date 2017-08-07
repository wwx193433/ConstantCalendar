package com.sky.plug.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.sky.constantcalendar.R;

/**
 * Created by Administrator on 17-8-6.
 */
public class IconFontCheckBox extends CheckBox {

    private String text;
    private String iconUncheck, iconChecked;
    private int textColor, checkedColor;
    public IconFontCheckBox(Context context) {
        super(context);
    }

    public IconFontCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
        formatCheckbox(context, attrs, 0);

    }

    private void init(Context context) {
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        setTypeface(iconfont);
    }

    private void formatCheckbox(Context context, AttributeSet attrs, int def) {
        //获取自定义属性的值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SkyText, def, 0);

        if(iconUncheck == null){
            iconUncheck = a.getString(R.styleable.SkyText_iconUncheck);
        }
        if(iconChecked == null){
            iconChecked = a.getString(R.styleable.SkyText_iconChecked);
        }

        checkedColor = a.getInt(R.styleable.SkyText_checkedColor, 0);
        textColor = this.getCurrentTextColor();
        text = this.getText().toString().replace(" ","").replace(iconChecked,"").replace(iconUncheck, "");
        setCheckedStatus(this.isChecked());
    }

    public void setItemChecked(boolean checked) {
        super.setChecked(checked);
        setCheckedStatus(checked);
    }

    public void setCheckedStatus(boolean isCheck){
        if (isCheck) {
            this.setText(iconChecked + " " + text);
            this.setTextColor(checkedColor);
        } else {
            this.setText(iconUncheck + " " + text);
            this.setTextColor(textColor);
        }
    }

    public IconFontCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
