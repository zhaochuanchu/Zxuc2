package com.example.zcc.zxuc;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.zcc.zxuc.FxService.clickBoolean;
import static com.example.zcc.zxuc.FxService.handler;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.screenWidth;
import static com.example.zcc.zxuc.FxService.status;
import static com.example.zcc.zxuc.FxService.statusBarHeight;
import static com.example.zcc.zxuc.FxService.testView;
import static com.example.zcc.zxuc.FxService.workRunnable;
import static com.example.zcc.zxuc.PetView.petImage;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ToolsMethod.alphaAnimation;
import static com.example.zcc.zxuc.ToolsMethod.setParams;

/**
 * Created by zcc on 2016/11/12.
 */

public class WorkView {

    private ImageView returnImage;
    private ImageView closeImage;
    private ImageView bgImage;
    private ImageView smallWorkImage;
    private ProgressBar workProgressbar;

    private TextView text;
    private TextView textView30min;
    private TextView textView60min;
    private TextView stopWork;

    private WindowManager.LayoutParams wParams;
    private LinearLayout workFloatLayout;
    private int clickX,clickY;

    public Timer workTimer;
    public int workTime;

    public WorkView(){

        wParams=new WindowManager.LayoutParams();
        setParams(wParams,0,0);
        workFloatLayout=(LinearLayout)inflater.inflate(R.layout.work_layout,null);

        workTime=0;

        bgImage=(ImageView)workFloatLayout.findViewById(R.id.bgWork);
        bgImage.setImageResource(R.drawable.bg);

        returnImage=(ImageView) workFloatLayout.findViewById(R.id.returnImage);
        returnImage.setImageResource(R.drawable.returnpicture);

        closeImage=(ImageView) workFloatLayout.findViewById(R.id.closeImage);
        closeImage.setImageResource(R.drawable.closepicture);

        smallWorkImage=(ImageView)workFloatLayout.findViewById(R.id.smallwork);
        smallWorkImage.setImageResource(R.drawable.smallwork);

        workProgressbar=(ProgressBar)workFloatLayout.findViewById(R.id.workProgressbar);
        workProgressbar.setProgress(0);

        text=(TextView)workFloatLayout.findViewById(R.id.Text);
        textView30min=(TextView)workFloatLayout.findViewById(R.id.textView30min);
        textView60min=(TextView)workFloatLayout.findViewById(R.id.textView60min);
        stopWork=(TextView)workFloatLayout.findViewById(R.id.stopWork);

        returnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//返回
                removeWorkView();
                //menuView.drawMenuView();
                if(testView!=null)
                testView.drawTestView();

            }
        });
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeWorkView();
                clickBoolean=false;
            }
        });



        workFloatLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {//拖动字典界面
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        clickX=(int)event.getX();
                        clickY=(int)event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        wParams.x=(int)(event.getRawX()-clickX);
                        wParams.y=(int)(event.getRawY()-clickY- statusBarHeight);//网上的算法烂的跟屎一样 还tm写博客
                        break;
                }
                mWindowMangager.updateViewLayout(workFloatLayout,wParams); //刷新悬浮窗的显示
                return false;
            }
        });

        textView30min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=2;//代表正在工作
                workTimer=new Timer(true);
                TimerTask workTask=new TimerTask() {
                    @Override
                    public void run() {
                        workProgressbar.incrementProgressBy(1);
                        if(workProgressbar.getProgress()>=100){
                            Log.i("workView","执行打工完成的代码");
                            workTime=30;
                            handler.post(workRunnable);
                            workTimer.cancel();
                            workProgressbar.setProgress(0);
                            status=0;//代表空闲了
                        }

                    }
                };
                workTimer.schedule(workTask,0,18000);//30分钟 bar长到100 每次增加百分之一

                removeWorkView();
                testView.drawTestView();

            }
        });

        textView60min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=2;//代表正在工作
                workTimer=new Timer(true);
                TimerTask workTask=new TimerTask() {
                    @Override
                    public void run() {
                        workProgressbar.incrementProgressBy(1);
                        if(workProgressbar.getProgress()>=100){
                            Log.i("workView","执行打工完成的代码");
                            workTime=60;
                            handler.post(workRunnable);
                            workTimer.cancel();
                            workProgressbar.setProgress(0);
                            status=0;//代表空闲了
                        }

                    }
                };
                workTimer.schedule(workTask,0,36000);//60分钟 bar长到100 每次增加百分之一

                removeWorkView();
                testView.drawTestView();

            }
        });

        stopWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=0;//代表空闲了
                workTimer.cancel();
                workProgressbar.setProgress(0);//进度条归零
                removeWorkView();
                testView.drawTestView();
            }
        });

    }


    public void drawWorkView(){
        wParams.x = (int) (wmParams.x + petImage.getWidth() + 10);
        wParams.y = (int) (wmParams.y - 80);
        if (wParams.x > screenWidth - bgImage.getWidth()) {
            wParams.x = (int) (wmParams.x + petImage.getWidth() - bgImage.getWidth() - 190);
            wParams.y = (int) (wmParams.y - 80);
        }


        if(status==2){//2代表正在工作
            text.setText("正在打工. . .");
            textView30min.setVisibility(View.GONE);
            textView60min.setVisibility(View.GONE);
            workProgressbar.setVisibility(View.VISIBLE);
            stopWork.setVisibility(View.VISIBLE);
        }
        else if(status==0){//0代表空闲
            text.setText("请选择打工的时间");
            textView30min.setVisibility(View.VISIBLE);
            textView60min.setVisibility(View.VISIBLE);
            workProgressbar.setVisibility(View.GONE);
            stopWork.setVisibility(View.GONE);
        }

        mWindowMangager.addView(workFloatLayout,wParams);
        bgImage.startAnimation(alphaAnimation(1));
    }

    public void removeWorkView(){
        mWindowMangager.removeView(workFloatLayout);
    }



}
