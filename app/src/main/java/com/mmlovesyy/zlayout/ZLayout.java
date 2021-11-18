package com.mmlovesyy.zlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ZLayout extends ViewGroup {

    private static final String TAG = "ZLayout";
    private static final boolean DEBUG = false;

    private int mWidth;
    private int mHeight;
    private int mLineCount;
    private int mChildCountNeedLayout = 0;

    private ArrayList<Integer> mBreakAtChild = new ArrayList<>(4);
    ArrayList<Integer> mMaxHeightInEachLine = new ArrayList<>(4);

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

        if (DEBUG) {
            Log.d(TAG, "onMeasure...");
        }

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        mBreakAtChild.clear();

        mWidth = measureWidth(widthMeasureSpec);
        mHeight = measureHeight(heightMeasureSpec);

        if (DEBUG) {
            Log.d(TAG, "mChildCountNeedLayout: " + mChildCountNeedLayout);
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * measure child with margins
     *
     * @param child
     * @param parentWidthMeasureSpec
     * @param parentHeightMeasureSpec
     */
    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        final ViewGroup.LayoutParams lp = child.getLayoutParams();

        int horizontalMargins = 0;
        int verticalMargins = 0;
        if (lp instanceof ZLayout.MarginLayoutParams) {
            horizontalMargins = ((MarginLayoutParams) lp).leftMargin + ((MarginLayoutParams) lp).rightMargin;
            verticalMargins = ((MarginLayoutParams) lp).topMargin + ((MarginLayoutParams) lp).bottomMargin;
        }

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + horizontalMargins, lp.width);

        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + verticalMargins, lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    protected int measureWidth(int widthMeasureSpec) {

        if (DEBUG) {
            Log.d(TAG, "enter measureWidth(), widthMeasureSpec: " + MeasureSpec.toString(widthMeasureSpec));
        }

        int count = getChildCount();
        mChildCountNeedLayout = 0;

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

                    mLineCount = 0;

                    int widthUsed = width;
                    for (int i = 0; i < count; ) {

                        View child = getChildAt(i);

                        if (child.getVisibility() == View.GONE) {
                            i++;
                            continue;
                        }

                        final int childMeasuredWidth = getMeasuredWidthWithMargins(child);

                        if ((i == 0 || mBreakAtChild.contains(i)) && childMeasuredWidth > width) {
                            break;
                        }

                        if (widthUsed + childMeasuredWidth <= (width - getPaddingLeft() - getPaddingRight())) {
                            mChildCountNeedLayout++;
                            widthUsed += childMeasuredWidth;

                            i++;

                        } else {

                            if (mLineCount > mMaxLines) {
                                break;

                            } else {
                                mLineCount++;
                                mBreakAtChild.add(i);
                                widthUsed = 0;
                            }
                        }
                    }

                } else {
                    mLineCount = 0;
                }

                break;

            case MeasureSpec.AT_MOST:
                if (needWidth > 0) {

                    mLineCount = 0;

                    width = Math.min(needWidth, widthSize);

                    int widthUsed = width;
                    int maxWidthUsed = 0;
                    for (int i = 0; i < count; ) {

                        View child = getChildAt(i);

                        if (child.getVisibility() == View.GONE) {
                            i++;
                            continue;
                        }

                        final int childMeasuredWidth = getMeasuredWidthWithMargins(child);

                        if ((i == 0 || mBreakAtChild.contains(i)) && childMeasuredWidth > width) {
                            break;
                        }

                        if (widthUsed + childMeasuredWidth <= width - getPaddingLeft() - getPaddingRight()) {
                            mChildCountNeedLayout++;
                            widthUsed += childMeasuredWidth;
                            maxWidthUsed = Math.max(maxWidthUsed, widthUsed);

                            i++;

                        } else {

                            if (mLineCount > mMaxLines) {
                                break;

                            } else {
                                mLineCount++;
                                mBreakAtChild.add(i);
                                widthUsed = 0;
                            }
                        }
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

        if (DEBUG) {
            Log.d(TAG, "after measureWidth(), mWidth: " + width + ", mLineCount: " + mLineCount);
        }

        return width;
    }

    private int measureHeight(int heightMeasureSpec) {

        if (DEBUG) {
            Log.d(TAG, "enter measureHeight(), heightMeasureSpec: " + MeasureSpec.toString(heightMeasureSpec));
        }

        int count = getChildCount();

        if (count <= 0 || mMaxLines <= 0 || mLineCount == 0) {
            return 0;
        }

        if (DEBUG) {
            Log.d(TAG, "mBreakAtChild: " + mBreakAtChild);
        }

        mMaxHeightInEachLine = new ArrayList<>(mBreakAtChild.size());

        // 简单的认为每行的第1个子view 的高度为该行最大高度
        for (int i : mBreakAtChild) {
            mMaxHeightInEachLine.add(getMeasuredHeightWithMargins(getChildAt(i)));
        }

        if (DEBUG) {
            Log.d(TAG, "mMaxHeightInEachLine: " + mMaxHeightInEachLine);
        }

        int needHeight = 0;
        for (int height : mMaxHeightInEachLine) {
            needHeight += height;
        }

        int height = 0;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int totalSpacing = mLineCount > 1 ? (int) ((mLineCount - 1) * mLineSpacing) : 0;

        switch (heightMode) {

            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;

            case MeasureSpec.AT_MOST:
                height = Math.min(needHeight + totalSpacing + getPaddingBottom() + getPaddingTop(), heightSize);

                break;

            case MeasureSpec.UNSPECIFIED:
                height = needHeight + totalSpacing + getPaddingBottom() + getPaddingTop();

                break;
        }

        if (DEBUG) {
            Log.d(TAG, "after measureHeight(), mHeight: " + height);
        }

        return height;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (DEBUG) {
            Log.d(TAG, "onLayout..." + mBreakAtChild);
//            System.out.println("onLayout..." + mBreakAtChild + ", mChildCountNeedLayout: " + mChildCountNeedLayout);
        }

        int contentWidth = r - l - getPaddingRight();
        int startX = getPaddingLeft();
        int startY = getPaddingTop();

        for (int i = 0, j = 0; i < mChildCountNeedLayout; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();

            ZLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int leftMargin = lp.leftMargin;
            final int rightMargin = lp.rightMargin;
            final int topMargin = lp.topMargin;

            if (i != 0 && startX + leftMargin + childWidth + rightMargin > contentWidth) {
                startX = getPaddingLeft();
                startY += mMaxHeightInEachLine.get(j++) + mLineSpacing;
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

    protected static int getMeasuredWidthWithMargins(View v, final boolean ignoreLeftMargin) {
        ZLayout.LayoutParams lp = (LayoutParams) v.getLayoutParams();
        return v.getMeasuredWidth() + (ignoreLeftMargin ? 0 : lp.leftMargin) + lp.rightMargin;
    }

    protected static int getMeasuredWidthWithMargins(View v) {
        return getMeasuredWidthWithMargins(v, false);
    }

    protected static int getMeasuredHeightWithMargins(View v) {
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

    @Override
    public void addView(View child) {

        if (child instanceof TextView) {
            ((TextView) child).setSingleLine(true);
            ((TextView) child).setMaxLines(1);
            ((TextView) child).setEllipsize(TextUtils.TruncateAt.END);
        }

        super.addView(child);
    }


    public static class LayoutParams extends MarginLayoutParams {

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
