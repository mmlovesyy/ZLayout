package com.mmlovesyy.zlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

public class TagDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_demo);

        final ZLayout zLayout = (ZLayout) findViewById(R.id.tag_demo_z);

        String[] tagText = {
                "凡事",
                "坚持到最后",
                "结果总会是好的",
                "如果不好",
                "说明",
                "还没到最后"
        };

        addTag(zLayout, tagText);
    }

    private void addTag(ZLayout zLayout, String[] tagText) {

        for (String t : tagText) {

            final TagTextView tagview = new TagTextView(getApplicationContext());
            tagview.setSingleLine(true);
            tagview.setEllipsize(TextUtils.TruncateAt.END);
            tagview.setText(t);
            tagview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tagview.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tag));
            tagview.setTextColor(getResources().getColorStateList(R.color.tag_color));
            tagview.setPadding(25, 15, 25, 15);
            tagview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tagview.setChecked(!tagview.isChecked());
                }
            });

            ViewGroup.MarginLayoutParams lp = new ZLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 20;
            lp.topMargin = 10;
            lp.rightMargin = 0;
            lp.bottomMargin = 0;
            zLayout.addView(tagview, lp);
        }
    }
}
