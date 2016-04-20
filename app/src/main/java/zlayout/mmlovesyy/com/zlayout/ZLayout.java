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

        int measureWidth = measure(widthMeasureSpec);
        int measureHeight = measure(heightMeasureSpec);

        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measure(int widthMeasureSpec) {

        int result = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (widthMode) {

            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.d(TAG, "onLayout...");

        int parentWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int startX = l + getPaddingLeft();
        int startY = t + getPaddingTop();

        for (int i = 0, count = getChildCount(); i < count; i++) {
            View child = getChildAt(i);

            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();

            if (startX + childWidth > parentWidth) {

                if (i == 0) {
                    Log.d(TAG, "no enough space for the 1st child view");
                    return;
                }

                startX = l + getPaddingLeft();
                startY += childHeight;
            }

            child.layout(startX, startY, startX + childWidth, startY + childHeight);

            startX += childWidth;
        }
    }
}
