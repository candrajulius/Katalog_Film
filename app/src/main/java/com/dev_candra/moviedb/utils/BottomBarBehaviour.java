package com.dev_candra.moviedb.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;

public class BottomBarBehaviour extends CoordinatorLayout.Behavior<BubbleNavigationLinearView> {

    private int Height;

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull BubbleNavigationLinearView child, int layoutDirection) {

        Height = child.getHeight();

        return super.onLayoutChild(parent, child, layoutDirection);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BubbleNavigationLinearView child, @NonNull View directTargetChild, @NonNull View target, int axes) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BubbleNavigationLinearView child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {

        if (dyConsumed > 0){
            slideDown(child);
        }else if (dyConsumed < 0){
            slideUp(child);
        }
    }

    private void slideUp(BubbleNavigationLinearView child){
        child.clearAnimation();
        child.animate().translationY(0).setDuration(200);
    }

    private void slideDown(BubbleNavigationLinearView child){
        child.clearAnimation();
        child.animate().translationY(Height).setDuration(200);
    }
}
