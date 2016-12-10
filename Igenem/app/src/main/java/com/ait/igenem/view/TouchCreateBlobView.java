package com.ait.igenem.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ait.igenem.DecisionActivity;
import com.ait.igenem.PassDataDynamicBlobInterface;
import com.ait.igenem.R;
import com.ait.igenem.model.Blob;
import com.ait.igenem.model.Decision;

import java.util.List;

import static android.R.attr.enabled;

/**
 * Created by chk on 12/10/16.
 */

public class TouchCreateBlobView extends View {

    private Paint paintBlobPro;
    private Paint paintBlobCon;
    private Paint paintText;

    private Blob currBlob;
    private List<Blob> blobList;
    private int posX;
    private int posY;

    public TouchCreateBlobView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintBlobPro = new Paint();
        paintBlobPro.setColor(Color.WHITE);
        paintBlobPro.setStyle(Paint.Style.FILL);

        paintBlobCon = new Paint();
        paintBlobCon.setColor(Color.BLACK);
        paintBlobCon.setStyle(Paint.Style.FILL);

        paintText = new Paint();
        paintText.setColor(Color.RED);
        paintText.setTextSize(50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //currDecision = passDataDynamicBlobInterface.getDecision();
        super.onDraw(canvas);
        blobList = (List<Blob>) getTag(R.string.blob);

        if(blobList != null) {
            for (int i = 0; i < blobList.size(); i++) {
                currBlob = blobList.get(i);
                if(currBlob.isPro()){
                    canvas.drawCircle(currBlob.getPosx(), currBlob.getPosy(),
                            currBlob.getRadius(), paintBlobPro);
                }
                else if(!currBlob.isPro()){
                    canvas.drawCircle(currBlob.getPosx(), currBlob.getPosy(),
                            currBlob.getRadius(), paintBlobCon);

                }
                canvas.drawText(currBlob.getName(), currBlob.getPosx(), currBlob.getPosy(), paintText);
            }
        }

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
