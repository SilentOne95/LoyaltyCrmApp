package com.example.konta.sketch_loyalityapp.ModelClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Fixing problem with moving map upwards and downwards as parent of MapFragment (NestedScrollView)
 * was taking control over certain touch events.
 */
public class CustomMapFrameLayout extends FrameLayout {

    public CustomMapFrameLayout(@NonNull Context context) {
        super(context);
    }

    public CustomMapFrameLayout(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_DOWN:
                this.getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(motionEvent);
    }
}