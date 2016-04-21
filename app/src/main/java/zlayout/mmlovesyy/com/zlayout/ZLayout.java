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
        int childWidth = count > 0 ? getChildAt(0).getMeasuredWidth() : 0;
        int needWidthInSingleLine = count * childWidth;

        int width = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (widthMode) {

            case MeasureSpec.EXACTLY:
                width = widthSize;

                if (needWidthInSingleLine > 0) {

                    int widthUsedPerLine = 0;
                    for (int i = 1; i <= count; i++) {

                        if (widthUsedPerLine + childWidth < width) {
                            widthUsedPerLine += childWidth;

                        } else {
                            break;
                        }
                    }

                    mLineCount = (int) Math.ceil(needWidthInSingleLine * 1.0 / widthUsedPerLine);

                } else {
                    mLineCount = 0;
                }

                break;

            case MeasureSpec.AT_MOST:
                if (needWidthInSingleLine > 0) {
                    width = Math.min(needWidthInSingleLine, widthSize);

                    int widthUsedPerLine = 0;
                    for (int i = 1; i <= count; i++) {

                        if (widthUsedPerLine + childWidth < width) {
                            widthUsedPerLine += childWidth;

                        } else {
                            break;
                        }
                    }

                    mLineCount = (int) Math.ceil(needWidthInSingleLine * 1.0 / widthUsedPerLine);

                } else {
                    width = 0;
                    mLineCount = 0;
                }

                break;

            case MeasureSpec.UNSPECIFIED:
                width = needWidthInSingleLine;
                mLineCount = 1;

                break;

        }

        Log.d(TAG, "after measureWidth(), mWidth: " + width + ", mLineCount: " + mLineCount);

        return width;
    }

    private int measureHeight(int heightMeasureSpec) {

        Log.d(TAG, "enter measureHeight(), heightMeasureSpec: " + MeasureSpec.toString(heightMeasureSpec));

        int count = getChildCount();
        int childHeight = count > 0 ? getChildAt(0).getMeasuredHeight() : 0;

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

        Log.d(TAG, "after measureHeight(), mWidth: " + height);

        return height;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.d(TAG, "onLayout...");

        int parentWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int startX = getPaddingLeft();
        int startY = getPaddingTop();

        for (int i = 0, count = getChildCount(); i < count; i++) {
            View child = getChildAt(i);

            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();

            if (startX + childWidth > parentWidth) {

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

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
