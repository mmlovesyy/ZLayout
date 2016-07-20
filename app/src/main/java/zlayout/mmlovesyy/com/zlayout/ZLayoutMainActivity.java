package zlayout.mmlovesyy.com.zlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ZLayoutMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zlayout_main);

        final ZLayout zLayout = (ZLayout) findViewById(R.id.z);

        final String[] s = new String[]{"xiaonaxiaonaxiaonaxiaonaxiaonaxiaonaxiaonaxiaonaxiaonaxiaonaxiaonaxiaonaxiaona"
                , "linus", "lei", "xueyan.lin"};

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView img = new TextView(getApplicationContext());
                img.setSingleLine(true);
                img.setEllipsize(TextUtils.TruncateAt.END);
                img.setText(s[(int) Math.floor(Math.random() * s.length)]);
                img.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_child));
                img.setPadding(5, 5, 5, 5);
                img.setTextColor(getResources().getColor(R.color.colorPrimary));
                Log.d("ZLayout", img.getText().toString());

//                ImageView img = new ImageView(getApplicationContext());
//                img.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                ViewGroup.MarginLayoutParams lp = new ZLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = 20;
                lp.topMargin = 10;
                lp.rightMargin = 0;
                lp.bottomMargin = 0;
                zLayout.addView(img, lp);
            }
        });

    }
}
