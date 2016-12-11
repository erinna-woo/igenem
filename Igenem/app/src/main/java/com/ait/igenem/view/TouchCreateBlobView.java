package com.ait.igenem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.ait.igenem.model.Blob;

import java.util.List;

/**
 * Created by chk on 12/10/16.
 */

public class TouchCreateBlobView extends RelativeLayout {

    private Blob currBlob;
    private List<Blob> blobList;
    private int posX;
    private int posY;

    public TouchCreateBlobView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

    }




//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int w = MeasureSpec.getSize(widthMeasureSpec);
//        int h = MeasureSpec.getSize(heightMeasureSpec);
//        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
//        setMeasuredDimension(d, d);
//    }


        @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);

        //add new blobView
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("TAG", "touched down: (" + x + ", " + y + ")");

                break;
            case MotionEvent.ACTION_UP:
                Log.i("TAG", "touched up");
                break;
        }

        return true;
    }
}
