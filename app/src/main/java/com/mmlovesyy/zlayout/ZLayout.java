package com.mmlovesyy.zlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cmm on 16/3/7.
 */
public class ZLayout extends ViewGroup {

    private static final String TAG = "ZLayout";

    private int mWidth;
    private int mHeight;
    private int mLineCount;
    private int mChildCountNeedLayout = 0;

    // attrs
    private float mLineSpacing = 0;
    private int mMaxLines = Integer.MAX_VALUE;

    public ZLayout(Context context) {
        super(context);
    }

    public ZLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ZLayout, 0, 0);
        try {
            mLineSpacing = ta.getDimension(R.styleable.ZLayout_lineSpacing, 0);
            mMaxLines = ta.getInt(R.styleable.ZLayout_maxLines, Integer.MAX_VALUE);

            Log.d(TAG, "mLineSpacing: " + mLineSpacing);
            Log.d(TAG, "mMaxLines: " + mMaxLines);
        } finally {
            ta.recycle();
        }
    }

    public ZLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.d(TAG, "onMeasure...");

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        mWidth = measureWidth(widthMeasureSpec);
        mHeight = measureHeight(heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);
    }

    private int measureWidth(int widthMeasureSpec) {

        Log.d(TAG, "enter measureWidth(), widthMeasureSpec: " + MeasureSpec.toString(widthMeasureSpec));

        int count = getChildCount();
        mChildCountNeedLayout = count;

        if (count <= 0 || mMaxLines <= 0) {
            return 0;
        }

        int needWidth = 0;
        for (int i = 0; i < count; i++) {
            needWidth += getMeasuredWidthWithMargins(getChildAt(i));
        }

        int width = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (widthMode) {

            case MeasureSpec.EXACTLY:
                width = widthSize;

                if (needWidth > 0) {

                    mLineCount = 1;

                    int widthUsed = 0;
                    for (int i = 0; i < count; i++) {

                        View child = getChildAt(i);
                        LayoutParams lp = (LayoutParams) child.getLayoutParams();
                        final int leftMargin = lp.leftMargin;
                        final int childMeasuredWidth = child.getMeasuredWidth();

                        if (widthUsed + childMeasuredWidth + leftMargin > width && widthUsed != 0) {
                            mLineCount++;
                            widthUsed = 0;

                            if (mLineCount > mMaxLines) {
                                mLineCount = mMaxLines;
                                mChildCountNeedLayout = i;
                                break;
                            }
                        }

                        widthUsed += childMeasuredWidth + leftMargin;
                    }

                } else {
                    mLineCount = 0;
                }

                break;

            case MeasureSpec.AT_MOST:
                if (needWidth > 0) {

                    mLineCount = 1;

                    width = Math.min(needWidth, widthSize);

                    int widthUsed = 0;
                    int maxWidthUsed = 0;
                    for (int i = 0; i < count; i++) {

                        View child = getChildAt(i);
                        final int childMeasuredWidth = child.getMeasuredWidth();
                        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                        final int leftMargin = lp.leftMargin;

                        if (widthUsed + childMeasuredWidth + leftMargin > width && widthUsed != 0) {
                            mLineCount++;

                            if (mLineCount > mMaxLines) {
                                mLineCount = mMaxLines;
                                mChildCountNeedLayout = i;
                                break;
                            }

                            widthUsed = 0;
                        }

                        widthUsed += childMeasuredWidth + leftMargin;
                        maxWidthUsed = Math.max(maxWidthUsed, widthUsed);
                    }

                    width = maxWidthUsed;

                } else {
                    width = 0;
                    mLineCount = 0;
                }

                break;

            case MeasureSpec.UNSPECIFIED:
                width = needWidth;
                mLineCount = 1;

                break;

        }

        Log.d(TAG, "after measureWidth(), mWidth: " + width + ", mLineCount: " + mLineCount);

        return width;
    }

    private int measureHeight(int heightMeasureSpec) {

        Log.d(TAG, "enter measureHeight(), heightMeasureSpec: " + MeasureSpec.toString(heightMeasureSpec));

        int count = getChildCount();

        if (count <= 0 || mMaxLines <= 0) {
            return 0;
        }

        int childHeight = getMeasuredHeightWithMargins(getChildAt(0));

        int height = 0;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int totalSpacing = mLineCount > 1 ? (int) ((mLineCount - 1) * mLineSpacing) : 0;

        switch (heightMode) {

            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;

            case MeasureSpec.AT_MOST:
                int needHeight = mLineCount * childHeight;
                height = Math.min(needHeight + totalSpacing, heightSize);

                break;

            case MeasureSpec.UNSPECIFIED:
                height = mLineCount * childHeight + totalSpacing;

                break;
        }

        Log.d(TAG, "after measureHeight(), mHeight: " + height);

        return height;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.d(TAG, "onLayout...");

        Log.d(TAG, "mChildCountNeedLayout: " + mChildCountNeedLayout);

        int contentWidth = r - l - getPaddingLeft() - getPaddingRight();
        int startX = getPaddingLeft();
        int startY = getPaddingTop();

        for (int i = 0; i < mChildCountNeedLayout; i++) {
            View child = getChildAt(i);

            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();

            ZLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int leftMargin = lp.leftMargin;
            final int topMargin = lp.topMargin;

            if (i != 0 && startX + leftMargin + childWidth > contentWidth) {
                startX = getPaddingLeft();
                startY += getMeasuredHeightWithMargins(child) + mLineSpacing;
            }

            child.layout(startX + leftMargin, startY + topMargin, startX + leftMargin + childWidth, startY + topMargin + childHeight);
            startX += getMeasuredWidthWithMargins(child);
        }
    }

    public void setMaxLines(int maxLines) {
        this.mMaxLines = maxLines;
        requestLayout();
    }

    public void setLineSpacing(int lineSpacing) {
        this.mLineSpacing = lineSpacing;
        requestLayout();
    }

    private static int getMeasuredWidthWithMargins(View v) {
        ZLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
        return v.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    private static int getMeasuredHeightWithMargins(View v) {
        ZLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
        return v.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
