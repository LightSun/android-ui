package com.heaven7.android.ui.round;

import android.os.Parcel;
import android.os.Parcelable;

public class RoundParameters implements Parcelable {

    public static final int STYLE_STROKE = 1;
    public static final int STYLE_FILL = 2;
    public static final int STYLE_STROKE_AND_FILL = 3;

    private float radiusX;
    private float radiusY;
    private float borderWidthX;
    private float borderWidthY;

    private boolean roundAfterPadding = true;
    private int borderColor;
    private boolean circle;
    private int style; //1, stroke, 2.fill, 3, stroke+fill

    public RoundParameters(){}
    public RoundParameters(RoundParameters p){
        if(p != null){
            this.radiusX = p.radiusX;
            this.radiusY = p.radiusY;
            this.borderWidthX = p.borderWidthX;
            this.borderWidthY = p.borderWidthY;
            this.roundAfterPadding = p.roundAfterPadding;
            this.borderColor = p.borderColor;
            this.circle = p.circle;
            this.style = p.style;
        }
    }

    public void setRadiusX(float radiusX) {
        this.radiusX = radiusX;
    }
    public void setRadiusY(float radiusY) {
        this.radiusY = radiusY;
    }

    public void setRoundAfterPadding(boolean roundAfterPadding) {
        this.roundAfterPadding = roundAfterPadding;
    }
    public void setCircle(boolean circle) {
        this.circle = circle;
    }

    public void setBorderWidthX(float borderWidthX) {
        this.borderWidthX = borderWidthX;
    }
    public void setBorderWidthY(float borderWidthY) {
        this.borderWidthY = borderWidthY;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }
    public boolean hasBorder() {
        return borderWidthX > 0 || borderWidthY > 0;
    }
    protected RoundParameters(RoundParameters.Builder builder) {
        this.radiusX = builder.radiusX;
        this.radiusY = builder.radiusY;
        this.borderWidthX = builder.borderWidthX;
        this.borderWidthY = builder.borderWidthY;
        this.roundAfterPadding = builder.roundAfterPadding;
        this.borderColor = builder.borderColor;
        this.circle = builder.circle;
        this.style = builder.style;
    }

    public float getRadiusX() {
        return this.radiusX;
    }

    public float getRadiusY() {
        return this.radiusY;
    }

    public float getBorderWidthX() {
        return this.borderWidthX;
    }

    public float getBorderWidthY() {
        return this.borderWidthY;
    }

    public boolean isRoundAfterPadding() {
        return this.roundAfterPadding;
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public boolean isCircle() {
        return this.circle;
    }
    //has
    public boolean isValid(){
        return radiusX > 0 || radiusY > 0 || borderWidthX > 0 || borderWidthY > 0 || circle;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public static class Builder {
        private float radiusX;
        private float radiusY;
        private float borderWidthX;
        private float borderWidthY;
        private boolean roundAfterPadding = true;
        private int borderColor;
        private boolean circle;
        private int style = STYLE_FILL;

        public Builder setStyle(int style){
            this.style = style;
            return this;
        }
        public Builder setRadiusX(float radiusX) {
            this.radiusX = radiusX;
            return this;
        }

        public Builder setRadiusY(float radiusY) {
            this.radiusY = radiusY;
            return this;
        }

        public Builder setBorderWidthX(float borderWidthX) {
            this.borderWidthX = borderWidthX;
            return this;
        }

        public Builder setBorderWidthY(float borderWidthY) {
            this.borderWidthY = borderWidthY;
            return this;
        }

        public Builder setRoundAfterPadding(boolean roundAfterPadding) {
            this.roundAfterPadding = roundAfterPadding;
            return this;
        }

        public Builder setBorderColor(int borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder setCircle(boolean circle) {
            this.circle = circle;
            return this;
        }

        public RoundParameters build() {
            return new RoundParameters(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.radiusX);
        dest.writeFloat(this.radiusY);
        dest.writeFloat(this.borderWidthX);
        dest.writeFloat(this.borderWidthY);
        dest.writeByte(this.roundAfterPadding ? (byte) 1 : (byte) 0);
        dest.writeInt(this.borderColor);
        dest.writeByte(this.circle ? (byte) 1 : (byte) 0);
        dest.writeInt(this.style);
    }

    protected RoundParameters(Parcel in) {
        this.radiusX = in.readFloat();
        this.radiusY = in.readFloat();
        this.borderWidthX = in.readFloat();
        this.borderWidthY = in.readFloat();
        this.roundAfterPadding = in.readByte() != 0;
        this.borderColor = in.readInt();
        this.circle = in.readByte() != 0;
        this.style = in.readInt();
    }

    public static final Creator<RoundParameters> CREATOR = new Creator<RoundParameters>() {
        @Override
        public RoundParameters createFromParcel(Parcel source) {
            return new RoundParameters(source);
        }

        @Override
        public RoundParameters[] newArray(int size) {
            return new RoundParameters[size];
        }
    };
}