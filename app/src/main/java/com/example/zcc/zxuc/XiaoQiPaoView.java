package com.example.zcc.zxuc;

import android.graphics.Color;
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
import static com.example.zcc.zxuc.FxService.getValuebyid;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.screenWidth;
import static com.example.zcc.zxuc.PetView.petImage;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ToolsMethod.alphaAnimation;
import static com.example.zcc.zxuc.ToolsMethod.setParams;


public class XiaoQiPaoView {

    private ImageView xiaoQiPaoImage;
    private TextView textView2;
    private WindowManager.LayoutParams xParams;//对应小气泡的Params
    private LinearLayout xiaoQiPaoFloatLayout;//小气泡布局
    private Timer actionTimer;//计时器 用来在局部控制动画

    public XiaoQiPaoView(){
        xParams=new WindowManager.LayoutParams();
        setParams(xParams,0,0);
        xiaoQiPaoFloatLayout=(LinearLayout)inflater.inflate(R.layout.xiaoqipao_layout,null);
        xiaoQiPaoImage=(ImageView)xiaoQiPaoFloatLayout.findViewById(R.id.xiaoqipao);
        xiaoQiPaoImage.setImageResource(R.drawable.xiaoqipao1);
        textView2=(TextView)xiaoQiPaoFloatLayout.findViewById(R.id.textView2);
        xiaoQiPaoFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }//加载小气泡

    public void drawXiaoQiPao(){
        xParams.x = (int) (wmParams.x + petImage.getWidth() - 30);
        xParams.y = (int) (wmParams.y);
        if(xParams.x>screenWidth-xiaoQiPaoImage.getWidth()){
            xParams.x = (int) (wmParams.x + petImage.getWidth() - xiaoQiPaoImage.getWidth()-140);
            xParams.y = (int) (wmParams.y);
        }

        mWindowMangager.addView(xiaoQiPaoFloatLayout, xParams);
        /*AnimationDrawable anim = (AnimationDrawable) xiaoQiPaoImage.getDrawable();
        anim.start();*/
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mWindowMangager.removeView(xiaoQiPaoFloatLayout);
                clickBoolean=false;
            }
        };
        xiaoQiPaoImage.startAnimation(alphaAnimation(1));
        actionTimer=new Timer(true);
        actionTimer.schedule(task, 3000);
        mWindowMangager.updateViewLayout(xiaoQiPaoFloatLayout, xParams);
        textView2.setText(getValuebyid(10)+","+getStatement(new Random().nextInt(9)));//应该释放吗
    }//画小气泡

}
