package com.mmlovesyy.zlayout;

import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

public class AutoScrollHandler {

    public static final int TEST_LIST_ITEM_COUNT = 300;

    public static final int TEXT_SIZE_DP = 25;

    public static final int AUTO_SCROLL_INTERVAL = 1;

    public static final int AUTO_SCROLL_STEP = 10;

    private ListView listView;

    private int itemCount;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public AutoScrollHandler(ListView listView, int itemCount) {
        this.listView = listView;
        this.itemCount = itemCount;
    }

    public void startAutoScrollDown() {
        final Timer timer = new Timer();
//        FpsCalculator.instance().startCalculate();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (listView.getLastVisiblePosition() >= itemCount - 1) {
//                    final int avgFps = FpsCalculator.instance().stopGetAvgFPS();
//                    uiHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(listView.getContext(), "Average FPS: " + avgFps, Toast.LENGTH_LONG).show();
//                        }
//                    });
                    timer.cancel();

                    startAutoScrollUp();
                }
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.smoothScrollByOffset(AUTO_SCROLL_STEP);
                    }
                });
            }
        }, 0, AUTO_SCROLL_INTERVAL);

    }

    public void startAutoScrollUp() {
        final Timer timer = new Timer();
//        FpsCalculator.instance().startCalculate();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (listView.getFirstVisiblePosition() <= 1) {
//                    final int avgFps = FpsCalculator.instance().stopGetAvgFPS();
//                    uiHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(listView.getContext(), "Average FPS: " + avgFps, Toast.LENGTH_LONG).show();
//                        }
//                    });
                    timer.cancel();
                }
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.smoothScrollByOffset(-AUTO_SCROLL_STEP);
                    }
                });
            }
        }, 0, AUTO_SCROLL_INTERVAL);
    }
}
