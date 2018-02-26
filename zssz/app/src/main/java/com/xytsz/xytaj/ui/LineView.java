package com.xytsz.xytaj.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.xytsz.xytaj.R;

/**
 * Created by admin on 2017/7/18.
 * 签到  画线
 */
public class LineView extends View {

    private float mEndX;
    private float endx;
    private float halfWidth;
    private float endWidth;
    private RunnableTask runnableTask;
    private String simpletext;
    private String simple;
    private String experttext;
    private String expert;

    private String supertext;
    private String supernumber;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            updateX();

        }
    };

    private Context context;

    public LineView(Context context, float endX) {
        super(context, null);
        this.endx = endX;
        this.context = context;

        simpletext = context.getString(R.string.sign_simple);
        simple = context.getString(R.string.sign_simple_standar);
        experttext = context.getString(R.string.sign_great);
        expert = context.getString(R.string.sign_grent_standar);
        supertext = context.getString(R.string.sign_super);
        supernumber = context.getString(R.string.sign_super_standar);


        runnableTask = new RunnableTask();
    }

    public LineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    class RunnableTask implements Runnable {
        @Override
        public void run() {
            // 进行滚动操作
            handler.obtainMessage().sendToTarget();// 将延迟消息转发到handler中
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // 去除延迟消息
        handler.removeCallbacksAndMessages(null);// 去除handler延迟消息操作
        super.onDetachedFromWindow();
    }

    public void updateX() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mEndX < endx) {
                    while (mEndX != endx) {
                        mEndX += 1;
                        handler.postDelayed(runnableTask, 500);

                    }
                }
            }
        }.start();

        invalidate();
    }

    private float y = (float) 7;


    @Override
    protected void onDraw(Canvas canvas) {
        //初始线
        Paint originalPaint = new Paint();
        originalPaint.setColor(context.getResources().getColor(R.color.graybackground));
        originalPaint.setStrokeWidth(5);
        originalPaint.setStyle(Paint.Style.FILL);
        canvas.drawLine(y * 2, y, halfWidth - y, y, originalPaint);
        canvas.drawLine(halfWidth + y, y, endWidth - 2 * y, y, originalPaint);


        //画原点
        Paint circlePaint = new Paint();
        circlePaint.setColor(context.getResources().getColor(R.color.cc_supervise));
        circlePaint.setStrokeWidth(5);
        canvas.drawCircle(y, y, 7, circlePaint);
        canvas.drawCircle(halfWidth, y, 7, circlePaint);
        canvas.drawCircle(endWidth - y, y, 7, circlePaint);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(context.getResources().getColor(R.color.cc_supervise));
        textPaint.setTextSize(30);
        Paint textPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint1.setColor(context.getResources().getColor(R.color.cc_dark_fade));
        textPaint1.setTextSize(30);
        float stringWidth = textPaint.measureText(supertext);
        float string2Width = textPaint.measureText(supernumber);
        // textPaint.setStrokeWidth(10);
        canvas.drawText(simpletext, 0, 7 * y, textPaint);
        canvas.drawText(simple, 0, 13 * y, textPaint1);
        canvas.drawText(experttext, halfWidth - stringWidth / 2, 7 * y, textPaint);
        canvas.drawText(expert, halfWidth - stringWidth/2, 13 * y, textPaint1);
        canvas.drawText(supertext, endWidth - stringWidth, 7 * y, textPaint);
        canvas.drawText(supernumber, endWidth - string2Width, 13 * y, textPaint1);


        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.cc_supervise));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawLine(y, y, mEndX, y, paint);


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        halfWidth = w / 2;
        endWidth = w;
    }
}
