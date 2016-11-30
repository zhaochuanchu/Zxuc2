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

import static com.example.zcc.zxuc.FxService.adventureRunnable;
import static com.example.zcc.zxuc.FxService.clickBoolean;
import static com.example.zcc.zxuc.FxService.handler;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.screenWidth;
import static com.example.zcc.zxuc.FxService.status;
import static com.example.zcc.zxuc.FxService.statusBarHeight;
import static com.example.zcc.zxuc.FxService.testView;
import static com.example.zcc.zxuc.PetView.petImage;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ToolsMethod.alphaAnimation;
import static com.example.zcc.zxuc.ToolsMethod.setParams;

/**
 * Created by zcc on 2016/11/12.
 */

public class AdventureView {
    private ImageView returnImage;
    private ImageView closeImage;
    private ImageView bgImage;
    private ImageView smallAdventureImage;
    private ProgressBar adventureProgressbar;

    private TextView text;
    private TextView senlin;
    private TextView huoshan;
    private TextView haiyang;

    private WindowManager.LayoutParams aParams;
    private LinearLayout adventureFloatLayout;
    private int clickX,clickY;

    public Timer adventureTimer;

    public int adventureType;//1 2 3 代表森林 火山 海洋 0代表没有

    public AdventureView(){
        aParams=new WindowManager.LayoutParams();
        setParams(aParams,0,0);
        adventureFloatLayout=(LinearLayout)inflater.inflate(R.layout.adventure_layout,null);

        adventureType=0;

        bgImage=(ImageView)adventureFloatLayout.findViewById(R.id.bgAdventure);
        bgImage.setImageResource(R.drawable.bg);

        returnImage=(ImageView) adventureFloatLayout.findViewById(R.id.returnImage);
        returnImage.setImageResource(R.drawable.returnpicture);

        closeImage=(ImageView) adventureFloatLayout.findViewById(R.id.closeImage);
        closeImage.setImageResource(R.drawable.closepicture);

        smallAdventureImage=(ImageView)adventureFloatLayout.findViewById(R.id.smalladventure);
        smallAdventureImage.setImageResource(R.drawable.smalladventure);

        adventureProgressbar=(ProgressBar)adventureFloatLayout.findViewById(R.id.adventureProgressbar);
        adventureProgressbar.setProgress(0);

        text=(TextView)adventureFloatLayout.findViewById(R.id.Text);
        senlin=(TextView)adventureFloatLayout.findViewById(R.id.senlinText);
        huoshan=(TextView)adventureFloatLayout.findViewById(R.id.huoshanText);
        haiyang=(TextView)adventureFloatLayout.findViewById(R.id.haiyangText);


        returnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//返回
                removeAdventureView();
                //menuView.drawMenuView();
                testView.drawTestView();

            }
        });
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAdventureView();
                clickBoolean=false;
            }
        });

        adventureFloatLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {//拖动字典界面
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        clickX=(int)event.getX();
                        clickY=(int)event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        aParams.x=(int)(event.getRawX()-clickX);
                        aParams.y=(int)(event.getRawY()-clickY- statusBarHeight);//网上的算法烂的跟屎一样 还tm写博客
                        break;
                }
                mWindowMangager.updateViewLayout(adventureFloatLayout,aParams); //刷新悬浮窗的显示
                return false;
            }
        });

        senlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=3;//代表正在探险
                adventureTimer=new Timer(true);
                TimerTask studyTask=new TimerTask() {
                    @Override
                    public void run() {
                        adventureProgressbar.incrementProgressBy(1);
                        if(adventureProgressbar.getProgress()>=100){
                            Log.i("adventureView","执行探险完成的代码");
                            adventureType=1;
                            handler.post(adventureRunnable);

                            adventureTimer.cancel();
                            adventureProgressbar.setProgress(0);
                            status=0;//代表空闲了
                        }

                    }
                };
                adventureTimer.schedule(studyTask,0,18000);//30分钟 bar长到100 每次增加百分之一

                removeAdventureView();
                testView.drawTestView();

            }
        });

        huoshan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=3;//代表正在探险
                adventureTimer=new Timer(true);
                TimerTask studyTask=new TimerTask() {
                    @Override
                    public void run() {
                        adventureProgressbar.incrementProgressBy(1);
                        if(adventureProgressbar.getProgress()>=100){
                            Log.i("adventureView","执行探险完成的代码");
                            adventureType=2;
                            handler.post(adventureRunnable);

                            adventureTimer.cancel();
                            adventureProgressbar.setProgress(0);
                            status=0;//代表空闲了
                        }

                    }
                };
                adventureTimer.schedule(studyTask,0,18000);//30分钟 bar长到100 每次增加百分之一

                removeAdventureView();
                testView.drawTestView();

            }
        });

        haiyang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=3;//代表正在探险
                adventureTimer=new Timer(true);
                TimerTask studyTask=new TimerTask() {
                    @Override
                    public void run() {
                        adventureProgressbar.incrementProgressBy(1);
                        if(adventureProgressbar.getProgress()>=100){
                            Log.i("adventureView","执行探险完成的代码");
                            adventureType=3;
                            handler.post(adventureRunnable);

                            adventureTimer.cancel();
                            adventureProgressbar.setProgress(0);
                            status=0;//代表空闲了
                        }

                    }
                };
                adventureTimer.schedule(studyTask,0,18000);//30分钟 bar长到100 每次增加百分之一

                removeAdventureView();
                testView.drawTestView();

            }
        });



    }

    public void drawAdventureView(){
        aParams.x = (int) (wmParams.x + petImage.getWidth() + 10);
        aParams.y = (int) (wmParams.y - 80);
        if (aParams.x > screenWidth - bgImage.getWidth()) {
            aParams.x = (int) (wmParams.x + petImage.getWidth() - bgImage.getWidth() - 190);
            aParams.y = (int) (wmParams.y - 80);
        }


        if(status==3){//3代表正在探险
            text.setText("正在探险. . .");
            senlin.setVisibility(View.GONE);
            huoshan.setVisibility(View.GONE);
            haiyang.setVisibility(View.GONE);
            adventureProgressbar.setVisibility(View.VISIBLE);
        }
        else if(status==0){//0代表空闲
            text.setText("请选择探险地点");
            senlin.setVisibility(View.VISIBLE);
            huoshan.setVisibility(View.VISIBLE);
            haiyang.setVisibility(View.VISIBLE);
            adventureProgressbar.setVisibility(View.GONE);
        }

        mWindowMangager.addView(adventureFloatLayout,aParams);
        bgImage.startAnimation(alphaAnimation(1));
    }

    public void removeAdventureView(){
        mWindowMangager.removeView(adventureFloatLayout);
    }

}
