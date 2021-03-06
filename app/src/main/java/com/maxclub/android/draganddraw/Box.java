package com.maxclub.android.draganddraw;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

public class Box implements Parcelable {
    private PointF mOrigin;
    private PointF mCurrent;
    private float mRotationDegree;

    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
        mRotationDegree = 0;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public float getRotationDegree() {
        return mRotationDegree;
    }

    public void setRotationDegree(float rotationDegree) {
        mRotationDegree = rotationDegree;
    }

    public static final Parcelable.Creator<Box> CREATOR = new Parcelable.Creator<Box>() {
        public Box createFromParcel(Parcel in) {
            return new Box(in);
        }

        public Box[] newArray(int size) {
            return new Box[size];
        }
    };

    private Box(Parcel in) {
        mOrigin = (PointF) in.readValue(ClassLoader.getSystemClassLoader());
        mCurrent = (PointF) in.readValue(ClassLoader.getSystemClassLoader());
        mRotationDegree = (float) in.readValue(ClassLoader.getSystemClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mOrigin);
        dest.writeValue(mCurrent);
        dest.writeValue(mRotationDegree);
    }
}
