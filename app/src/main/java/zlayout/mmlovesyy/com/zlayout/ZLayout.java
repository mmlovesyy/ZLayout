package zlayout.mmlovesyy.com.zlayout;

import android.content.Context;
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

    public ZLayout(Context context) {
        super(context);
    }

    public ZLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    // note: for simplicity assume children have the same dimension
    private int measureWidth(int widthMeasureSpec) {

        Log.d(TAG, "enter measureWidth(), widthMeasureSpec: " + MeasureSpec.toString(widthMeasureSpec));

        int count = getChildCount();
        int childWidth = count > 0 ? getMeasuredWidthWithMargins(getChildAt(0)) : 0;
        int needWidth = count * childWidth;

        int width = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (widthMode) {

            case MeasureSpec.EXACTLY:
                width = widthSize;

                if (needWidth > 0) {

                    int widthUsed = 0;
                    for (int i = 1; i <= count; i++) {

                        if (widthUsed + childWidth <= width) {
                            widthUsed += childWidth;

                        } else {
                            break;
                        }
                    }

                    mLineCount = (int) Math.ceil(needWidth * 1.0 / widthUsed);

                } else {
                    mLineCount = 0;
                }

                break;

            case MeasureSpec.AT_MOST:
                if (needWidth > 0) {
                    width = Math.min(needWidth, widthSize);

                    int widthUsedPerLine = 0;
                    for (int i = 1; i <= count; i++) {

                        if (widthUsedPerLine + childWidth <= width) {
                            widthUsedPerLine += childWidth;

                        } else {
                            break;
                        }
                    }

                    mLineCount = (int) Math.ceil(needWidth * 1.0 / widthUsedPerLine);

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
        int childHeight = count > 0 ? getMeasuredHeightWithMargins(getChildAt(0)) : 0;

        int height = 0;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (heightMode) {

            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;

            case MeasureSpec.AT_MOST:
                int needHeight = mLineCount * childHeight;
                height = Math.min(needHeight, heightSize);

                break;

            case MeasureSpec.UNSPECIFIED:
                height = mLineCount * childHeight;

                break;
        }

        Log.d(TAG, "after measureHeight(), mHeight: " + height);

        return height;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.d(TAG, "onLayout...");

        int contentWidth = r - l - getPaddingLeft() - getPaddingRight();
        int startX = getPaddingLeft();
        int startY = getPaddingTop();

        for (int i = 0, count = getChildCount(); i < count; i++) {
            View child = getChildAt(i);

            int childHeight = getMeasuredHeightWithMargins(child);
            int childWidth = getMeasuredWidthWithMargins(child);

            if (startX + childWidth > contentWidth) {

                if (i == 0) {
                    Log.d(TAG, "no enough space for the 1st child view");
                    return;
                }

                startX = getPaddingLeft();
                startY += childHeight;
            }

            child.layout(startX, startY, startX + childWidth, startY + childHeight);

            startX += childWidth;
        }
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
