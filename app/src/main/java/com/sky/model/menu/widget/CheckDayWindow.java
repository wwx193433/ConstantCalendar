package com.sky.model.menu.widget;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.IconFontCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 17-8-6.
 * 按周常规选择
 */
public class CheckDayWindow extends PopupWindow implements android.view.View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context context;
    private static int daynum = 0xf8;

    //周一到周日
    private IconFontCheckBox chb_monday, chb_tuesday, chb_wednesday,
            chb_thursday, chb_friday, chb_saturday, chb_sunday,
            chb_everyday, chb_once;
    private List<IconFontCheckBox> checkBoxes = new ArrayList<>();

    // 取消、完成按钮
    private TextView bc_cancel, bc_confirm;

    //数据回调接口
    private DataInterface dataInterface;

    public CheckDayWindow(Context context, int daynum) {
        super(context);
        this.context = context;
        this.daynum = daynum;
        View view = View.inflate(context, R.layout.bell_cycle, null);
        setContentView(view);

        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);

        //初始化组件
        initView(view);

        //选择事件
        setCheckListener();
        //点击事件
        setClickListener();

        // 设置默认选中
        setCheckedDay();
    }

    //选择事件
    private void setCheckListener() {
        chb_monday.setOnCheckedChangeListener(this);
        chb_tuesday.setOnCheckedChangeListener(this);
        chb_wednesday.setOnCheckedChangeListener(this);
        chb_thursday.setOnCheckedChangeListener(this);
        chb_friday.setOnCheckedChangeListener(this);
        chb_saturday.setOnCheckedChangeListener(this);
        chb_sunday.setOnCheckedChangeListener(this);
        chb_everyday.setOnCheckedChangeListener(this);
        chb_once.setOnCheckedChangeListener(this);
        checkBoxes.add(chb_monday);
        checkBoxes.add(chb_tuesday);
        checkBoxes.add(chb_wednesday);
        checkBoxes.add(chb_thursday);
        checkBoxes.add(chb_friday);
        checkBoxes.add(chb_saturday);
        checkBoxes.add(chb_sunday);
        checkBoxes.add(chb_once);
    }

    //点击事件
    private void setClickListener() {
        bc_cancel.setOnClickListener(this);
        bc_confirm.setOnClickListener(this);
    }

    private void initView(View view) {
        chb_monday = (IconFontCheckBox) view.findViewById(R.id.chb_monday);
        chb_tuesday = (IconFontCheckBox) view.findViewById(R.id.chb_tuesday);
        chb_wednesday = (IconFontCheckBox) view.findViewById(R.id.chb_wednesday);
        chb_thursday = (IconFontCheckBox) view.findViewById(R.id.chb_thursday);
        chb_friday = (IconFontCheckBox) view.findViewById(R.id.chb_friday);
        chb_saturday = (IconFontCheckBox) view.findViewById(R.id.chb_saturday);
        chb_sunday = (IconFontCheckBox) view.findViewById(R.id.chb_sunday);
        chb_everyday = (IconFontCheckBox) view.findViewById(R.id.chb_everyday);
        chb_once = (IconFontCheckBox) view.findViewById(R.id.chb_once);

        bc_cancel = (TextView) view.findViewById(R.id.bc_cancel);
        bc_confirm = (TextView) view.findViewById(R.id.bc_confirm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bc_cancel:
                dismiss();
                break;
            case R.id.bc_confirm:
                dataInterface.onDataCallBack(daynum);
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!buttonView.isPressed()) {
            return;
        }

        switch (buttonView.getId()) {
            case R.id.chb_monday:
                daynum = isChecked ? (daynum | 0x80) & 0xfe : daynum & 0x7f;
                setCheckedDay();
                break;
            case R.id.chb_tuesday:
                daynum = isChecked ? (daynum | 0x40) & 0xfe : daynum & 0xbf;
                setCheckedDay();
                break;
            case R.id.chb_wednesday:
                daynum = isChecked ? (daynum | 0x20) & 0xfe : daynum & 0xdf;
                setCheckedDay();
                break;
            case R.id.chb_thursday:
                daynum = isChecked ? (daynum | 0x10) & 0xfe : daynum & 0xef;
                setCheckedDay();
                break;
            case R.id.chb_friday:
                daynum = isChecked ? (daynum | 0x08) & 0xfe : daynum & 0xf7;
                setCheckedDay();
                break;
            case R.id.chb_saturday:
                daynum = isChecked ? (daynum | 0x04) & 0xfe : daynum & 0xfb;
                setCheckedDay();
                break;
            case R.id.chb_sunday:
                daynum = isChecked ? (daynum | 0x02) & 0xfe : daynum & 0xfd;
                setCheckedDay();
                break;
            case R.id.chb_everyday:
                daynum = isChecked ? 0xfe : 0x01;
                setCheckedDay();
                break;
            case R.id.chb_once:
                daynum = isChecked ? 0x01 : 0xf8;
                setCheckedDay();
                break;
            default:
                break;
        }

    }

    public void setCheckedDay() {
        if (daynum == 0) {
            daynum = 0x01;
        }
        //每天判断
        chb_everyday.setItemChecked(daynum == 0xfe);
        int temp = checkBoxes.size() - 1;
        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).setItemChecked(((daynum >> temp) & 1) == 1);
            temp--;
        }
    }

    /**
     * 定义回调接口
     */
    public interface DataInterface {
        void onDataCallBack(int daynum);
    }

    public void onSelectData(DataInterface dataInterface) {
        this.dataInterface = dataInterface;
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT < 24) {
            super.showAtLocation(anchor, 0, 0, Gravity.END);
        } else {
            super.setFocusable(true);
            //    在某个控件下方弹出
            super.showAsDropDown(anchor);
        }
    }
}
