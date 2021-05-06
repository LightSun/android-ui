package com.heaven7.android.layout.uiapp.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;

public final class WatermarkHelper {

    public static Params.Builder newBuilder(){
        return new Params.Builder();
    }
    /**
     * draw water mark in target bounds
     * @param canvas the canvas
     * @param param the parameter
     * @param target the bounds
     */
    public static void drawWatermark(Canvas canvas, Params param, Rect target){
        Paint mPaint = new Paint();
        mPaint.setColor(parseColor(param.color));
        mPaint.setTextSize(param.textSize);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);

        RectF mRectF = new RectF();
        DrawingUtils.computeTextDrawingCoordinate(param.text, mPaint, target, mRectF);

        canvas.save();
        // mPaint.setColor(Color.argb(191, 200, 215, 200));
        // mPaint.setTextSize(46);
        canvas.rotate(param.rotate, target.centerX(), target.centerY()); //-20
        /*canvas.drawText(mText + "", this.getWidth() / 4f, this.getHeight() / 2f, mPaint);
        canvas.drawText(mText + "", this.getWidth() / 1.5f, this.getHeight() / 2f, mPaint);
        canvas.drawText(mText + "", this.getWidth() / 1.5f, this.getHeight() / 4f, mPaint);
        canvas.drawText(mText + "", this.getWidth() / 4f, this.getHeight() / 4f, mPaint);*/
        // canvas.drawText(mText + "", this.getWidth() / 2f, this.getHeight() / 2f, mPaint);
        canvas.drawText(param.text, mRectF.left, mRectF.top - mPaint.ascent(), mPaint);
        canvas.restore();
        //多行文字用 StaticLayout
       /* StaticLayout myStaticLayout = new StaticLayout(mWatermarkText, textPain,
                canvas.getWidth() - mGravityPadding,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.translate(getTextX(myStaticLayout.getWidth()), getTextY(myStaticLayout.getHeight()));
        myStaticLayout.draw(canvas);*/
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

    public static class Params{
        private String text;
        private float rotate = 0;
        private String color;
        private float textSize;

        protected Params(Params.Builder builder) {
            this.text = builder.text;
            this.rotate = builder.rotate;
            this.color = builder.color;
            this.textSize = builder.textSize;
        }

        public String getText() {
            return this.text;
        }

        public float getRotate() {
            return this.rotate;
        }

        public String getColor() {
            return this.color;
        }

        public float getTextSize() {
            return this.textSize;
        }

        public static class Builder {
            private String text;
            private float rotate = 0;
            private String color;
            private float textSize;

            public Builder setText(String text) {
                this.text = text;
                return this;
            }

            public Builder setRotate(float rotate) {
                this.rotate = rotate;
                return this;
            }

            public Builder setColor(String color) {
                this.color = color;
                return this;
            }

            public Builder setTextSize(float textSize) {
                this.textSize = textSize;
                return this;
            }

            public Params build() {
                return new Params(this);
            }
        }
    }
}
