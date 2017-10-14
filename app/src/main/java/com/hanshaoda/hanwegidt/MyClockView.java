package com.hanshaoda.hanwegidt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * author: hanshaoda
 * created on: 2017/10/12 下午5:07
 * description:
 */

public class MyClockView extends View {

    public static final float DEFAULT_BOUNDARY_WIDTH = 7f;//外圆边框宽度
    public static final float LONG_DEGREE_LENGTH = 30f;//长刻度线
    public static final float SHORT_DEGREE_LENGTH = 15f;//短刻度线
    public static final float DEFAULT_MORE_LENGTH = 35f;//针长处原点的长度
    private Paint mPaintCricle;
    private Paint mPaintOutCri;
    private Paint mPaintHour;
    private Paint mPaintDial;
    private Paint mPaintDegree;
    private Paint mPaintNumber;
    //    刻度盘字体大小
    private int degreeNumberSize = 40;
    private Paint mPaintMinute;
    private Paint mPaintSecond;
    private Paint mPaintCenter;
    //    秒
    private int second;
    //    分
    private int minute;
    //    小时
    private int hour;
    //    是否刷新时间
    private boolean isPuase;
    private float secondLength;
    private float minuteLength;
    private float hourLength;
    private float minutAngle;

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
        initTime();
    }

    /**
     * 初始化时间
     */
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        second = calendar.get(Calendar.SECOND);
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isPuase) {
                    postInvalidate();
                    refreshTime();
                }
            }
        }, 0, 1000);
    }

    /**
     * 更新时间
     */
    private void refreshTime() {

        second++;
        if (second == 60) {
            second = 0;
            minute++;
        }

        if (minute == 60) {
            minute = 0;
            hour++;
        }

        if (hour == 24) {
            hour = 0;
        }
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
            float[] temp = setPoints((i + 1) * 30, r - LONG_DEGREE_LENGTH - degreeNumberSize / 2 - 15);
            canvas.drawText((i + 1) + "", temp[2], temp[3] + degreeNumberSize / 2 - 6, mPaintNumber);
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

        secondLength = r * 0.8f;
        minuteLength = r * 0.6f;
        hourLength = r * 0.5f;

        minutAngle = minute / 60f * 360;//初始角度
//        设置时针的起始点
        float[] hourPoints = setPoints(hour % 12 / 12f * 360 + minute * 5 / 60, hourLength);
        canvas.drawLine(hourPoints[0], hourPoints[1], hourPoints[2], hourPoints[3], mPaintHour);
        float[] minutePoints = setPoints(minute / 60f * 360, minuteLength);
        canvas.drawLine(minutePoints[0], minutePoints[1], minutePoints[2], minutePoints[3], mPaintMinute);
        float[] secondPoints = setPoints(second / 60f * 360, secondLength);
        canvas.drawLine(secondPoints[0], secondPoints[1], secondPoints[2], secondPoints[3], mPaintSecond);


//        表芯
        mPaintCenter.setColor(Color.GREEN);
        canvas.drawCircle(0, 0, 4, mPaintCenter);


        super.onDraw(canvas);


    }

    private float[] setPoints(float v, float length) {
        float[] floats = new float[4];
        if (v <= 90) {
            floats[0] = -(float) (Math.sin(v * Math.PI / 180) * DEFAULT_MORE_LENGTH);
            floats[1] = (float) (Math.cos(v * Math.PI / 180) * DEFAULT_MORE_LENGTH);
            floats[2] = (float) (Math.sin(v * Math.PI / 180) * length);
            floats[3] = -(float) (Math.cos(v * Math.PI / 180) * length);
        } else if (v <= 180f) {
            floats[0] = -(float) (Math.cos((v - 90) * Math.PI / 180) * DEFAULT_MORE_LENGTH);
            floats[1] = -(float) (Math.sin((v - 90) * Math.PI / 180) * DEFAULT_MORE_LENGTH);
            floats[2] = (float) (Math.cos((v - 90) * Math.PI / 180) * length);
            floats[3] = (float) (Math.sin((v - 90) * Math.PI / 180) * length);
        } else if (v <= 270f) {
            floats[0] = (float) (Math.sin((v - 180) * Math.PI / 180) * DEFAULT_MORE_LENGTH);
            floats[1] = -(float) (Math.cos((v - 180) * Math.PI / 180) * DEFAULT_MORE_LENGTH);
            floats[2] = -(float) (Math.sin((v - 180) * Math.PI / 180) * length);
            floats[3] = (float) (Math.cos((v - 180) * Math.PI / 180) * length);
        } else if (v <= 360f) {
            floats[0] = (float) (Math.cos((v - 270) * Math.PI / 180) * DEFAULT_MORE_LENGTH);
            floats[1] = (float) (Math.sin((v - 270) * Math.PI / 180) * DEFAULT_MORE_LENGTH);
            floats[2] = -(float) (Math.cos((v - 270) * Math.PI / 180) * length);
            floats[3] = -(float) (Math.sin((v - 270) * Math.PI / 180) * length);
        }
        return floats;
    }
}
