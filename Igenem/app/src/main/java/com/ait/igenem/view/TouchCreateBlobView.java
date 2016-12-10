package com.ait.igenem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static android.R.attr.enabled;

/**
 * Created by chk on 12/10/16.
 */

public class TouchCreateBlobView extends View {
    public TouchCreateBlobView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /*
    private class GrowBlobThread extends Thread {
        public void run() {
            while (enabled) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //tvData.append("#");
                    }
                });

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    */
}
