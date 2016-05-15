package zlayout.mmlovesyy.com.zlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ZLayoutMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zlayout_main);

        final ZLayout zLayout = (ZLayout) findViewById(R.id.z);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = new ImageView(getApplicationContext());
                img.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                ViewGroup.MarginLayoutParams lp = new ZLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = 200;
                lp.topMargin = 100;
                zLayout.addView(img, lp);
            }
        });

    }
}
