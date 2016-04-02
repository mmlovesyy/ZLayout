package zlayout.mmlovesyy.com.zlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ZLayoutMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zlayout_main);

        Button btn = (Button) findViewById(R.id.add_img);

        final ZLayout zLayout = (ZLayout) findViewById(R.id.z);
        FrameLayout.MarginLayoutParams lp = (FrameLayout.MarginLayoutParams) zLayout.getLayoutParams();
        lp.leftMargin = 0;
        zLayout.setLayoutParams(lp);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = new ImageView(getApplicationContext());
                img.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                zLayout.addView(img);
            }
        });

    }
}
