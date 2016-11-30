package com.example.zcc.zxuc;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.zcc.zxuc.FxService.clickBoolean;
import static com.example.zcc.zxuc.FxService.getStatement;
import static com.example.zcc.zxuc.FxService.getValue;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.screenHight;
import static com.example.zcc.zxuc.FxService.screenWidth;
import static com.example.zcc.zxuc.PetView.petImage;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ToolsMethod.alphaAnimation;
import static com.example.zcc.zxuc.ToolsMethod.setParams;

public class QiPaoView {

    private ImageView qipaoImage;
    public TextView textView1;
    private LinearLayout qipaoFloatLayout;//气泡布局
    private WindowManager.LayoutParams sParams;//对应气泡的Params
    private Timer actionTimer;//计时器 用来在局部控制动画

    public QiPaoView(){
        sParams=new WindowManager.LayoutParams();
        setParams(sParams,0,0);
        qipaoFloatLayout=(LinearLayout)inflater.inflate(R.layout.status_layout,null);
        qipaoImage=(ImageView)qipaoFloatLayout.findViewById(R.id.qipao);//获取这个对象
        qipaoImage.setImageResource(R.drawable.newerdb);
        textView1=(TextView)qipaoFloatLayout.findViewById(R.id.textView);
        qipaoFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }//加载大气泡

    public void drawQiPao(){

        /*sParams.x = (int) (wmParams.x + petImage.getWidth() - 70);
        sParams.y = (int) (wmParams.y - 550);*/

        sParams.x = (int) (wmParams.x + petImage.getWidth() - 30);
        sParams.y = (int) (wmParams.y);
        if(sParams.x>screenWidth-qipaoImage.getWidth()){
            sParams.x = (int) (wmParams.x + petImage.getWidth() - qipaoImage.getWidth()-140);
            sParams.y = (int) (wmParams.y);
        }

        mWindowMangager.addView(qipaoFloatLayout, sParams);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mWindowMangager.removeView(qipaoFloatLayout);
            }
        };
        qipaoImage.startAnimation(alphaAnimation(1));
        actionTimer=new Timer(true);
        actionTimer.schedule(task, 3000);
        mWindowMangager.updateViewLayout(qipaoFloatLayout, sParams);

    }//画大气泡

}
