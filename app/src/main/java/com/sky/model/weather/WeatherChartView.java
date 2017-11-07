package com.sky.model.weather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

/**
 * Created by Administrator on 17-10-16.
 */
public class WeatherChartView extends View {
    private int highPoints[] = new int[0];
    private int lowPoints[] = new int[0];
    private int width, height;
    private float radius, cRadius;
    private float density;
    private int dayIndex = 1;
    private float fontsize;
    private float textspace;
    private int interval;


    public void setHighPoints(int[] highPoints) {
        this.highPoints = highPoints;
    }

    public void setLowPoints(int[] lowPoints) {
        this.lowPoints = lowPoints;
    }

    public WeatherChartView(Context context) {
        super(context);
    }

    public WeatherChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = getResources().getDisplayMetrics().density;
        radius = density * 4;
        cRadius = density * 6;
        fontsize = density * 14;
        textspace = density * 16;
    }

    public static int[] concat(int[] first, int[] second) {
        int[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private int getMaxNum(int[] h, int[] l) {
        int[] points = concat(h, l);
        if (points.length == 0) {
            return 0;
        }
        int max = points[0];
        for (int p : points) {
            if (p > max) {
                max = p;
            }
        }
        return max;
    }

    private int getMinNum(int[] h, int[] l) {
        int[] points = concat(h, l);
        if (points.length == 0) {
            return 0;
        }
        int min = points[0];
        for (int p : points) {
            if (p < min) {
                min = p;
            }
        }
        return min;
    }

    public WeatherChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(highPoints.length==0){
            return;
        }
        width = canvas.getWidth();
        height = canvas.getHeight();

        int max = getMaxNum(highPoints, lowPoints);
        int min = getMinNum(highPoints, lowPoints);
        interval = max - min;

        int k = height/2/interval;//高度和屏幕对应的系数

        int[] mXAxis = new int[highPoints.length];
        int[] high_mYAxis = new int[highPoints.length];
        int[] low_mYAxis = new int[lowPoints.length];
        for (int i = 0; i < high_mYAxis.length; i++) {
            mXAxis[i] = width / (highPoints.length) / 2 + width / (highPoints.length) * i;
            high_mYAxis[i] = (interval - (highPoints[i]-min)) * k + height/4;
            low_mYAxis[i] = (interval - (lowPoints[i]-min)) * k + height/4;
        }
        drawHighChart(canvas, mXAxis, high_mYAxis);
        drawLowChart(canvas, mXAxis, low_mYAxis);
    }

    private void drawHighChart(Canvas canvas, int[] mXAxis, int[] mYAxis) {
        //线条画笔
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);//抗锯齿
        linePaint.setStrokeWidth((float) 5.0);
        linePaint.setColor(Color.parseColor("#F5C826"));
        linePaint.setStyle(Paint.Style.STROKE);////设置画笔为空心

        //点画笔
        Paint pointPaint = new Paint();
        pointPaint.setAntiAlias(true);


        //文字画笔
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(fontsize);
        textPaint.setTextAlign(Paint.Align.CENTER);//文字居中

        for (int i = 0; i < mXAxis.length; i++) {
            //画折线图
            Path path = null;
            if (i < mXAxis.length - 1) {
                path = new Path();
                path.moveTo(mXAxis[i], mYAxis[i]);
                path.lineTo(mXAxis[i + 1], mYAxis[i + 1]);
            }
            //画点
            if (i < dayIndex) {
                pointPaint.setAlpha(0x7f);
                linePaint.setColor(Color.parseColor("#eeeeee"));
                pointPaint.setColor(Color.parseColor("#eeeeee"));
            } else {
                pointPaint.setAlpha(0xff);
                linePaint.setColor(Color.parseColor("#F5C826"));
                pointPaint.setColor(Color.parseColor("#F5C826"));
            }
            canvas.drawCircle(mXAxis[i], mYAxis[i], radius, pointPaint);
            if (null != path) {
                canvas.drawPath(path, linePaint);
            }
            canvas.drawText(highPoints[i]+"°", mXAxis[i], mYAxis[i]-textspace+radius, textPaint);
        }
    }

    private void drawLowChart(Canvas canvas, int[] mXAxis, int[] mYAxis) {
        //线条画笔
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);//抗锯齿
        linePaint.setStrokeWidth((float) 5.0);
        linePaint.setColor(Color.parseColor("#F5C826"));
        linePaint.setStyle(Paint.Style.STROKE);////设置画笔为空心

        //点画笔
        Paint pointPaint = new Paint();
        pointPaint.setAntiAlias(true);


        //文字画笔
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(fontsize);
        textPaint.setTextAlign(Paint.Align.CENTER);//文字居中

        for (int i = 0; i < mXAxis.length; i++) {
            //画折线图
            Path path = null;
            if (i < mXAxis.length - 1) {
                path = new Path();
                path.moveTo(mXAxis[i], mYAxis[i]);
                path.lineTo(mXAxis[i + 1], mYAxis[i + 1]);
            }
            //画点
            if (i < dayIndex) {
                pointPaint.setAlpha(0x7f);
                linePaint.setColor(Color.parseColor("#eeeeee"));
                pointPaint.setColor(Color.parseColor("#eeeeee"));
            } else {
                pointPaint.setAlpha(0xff);
                linePaint.setColor(Color.parseColor("#FD9830"));
                pointPaint.setColor(Color.parseColor("#FD9830"));
            }
            canvas.drawCircle(mXAxis[i], mYAxis[i], radius, pointPaint);
            if (null != path) {
                canvas.drawPath(path, linePaint);
            }
            canvas.drawText(lowPoints[i]+"°", mXAxis[i], mYAxis[i]+textspace+radius, textPaint);
        }

    }
}
