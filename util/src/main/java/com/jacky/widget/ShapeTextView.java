package com.jacky.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.jacky.util.R;

/**
 * 用于添加边框和背景，免于写 一堆的 xml 文件
 */
public class ShapeTextView extends AppCompatTextView {

    public ShapeTextView(Context context) {
        super(context);
        initView(context, null);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private float topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius;
    private Paint mPaint;
    private int strikeColor, solid;
    private float strikeWidth;
    private RectF mRect = new RectF();

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShapeTextView);
        topLeftRadius = topRightRadius = bottomLeftRadius = bottomRightRadius =
                a.getDimension(R.styleable.ShapeTextView_android_radius, 0);
        if(a.hasValue(R.styleable.ShapeTextView_android_topLeftRadius)) {
            topLeftRadius = a.getDimension(R.styleable.ShapeTextView_android_topLeftRadius, 0);
        }
        if(a.hasValue(R.styleable.ShapeTextView_android_topRightRadius)) {
            topRightRadius = a.getDimension(R.styleable.ShapeTextView_android_topRightRadius, 0);
        }
        if(a.hasValue(R.styleable.ShapeTextView_android_bottomLeftRadius)) {
            bottomLeftRadius = a.getDimension(R.styleable.ShapeTextView_android_bottomLeftRadius, 0);
        }
        if(a.hasValue(R.styleable.ShapeTextView_android_bottomRightRadius)) {
            bottomRightRadius = a.getDimension(R.styleable.ShapeTextView_android_bottomRightRadius, 0);
        }

        strikeWidth = a.getDimension(R.styleable.ShapeTextView_strikeWidth, 0);
        strikeColor = a.getColor(R.styleable.ShapeTextView_strikeColor, 0x0);
        solid = a.getColor(R.styleable.ShapeTextView_solid, 0x0);
        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public void setSolidColor(int color) {
        solid = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth(), height = getHeight();

        Path path = new Path();
        path.moveTo(strikeWidth, topLeftRadius * 2 + strikeWidth);

        if(topLeftRadius > 0) { //绘制左上角的圆弧
            mRect.top = mRect.left = strikeWidth;
            mRect.right = mRect.bottom = topLeftRadius * 2 + strikeWidth;
            path.arcTo(mRect, 180, 90);
        }

//        path.lineTo(width - topRightRadius - strikeWidth, strikeWidth);

        if(topRightRadius > 0) {
            mRect.top = strikeWidth;
            mRect.left = width - topRightRadius - topRightRadius - strikeWidth;
            mRect.right = width - strikeWidth;
            mRect.bottom = topRightRadius + topRightRadius + strikeWidth;
            path.arcTo(mRect, 270, 90);
        }

//        path.lineTo(width - strikeWidth, height - bottomRightRadius - strikeWidth);

        if(bottomRightRadius > 0) {
            mRect.top = height - strikeWidth - bottomRightRadius * 2;
            mRect.left = width - strikeWidth - bottomRightRadius * 2;
            mRect.right = width - strikeWidth;
            mRect.bottom = height - strikeWidth;
            path.arcTo(mRect, 0, 90);
        }

//        path.lineTo(bottomLeftRadius + strikeWidth, height - strikeWidth);

        if(bottomLeftRadius > 0) {
            mRect.top = height - strikeWidth - bottomLeftRadius * 2;
            mRect.left = strikeWidth;
            mRect.right = strikeWidth + bottomLeftRadius * 2;
            mRect.bottom = height - strikeWidth;
            path.arcTo(mRect, 90, 90);
        }
//        path.lineTo(strikeWidth, strikeWidth + topLeftRadius);
        path.close();

        mPaint.setColor(solid);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, mPaint);

        if(strikeWidth > 0) {
            mPaint.setColor(strikeColor);
            mPaint.setStrokeWidth(strikeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, mPaint);
        }
        super.onDraw(canvas);
    }
}
