package com.heaven7.android.layout.uiapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.heaven7.android.layout.uiapp.R;
import com.heaven7.android.layout.uiapp.utils.DrawingUtils;

/**
 * 空心文字效果
 */
public class WatermarkView extends View {

    private final Paint mPaint = new Paint();
    private final RectF mRectF = new RectF();
    private final Rect mRect = new Rect();
    private String mText;
    private float mRotate;

    public WatermarkView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public WatermarkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        String color;
        float textSize;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WatermarkView);
        try {
            mText = ta.getString(R.styleable.WatermarkView_wv_text);
            color = ta.getString(R.styleable.WatermarkView_wv_text_color);
            textSize = ta.getDimensionPixelSize(R.styleable.WatermarkView_wv_text_size, 46);
            mRotate = ta.getFloat(R.styleable.WatermarkView_wv_rotate_degree, 0);
        }finally {
            ta.recycle();
        }
        if(color == null){
            color = "191,200,215,200";
        }
        mPaint.setColor(parseColor(color));
        mPaint.setTextSize(textSize);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mText != null){
            //DrawingUtils.measure(mPaint, )
            int modeW = MeasureSpec.getMode(widthMeasureSpec);
            int modeH = MeasureSpec.getMode(heightMeasureSpec);
            int sizeW = MeasureSpec.getSize(widthMeasureSpec);
            int sizeH = MeasureSpec.getSize(heightMeasureSpec);
            if(modeW == MeasureSpec.EXACTLY && modeH == MeasureSpec.EXACTLY){
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }else {
                Rect rect = DrawingUtils.measure(mPaint, mText);
                if(modeW != MeasureSpec.EXACTLY){
                    sizeW = rect.width();
                }
                if(modeH != MeasureSpec.EXACTLY){
                    sizeH = rect.height();
                }
                if(sizeW < getSuggestedMinimumWidth()){
                    sizeW = getSuggestedMinimumWidth();
                }
                if(sizeH < getSuggestedMinimumHeight()){
                    sizeH = getSuggestedMinimumHeight();
                }
                setMeasuredDimension(sizeW, sizeH);
            }
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mText == null){
            return;
        }
        mRect.set(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingLeft() - getPaddingRight(),
                getHeight() - getPaddingTop() - getPaddingBottom());
        //Rect rect = DrawingUtils.measure(mPaint, mText);
        DrawingUtils.computeTextDrawingCoordinate(mText, mPaint, mRect, mRectF);

        canvas.save();
       // mPaint.setColor(Color.argb(191, 200, 215, 200));
       // mPaint.setTextSize(46);
        canvas.rotate(mRotate, mRect.centerX(), mRect.centerY()); //-20
        /*canvas.drawText(mText + "", this.getWidth() / 4f, this.getHeight() / 2f, mPaint);
        canvas.drawText(mText + "", this.getWidth() / 1.5f, this.getHeight() / 2f, mPaint);
        canvas.drawText(mText + "", this.getWidth() / 1.5f, this.getHeight() / 4f, mPaint);
        canvas.drawText(mText + "", this.getWidth() / 4f, this.getHeight() / 4f, mPaint);*/
       // canvas.drawText(mText + "", this.getWidth() / 2f, this.getHeight() / 2f, mPaint);
        canvas.drawText(mText, mRectF.left, mRectF.top - mPaint.ascent(), mPaint);
        super.onDraw(canvas);
        canvas.restore();
    }
    public void setText(String text){
        this.mText = text;
        requestLayout();
        invalidate();
    }
    private static int parseColor(String color){
        if(color.startsWith("#")){
            return Color.parseColor(color);
        }
        String[] strs = color.split(",");
        if(strs.length == 4){ //argb
            return Color.argb(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]),
                    Integer.parseInt(strs[2]), Integer.parseInt(strs[3]));
        }else if(strs.length == 3){
            return Color.argb(191, Integer.parseInt(strs[0]),
                    Integer.parseInt(strs[1]), Integer.parseInt(strs[2]));
        }else {
            throw new UnsupportedOperationException("");
        }
    }
}