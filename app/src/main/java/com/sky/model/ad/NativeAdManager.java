package com.sky.model.ad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;
import com.sky.constantcalendar.R;
import com.sky.plug.widget.IconFontButton;
import com.sky.plug.widget.IconFontTextview;

import java.util.List;

/**
 * Created by Administrator on 17-10-26.
 */
public class NativeAdManager implements NativeAD.NativeAdListener , View.OnClickListener{

    private static final String TAG = "NativeExpressActivity";
    private ViewGroup container;
    private Context context;

    protected AQuery $;

    private NativeADDataRef adItem;
    private NativeAD nativeAD;

    private RelativeLayout normal_ad_container, complex_ad_container;
    private TextView normal_ad_desc, normal_ad_title, complex_ad_desc;
    private ImageView normal_ad_img, complex_ad_img0, complex_ad_img1, complex_ad_img2;
    private LinearLayout complex_ad_img;
    private IconFontButton complex_ad_close, normal_ad_close;
    private IconFontTextview normal_ad_type, complex_ad_type;

    private boolean flag = false;

    public NativeAdManager(Context context, ViewGroup container){
        this.context = context;
        this.container = container;
        initView();
    }

    private void initView() {
        $ = new AQuery(context);
        View view = LayoutInflater.from(context).inflate(R.layout.ad_native, container, true);
        normal_ad_container = (RelativeLayout) view.findViewById(R.id.normal_ad_container);
        complex_ad_container = (RelativeLayout) view.findViewById(R.id.complex_ad_container);
        normal_ad_title = (TextView) view.findViewById(R.id.normal_ad_title);
        normal_ad_desc = (TextView) view.findViewById(R.id.normal_ad_desc);
        complex_ad_desc = (TextView) view.findViewById(R.id.complex_ad_desc);
        normal_ad_img = (ImageView) view.findViewById(R.id.normal_ad_img);
        complex_ad_img = (LinearLayout) view.findViewById(R.id.complex_ad_img);
        complex_ad_img0 = (ImageView) view.findViewById(R.id.complex_ad_img0);
        complex_ad_img1 = (ImageView) view.findViewById(R.id.complex_ad_img1);
        complex_ad_img2 = (ImageView) view.findViewById(R.id.complex_ad_img2);

        complex_ad_close = (IconFontButton) view.findViewById(R.id.complex_ad_close);
        normal_ad_close = (IconFontButton) view.findViewById(R.id.normal_ad_close);
        normal_ad_type = (IconFontTextview) view.findViewById(R.id.normal_ad_type);
        complex_ad_type = (IconFontTextview) view.findViewById(R.id.complex_ad_type);

        normal_ad_desc.setOnClickListener(this);
        normal_ad_img.setOnClickListener(this);
        complex_ad_desc.setOnClickListener(this);
        complex_ad_img.setOnClickListener(this);
        normal_ad_close.setOnClickListener(this);
        complex_ad_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.complex_ad_close || v.getId()==R.id.normal_ad_close){
            container.setVisibility(View.GONE);
        }else{
            hasAgree();
            if(flag){
                adItem.onClicked(v); // 点击接口
            }
        }
    }

    /**
     * 加载广告数据
     */
    public void loadNativeAD() {
        if (nativeAD == null) {
            this.nativeAD = new NativeAD(context, Constants.APPID, Constants.NativePosID, this);
        }
        int count = 2; // 一次拉取的广告条数：范围1-10
        nativeAD.loadAD(count);
    }

    public void hasAgree() {
        new AlertDialog.Builder(context).setTitle("确认下载此应用么？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        flag = true;
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                        flag = false;
                    }
                }).show();
    }

    public void showAD() {
        if (adItem.getAdPatternType() == AdPatternType.NATIVE_3IMAGE) {
            Log.i(TAG, "show three img ad.");
            normal_ad_container.setVisibility(View.GONE);
            complex_ad_container.setVisibility(View.VISIBLE);
            complex_ad_desc.setText(adItem.getDesc());
            $.id(complex_ad_img0).image(adItem.getImgList().get(0), false, true);
            $.id(complex_ad_img1).image(adItem.getImgList().get(1), false, true);
            $.id(complex_ad_img2).image(adItem.getImgList().get(2), false, true);

        } else if (adItem.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT) {
            Log.i(TAG, "show two img ad.");
            normal_ad_container.setVisibility(View.VISIBLE);
            complex_ad_container.setVisibility(View.GONE);
            normal_ad_title.setText(adItem.getTitle());
            normal_ad_desc.setText(adItem.getDesc());
            $.id(normal_ad_img).image(adItem.getImgUrl(), false, true);
        }
        adItem.onExposured(container); // 需要先调用曝光接口
    }

    @Override
    public void onADLoaded(List<NativeADDataRef> list) {
        if (list.size() > 0) {
            adItem = list.get(0);
            showAD();
            Log.i(TAG, "onADLoaded：原生广告加载成功");
        } else {
            Log.i(TAG, "NOADReturn");
        }
    }

    @Override
    public void onNoAD(AdError error) {
        Log.i(TAG, String.format("onNoAD, error code: %d, error msg: %s", error.getErrorCode(),
                        error.getErrorMsg()));
    }

    /**
     * 设置广告类型
     */
    public void setAdType(){
        if (adItem == null) {
            complex_ad_type.setVisibility(View.GONE);
            normal_ad_type.setVisibility(View.GONE);
            return;
        }
        complex_ad_type.setVisibility(View.VISIBLE);
        normal_ad_type.setVisibility(View.VISIBLE);
        if (!adItem.isAPP()) {
            normal_ad_type.setText(R.string.icon_link);
            complex_ad_type.setText(R.string.icon_link);
            return;
        }
        int icon = R.string.icon_app;

        normal_ad_type.setText(icon);
        complex_ad_type.setText(icon);
    }

    @Override
    public void onADStatusChanged(NativeADDataRef nativeADDataRef) {

    }

    @Override
    public void onADError(NativeADDataRef nativeADDataRef, AdError error) {
        Log.i(TAG, String.format("onADError, error code: %d, error msg: %s", error.getErrorCode(),
                error.getErrorMsg()));
    }
}
