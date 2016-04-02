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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);

        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int parentWidth = getMeasuredWidth() - getPaddingTop();
        int startX = l + getPaddingLeft();
        int startY = t + getPaddingTop();

        Log.d("ZLayout", "paddingLeft: " + getPaddingLeft());
        Log.d("ZLayout", "paddingRight: " + getPaddingTop());

        for (int i = 0, count = getChildCount(); i < count; i++) {
            View child = getChildAt(i);
//            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();

            if (startX + childWidth > parentWidth) {

                if (i == 0) {
                    Log.d("ZLayout", "no enough space for child view");
                    return;

                } else {
                    startX = l + getPaddingLeft();
                    startY += childHeight;
                }
            }

            child.layout(startX, startY, startX + childWidth, startY + childHeight);

            startX += childWidth;
        }
    }

    public static class MarginLayoutParams extends ViewGroup.MarginLayoutParams {

        public MarginLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public MarginLayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
