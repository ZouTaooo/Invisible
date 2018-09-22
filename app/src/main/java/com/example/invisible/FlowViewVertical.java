package com.example.invisible;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Map;

/**
 * Created by paike on 2017/2/10.
 * xyz@163.com
 */


public class FlowViewVertical extends View {
    private static final String TAG = "FlowViewVertical";
    private Paint bgPaint;
    private Paint proPaint;
    private TextPaint textPaint;
    private float bgRadius;
    private float proRadius;
    private int lineBgWidth;
    private int bgColor;
    private int lineProWidth;
    private int proColor;
    private int interval;
    private int bgPositionX;
    private int maxStep;
    private int proStep;
    private int textPaddingLeft;
    private int timePaddingRight;
    private int textMoveTop;
    private int timeMoveTop;
    private int textsize;
    private float starY;
    private float stopY;
    private String[] titles;
    private String[] times;
    private int border;
    int widthMeasureSpec;
    int heightMeasureSpec;
    private Map<String, String> map;

    public FlowViewVertical(Context context) {
        this(context, null);
    }

    public FlowViewVertical(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowViewVertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, com.xyz.step.R.styleable.FlowViewVertical);
        bgRadius = ta.getDimension(com.xyz.step.R.styleable.FlowViewVertical_v_bg_radius, 10);
        proRadius = ta.getDimension(com.xyz.step.R.styleable.FlowViewVertical_v_pro_radius, 8);
        lineBgWidth = (int) ta.getDimension(com.xyz.step.R.styleable.FlowViewVertical_v_bg_width, 3f);
        bgColor = ta.getColor(com.xyz.step.R.styleable.FlowViewVertical_v_bg_color, Color.parseColor("#cdcbcc"));
        lineProWidth = (int) ta.getDimension(com.xyz.step.R.styleable.FlowViewVertical_v_pro_width, 2f);
        proColor = ta.getColor(com.xyz.step.R.styleable.FlowViewVertical_v_pro_color, Color.parseColor("#029dd5"));
        interval = (int) ta.getDimension(com.xyz.step.R.styleable.FlowViewVertical_v_interval, 140);
        maxStep = ta.getInt(com.xyz.step.R.styleable.FlowViewVertical_v_max_step, 5);
        proStep = ta.getInt(com.xyz.step.R.styleable.FlowViewVertical_v_pro_step, 3);
        bgPositionX = (int) ta.getDimension(com.xyz.step.R.styleable.FlowViewVertical_v_bgPositionX, 200);
        textPaddingLeft = (int) ta.getDimension(com.xyz.step.R.styleable.FlowViewVertical_v_textPaddingLeft, 40);
        timePaddingRight = (int) ta.getDimension(com.xyz.step.R.styleable.FlowViewVertical_v_timePaddingRight, 80);
        textMoveTop = (int) ta.getDimension(com.xyz.step.R.styleable.FlowViewVertical_v_textMoveTop, 10);
        timeMoveTop = (int) ta.getDimension(com.xyz.step.R.styleable.FlowViewVertical_v_timeMoveTop, 8);
        textsize = (int) ta.getDimension(com.xyz.step.R.styleable.FlowViewVertical_v_textsize, 17);
        ta.recycle();
        init();
    }

    private void init() {
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(bgColor);
        bgPaint.setStrokeWidth(lineBgWidth);

        proPaint = new Paint();
        proPaint.setAntiAlias(true);
        proPaint.setStyle(Paint.Style.FILL);
        proPaint.setColor(proColor);
        proPaint.setStrokeWidth(lineProWidth);

        textPaint = new TextPaint();
        textPaint.setTextSize(textsize);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
        Log.e(TAG, "onMeasure: ");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int bgWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            bgWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        } else
            bgWidth = dip2px(getContext(), 311);
        starY = getPaddingTop() + bgRadius;
        Log.e(TAG, "onMeasure: startY" + starY);
        stopY = getPaddingTop() + bgRadius + (maxStep - 1) * interval;
        Log.e(TAG, "onMeasure: stopY" + starY);
        float bottom = stopY + bgRadius + getPaddingBottom();
        border = bgWidth - (bgPositionX + textPaddingLeft);
        setMeasuredDimension(bgWidth, (int) bottom);
    }

    /**
     * 把密度转换为像素
     */
    static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }

    /**
     * 得到设备的密度
     */
    private static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw: ");
        drawBg(canvas);
        drawProgress(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        for (int i = 0; i < maxStep; i++) {
            setPaintColor(i);
            if (null != times && i < proStep)
                canvas.drawText(times[i], bgPositionX - timePaddingRight, stopY - (i * interval) + timeMoveTop, textPaint);
            if (null != titles) {
                canvas.save();
                canvas.translate(bgPositionX + textPaddingLeft, (stopY - (i * interval) - textMoveTop));
                StaticLayout sl = new StaticLayout(titles[i], textPaint, border, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                sl.draw(canvas);
                canvas.restore();
            }
        }
    }

    private void drawProgress(Canvas canvas) {
        int linePro;
        float lastBottom = stopY;
        for (int i = 0; i < proStep; i++) {
            setPaintColor(i);
            if (i == 0 || i == maxStep - 1)
                linePro = interval / 2;
            else
                linePro = interval;
            canvas.drawLine(bgPositionX, lastBottom, bgPositionX, lastBottom - linePro, proPaint);
            Log.e(TAG, "drawProgress: line");
            lastBottom = lastBottom - linePro;
            canvas.drawCircle(bgPositionX, stopY - (i * interval), proRadius, proPaint);
            Log.e(TAG, "drawProgress: circle");
        }
    }

    private void setPaintColor(int i) {
        if (i < proStep) {
            textPaint.setColor(proColor);
        } else {
            textPaint.setColor(bgColor);
        }
        if (titles == null || map == null) return;
        String title = titles[i];
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (title.contains(entry.getKey())) {
                String value = entry.getValue();
                proPaint.setColor(Color.parseColor(value));
                textPaint.setColor(Color.parseColor(value));
                return;
            } else {
                proPaint.setColor(proColor);
            }
        }
    }

    private void drawBg(Canvas canvas) {
        if (titles != null) {
            canvas.drawLine(bgPositionX, stopY, bgPositionX, starY, bgPaint);
        }
        Log.e(TAG, "drawBg: line");
        for (int i = 0; i < maxStep; i++) {
            Log.e(TAG, "drawBg: circle");
            canvas.drawCircle(bgPositionX, stopY - (i * interval), bgRadius, bgPaint);
        }
    }

    public void setKeyColor(Map<String, String> map) {
        this.map = map;
    }

    public void setProgress(int progress, int maxStep, String[] titles, String[] times) {
        Log.e(TAG, "setProgress: ");
        proStep = progress;
        this.maxStep = maxStep;
        this.titles = titles;
        this.times = times;
        requestLayout();
        invalidate();
    }
}
