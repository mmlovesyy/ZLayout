package com.mmlovesyy.zlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

/**
 * Created by cmm on 17/1/5.
 */

public final class TagTextView extends TextView implements Checkable, IValue {

    private OnCheckedListener mOnCheckedListener;

    private boolean mChecked = false;

    private static final int[] CHECKED_STATE_SET = {
            R.attr.checked
    };

    public TagTextView(Context context) {
        super(context);
    }

    public TagTextView(Context context, OnCheckedListener listener) {
        super(context);

        mOnCheckedListener = listener;
    }

    public TagTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagTextView, 0, 0);
        try {
            mChecked = ta.getBoolean(R.styleable.TagTextView_checked, false);
        } finally {
            ta.recycle();
        }
    }

    public TagTextView(Context context, AttributeSet attrs, OnCheckedListener listener) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagTextView, 0, 0);
        try {
            mChecked = ta.getBoolean(R.styleable.TagTextView_checked, false);
        } finally {
            ta.recycle();
        }

        mOnCheckedListener = listener;
    }

    public void setOnCheckedListener(OnCheckedListener listener) {
        mOnCheckedListener = listener;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {

        if (mChecked) {
            final int[] states = super.onCreateDrawableState(extraSpace + 1);
            mergeDrawableStates(states, CHECKED_STATE_SET);
            return states;

        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    public void setChecked(boolean checked, boolean notify) {

        if (checked != mChecked) {
            mChecked = checked;
            refreshDrawableState();

            if (notify && mOnCheckedListener != null) {
                mOnCheckedListener.onChecked(this);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked, false);
    }

    @Override
    public String value() {
        return getText().toString();
    }

    public interface OnCheckedListener {
        void onChecked(Checkable view);
    }
}
