package com.maxclub.android.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";

    private static final String KEY_SAVE_INSTANCE_STATE = "SaveInstanceState";
    private static final String KEY_BOXEN = "Boxen";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    private RectF mRect;
    private Path mPathRect;
    private Matrix mMatrix;
    private float mStartRotationDegree;

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x556200EE);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xFFF1E7FC);

        mRect = new RectF();
        mPathRect = new Path();
        mMatrix = new Matrix();
    }

    public void clear() {
        mBoxen.clear();
        invalidate();
    }

    public void undo() {
        if (isUndoEnable()) {
            mBoxen.remove(mBoxen.size() - 1);
            invalidate();
        }
    }

    public boolean isUndoEnable() {
        return !mBoxen.isEmpty();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxen) {
            mRect.left = Math.min(box.getOrigin().x, box.getCurrent().x);
            mRect.right = Math.max(box.getOrigin().x, box.getCurrent().x);
            mRect.top = Math.min(box.getOrigin().y, box.getCurrent().y);
            mRect.bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            mPathRect.reset();
            mPathRect.addRect(mRect, Path.Direction.CW);

            mMatrix.reset();
            mMatrix.setRotate(box.getRotationDegree(), mRect.centerX(), mRect.centerY());
            mPathRect.transform(mMatrix);

            canvas.drawPath(mPathRect, mBoxPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());

        int actionMask = event.getActionMasked();
        int pointerCount = event.getPointerCount();

        switch (actionMask) {
            case MotionEvent.ACTION_DOWN:
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mStartRotationDegree = getDegreeBetweenPointers(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentBox != null) {
                    if (pointerCount == 1) {
                        mCurrentBox.setCurrent(current);
                    } else if (pointerCount == 2) {
                        float currentAngleDegree = getDegreeBetweenPointers(event);
                        mCurrentBox.setRotationDegree(currentAngleDegree - mStartRotationDegree);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                mCurrentBox = null;
                break;
        }

        return true;
    }

    private float getDegreeBetweenPointers(MotionEvent event) {
        float deltaX = (event.getX(0) - event.getX(1));
        float deltaY = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(deltaY, deltaX);

        return (float) Math.toDegrees(radians);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Log.i(TAG, "onSaveInstanceState");

        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SAVE_INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putParcelableArrayList(KEY_BOXEN, (ArrayList<? extends Parcelable>) mBoxen);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.i(TAG, "onRestoreInstanceState");

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable(KEY_SAVE_INSTANCE_STATE);
            mBoxen = bundle.getParcelableArrayList(KEY_BOXEN);
        }
        super.onRestoreInstanceState(state);
    }
}
