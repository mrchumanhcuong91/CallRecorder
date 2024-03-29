package com.project.android.callrecorder.Other;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
//https://stackoverflow.com/questions/32949971/how-can-i-make-sticky-headers-in-recyclerview-without-external-lib
//https://stackoverflow.com/questions/3972445/how-to-put-text-in-a-drawable
public class DividerItemDecorator extends RecyclerView.ItemDecoration {
    private Drawable mdivider;

    public DividerItemDecorator(Drawable _mdivider){
        mdivider = _mdivider;
    }
    @Override
    public void onDraw(Canvas ca, RecyclerView parent, RecyclerView.State state){
        int dividerLeft = 320;//parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - dividerLeft;

        //start draw each line
        for(int i =0;i < parent.getChildCount()-1; i++){
            View item = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) item.getLayoutParams();
            int dividerTop = item.getBottom() + params.bottomMargin;
            int dividerBotton = dividerTop + mdivider.getIntrinsicHeight();

            mdivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBotton);
            mdivider.draw(ca);
        }
    }
}

