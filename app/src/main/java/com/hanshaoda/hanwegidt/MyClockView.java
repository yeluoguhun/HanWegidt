package com.hanshaoda.hanwegidt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * author: hanshaoda
 * created on: 2017/10/12 下午5:07
 * description:
 */

public class MyClockView extends View {

    public static final float DEFAULT_BOUNDARY_WIDTH = 7f;//外圆边框宽度
    public static final float LONG_DEGREE_LENGTH = 30f;
    public static final float SHORT_DEGREE_LENGTH = 15f;
    private Paint mPaintCricle;
    private Paint mPaintOutCri;
    private Paint mPaintHour;
    private Paint mPaintDial;
    private Paint mPaintDegree;
    private Paint mPaintNumber;

    private int degreeNumberSize = 40;
    private Paint mPaintMinute;
    private Paint mPaintSecond;
    private Paint mPaintCenter;

    public MyClockView(Context context) {
        this(context, null);
    }

    public MyClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initPaint();
    }

    private void initPaint() {
        mPaintCricle = new Paint();//外圆
        mPaintOutCri = new Paint();//内圆
        mPaintHour = new Paint();//时
        mPaintMinute = new Paint();//分
        mPaintSecond = new Paint();
        mPaintDial = new Paint();//表盘
        mPaintDegree = new Paint();//刻度
        mPaintNumber = new Paint();//数字
        mPaintCenter = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        float r = Math.min(getHeight() / 2, getWidth() / 2) - DEFAULT_BOUNDARY_WIDTH / 2;
        mPaintCricle.setStyle(Paint.Style.STROKE);
        mPaintCricle.setColor(Color.BLACK);
        mPaintCricle.setAntiAlias(true);
        mPaintCricle.setStrokeWidth(DEFAULT_BOUNDARY_WIDTH);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, r, mPaintCricle);

        mPaintOutCri.setStyle(Paint.Style.STROKE);
        mPaintOutCri.setColor(Color.RED);
        mPaintOutCri.setAntiAlias(true);
        mPaintOutCri.setStrokeWidth(DEFAULT_BOUNDARY_WIDTH);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, r + 10, mPaintOutCri);

        float dialR = Math.min(getHeight() / 2, getWidth() / 2) - DEFAULT_BOUNDARY_WIDTH / 2;
        LinearGradient shader = new LinearGradient(400, 400, 500, 500, Color.rgb(255, 255, 255),
                Color.rgb(220, 220, 220), Shader.TileMode.REPEAT);
        mPaintDial.setStyle(Paint.Style.FILL);
        mPaintDial.setAntiAlias(true);
        mPaintDial.setStrokeWidth(DEFAULT_BOUNDARY_WIDTH);
        mPaintDial.setShader(shader);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, dialR, mPaintDial);

        float degreeLength = 0f;
        mPaintDegree.setColor(Color.BLUE);
        mPaintDegree.setAntiAlias(true);
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                mPaintDegree.setStrokeWidth(5);
                degreeLength = LONG_DEGREE_LENGTH;
            } else {
                mPaintDegree.setStrokeWidth(3);
                degreeLength = SHORT_DEGREE_LENGTH;
            }
            canvas.drawLine(getWidth() / 2, Math.abs(getHeight() / 2 - r),
                    getWidth() / 2, Math.abs(getHeight() / 2 - r) + degreeLength, mPaintDegree);
            canvas.rotate(360 / 60, getWidth() / 2, getHeight() / 2);
        }

        canvas.translate(getWidth() / 2, getHeight() / 2);
        mPaintNumber.setTextAlign(Paint.Align.CENTER);
        mPaintNumber.setTextSize(degreeNumberSize);
        mPaintNumber.setFakeBoldText(true);
        for (int i = 0; i < 12; i++) {

        }
//        时针
        mPaintHour.setAntiAlias(true);
        mPaintHour.setStrokeWidth(10);
//        分针
        mPaintMinute.setAntiAlias(true);
        mPaintMinute.setStrokeWidth(7);
//        秒针
        mPaintSecond.setColor(Color.rgb(255, 288, 196));
        mPaintSecond.setAntiAlias(true);
        mPaintSecond.setStrokeWidth(4);


//        表芯
        mPaintCenter.setColor(Color.GREEN);
        canvas.drawCircle(0, 0, 4, mPaintCenter);
        super.onDraw(canvas);


    }
}
