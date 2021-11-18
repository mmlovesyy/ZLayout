package com.mmlovesyy.zlayout;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class PerfContrastActivity extends AppCompatActivity {

    private static final String TAG = "PerfContrastActivity";

    private static final int ITEM_COUNT = 200;
    private static final int IMG_COUNT_IN_EACH_ITEM = 30;
    private boolean applyZLayout = false;

    private Button switchBtn;

    private AutoScrollHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perf_contrast);

        switchBtn = (Button) findViewById(R.id.btn_switch);
        switchBtn.setText(applyZLayout ? "ZLayout" : "LinearLayout");

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyZLayout = !applyZLayout;
                switchBtn.setText(applyZLayout ? "ZLayout" : "LinearLayout");
            }
        });

        ListView listView = (ListView) findViewById(R.id.listview);
        Adapter adapter = new Adapter();
        listView.setAdapter(adapter);

        handler = new AutoScrollHandler(listView, ITEM_COUNT);

        findViewById(R.id.btn_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.startAutoScrollDown();
            }
        });


    }

    private class Adapter extends BaseAdapter {

        private final ArrayList<Integer> data = new ArrayList<>(ITEM_COUNT);

        @Override
        public int getCount() {
            return ITEM_COUNT;
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (applyZLayout) {

                if (convertView == null || convertView instanceof ZLayout == false) {
                    convertView = buildZLayout(parent);
                }

                Log.d(TAG, "ZLAYOUT...");

            } else {

                if (convertView == null || convertView instanceof LinearLayout == false) {
                    convertView = buildViews(IMG_COUNT_IN_EACH_ITEM, parent.getContext());
                }

                Log.d(TAG, "LinearLayout...");
            }

            return convertView;
        }
    }

    private View buildZLayout(View parent) {
        View convertView = new ZLayout(parent.getContext());

        for (int i = 0; i < IMG_COUNT_IN_EACH_ITEM; i++) {

            ImageView avatar = new ImageView(parent.getContext());
            avatar.setImageDrawable(parent.getContext().getResources().getDrawable(R.mipmap.ic_launcher));

            ViewGroup.MarginLayoutParams lp = new ZLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ((ViewGroup) convertView).addView(avatar, lp);
        }

        return convertView;
    }

    private View buildViews(int size, Context context) {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int validWidth = ViewUtils.getScreenWidthPixels(this) - ViewUtils.dip2px(context, 32);
        int avatarWidth = 144;
        int avatarNumEachLine = validWidth / avatarWidth;

        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < size; i++) {

            if (i != 0 && i % avatarNumEachLine == 0) {

                layout.addView(l);
                l = new LinearLayout(this);
                l.setOrientation(LinearLayout.HORIZONTAL);
            }

            ImageView avatar = new ImageView(this);
            avatar.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(avatarWidth, avatarWidth);
            l.addView(avatar, lp);
        }

        layout.addView(l);

        return layout;
    }
}
