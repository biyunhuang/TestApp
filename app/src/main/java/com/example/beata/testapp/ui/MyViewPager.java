package com.example.beata.testapp.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by biyun
 * on 2018/1/27.
 */

public class MyViewPager extends ViewGroup {

    private int mLastX;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaxVelocity;

    private int mCurrentPage = 0;

    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();
        mMaxVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        Log.d("TAG","--l-->"+l+",--t-->"+t+",-->r-->"+r+",--b-->"+b);
        for(int i = 0; i < count; i++){
            View child = getChildAt(i);
            child.layout(i * getWidth(), t, (i+1) * getWidth(), b);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = mLastX - x;
               /* int oldScrollX = getScrollX();
                int preScrollX = oldScrollX + dx;
                if (preScrollX > (getChildCount() -1) * getWidth()) {
                    preScrollX = (getChildCount() - 1) * getWidth();
                }
                if (preScrollX < 0) {
                    preScrollX = 0;
                }
                scrollTo(preScrollX, getScrollY());*/
               scrollBy(dx, 0);
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocity = (int) velocityTracker.getXVelocity();
                if (velocity > mMaxVelocity && mCurrentPage > 0) {
                    Log.d("TAG","----------------快速的向右滑--------------------");
                    scrollToPage(mCurrentPage - 1);
                } else if (velocity < -mMaxVelocity && mCurrentPage < (getChildCount() - 1)) {
                    Log.d("TAG","----------------快速的向左滑--------------------");
                    scrollToPage(mCurrentPage + 1);
                } else {
                    Log.d("TAG","----------------慢慢的滑动--------------------");
                    slowScrollToPage();
                }
                recyclerVelocityTracker();
                break;
        }

        return true;
    }

    private void scrollToPage(int pageIndex) {
        mCurrentPage = pageIndex;
        if (mCurrentPage > getChildCount() - 1) {
            mCurrentPage = getChildCount() - 1;
        }
        int dx = mCurrentPage * getWidth() - getScrollX();
        mScroller.startScroll(getScrollX(), 0 , dx, 0, Math.abs(dx) * 2);
        invalidate();
    }

    private void slowScrollToPage() {
        int page = (getScrollX() + getWidth() / 2) / getWidth();
        scrollToPage(page);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recyclerVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
