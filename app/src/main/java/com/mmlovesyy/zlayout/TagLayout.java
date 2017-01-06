package com.mmlovesyy.zlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.mmlovesyy.zlayout.TagTextView.OnCheckedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmm on 17/1/6.
 */

public class TagLayout extends ZLayout implements OnCheckedListener {

    private CHECKED_TYPE mCheckedType = CHECKED_TYPE.SINGLE;

    private List<Checkable> mCheckedViews = new ArrayList<>(1);

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagLayout, 0, 0);
        try {
            mCheckedType = ta.getInt(R.styleable.TagLayout_checkedType, 0) == 0 ? CHECKED_TYPE.SINGLE : CHECKED_TYPE.MULTIPLE;
        } finally {
            ta.recycle();
        }
    }

    @Override
    public void onChecked(Checkable view) {

        if (mCheckedType == CHECKED_TYPE.SINGLE) {

            if (!mCheckedViews.contains(view)) {
                for (Checkable c : mCheckedViews) {
                    c.setChecked(!c.isChecked());
                }

                mCheckedViews.clear();
                mCheckedViews.add(view);
            }

        } else if (mCheckedType == CHECKED_TYPE.MULTIPLE) {

            if (view.isChecked()) {

                if (!mCheckedViews.contains(view)) {
                    mCheckedViews.add(view);
                }

            } else {

                if (mCheckedViews.contains(view)) {
                    int index = mCheckedViews.indexOf(view);
                    mCheckedViews.remove(index);
                }
            }
        }

    }

    public void setCheckedType(CHECKED_TYPE type) {
        this.mCheckedType = type;
    }

    public String value() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < mCheckedViews.size(); i++) {

            Checkable view = mCheckedViews.get(i);

            if (view instanceof IValue) {
                builder.append(((IValue) view).value() + "  ");
            }
        }

        return builder.toString();
    }

    private enum CHECKED_TYPE {
        SINGLE,
        MULTIPLE
    }
}
