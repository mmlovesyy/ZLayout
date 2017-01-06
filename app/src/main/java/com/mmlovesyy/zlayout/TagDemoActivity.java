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
                "少年，干了这碗鸡汤：",
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

        for (int i = 0; i < tagText.length; i++) {

            String t = tagText[i];
            final TagTextView tagView = new TagTextView(getApplicationContext());

            // layout parameters
            ViewGroup.MarginLayoutParams lp;

            if (i == 0) {

                // title
                tagView.setTextColor(0xff333333);
                tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

                lp = new ZLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            } else {

                tagView.setEllipsize(TextUtils.TruncateAt.END);
                tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                tagView.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_tag));
                tagView.setTextColor(getResources().getColorStateList(R.color.tag_color));
                tagView.setPadding(25, 15, 25, 15);
                tagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagView.setChecked(!tagView.isChecked());
                    }
                });

                lp = new ZLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            }

            tagView.setText(t);
            tagView.setSingleLine(true);

            lp.leftMargin = 20;
            lp.topMargin = 10;
            lp.rightMargin = 0;
            lp.bottomMargin = 0;
            zLayout.addView(tagView, lp);
        }
    }
}
