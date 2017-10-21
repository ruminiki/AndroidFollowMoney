package br.com.followmoney.components;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MyGestureListener implements GestureDetector.OnGestureListener {
    private static final String DEBUG_TAG = "Gestures";

    private OnGestureListener onGestureListener;

    public MyGestureListener(OnGestureListener onGestureListener) {
        this.onGestureListener = onGestureListener;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() < e2.getX()) {
            Log.d(DEBUG_TAG, "Left to Right swipe performed");
            onGestureListener.leftToRightGesture();
        }

        if (e1.getX() > e2.getX()) {
            Log.d(DEBUG_TAG, "Right to Left swipe performed");
            onGestureListener.rightToLeftGesture();
        }

        return true;
    }

    public interface OnGestureListener<T> {
        public void leftToRightGesture();
        public void rightToLeftGesture();
    }
}