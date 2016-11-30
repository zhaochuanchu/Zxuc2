package com.example.zcc.zxuc;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.zcc.zxuc.FxService.clickBoolean;
import static com.example.zcc.zxuc.FxService.dbWrite;
import static com.example.zcc.zxuc.FxService.getValue;
import static com.example.zcc.zxuc.FxService.getValuebyid;
import static com.example.zcc.zxuc.FxService.handler;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.petView;
import static com.example.zcc.zxuc.FxService.qiPaoView;
import static com.example.zcc.zxuc.FxService.screenWidth;
import static com.example.zcc.zxuc.FxService.status;
import static com.example.zcc.zxuc.FxService.statusBarHeight;
import static com.example.zcc.zxuc.FxService.studyRunnable;
import static com.example.zcc.zxuc.FxService.testView;
import static com.example.zcc.zxuc.PetView.petImage;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ToolsMethod.alphaAnimation;
import static com.example.zcc.zxuc.ToolsMethod.setParams;

/**
 * Created by zcc on 2016/11/12.
 */

public class StudyView {
    private ImageView returnImage;
    private ImageView closeImage;
    private ImageView bgImage;
    private ImageView smallStudyImage;
    private ProgressBar studyProgressbar;

    private TextView text;
    private TextView textView30min;
    private TextView textView60min;
    private TextView stopStudy;

    private WindowManager.LayoutParams sParams;
    private LinearLayout studyFloatLayout;
    private int clickX,clickY;

    public Timer studyTimer;
    public int studyTime;

    public StudyView(){
        sParams=new WindowManager.LayoutParams();
        setParams(sParams,0,0);
        studyFloatLayout=(LinearLayout)inflater.inflate(R.layout.study_layout,null);

        studyTime=0;

        bgImage=(ImageView)studyFloatLayout.findViewById(R.id.bgStudy);
        bgImage.setImageResource(R.drawable.bg);

        returnImage=(ImageView) studyFloatLayout.findViewById(R.id.returnImage);
        returnImage.setImageResource(R.drawable.returnpicture);

        closeImage=(ImageView) studyFloatLayout.findViewById(R.id.closeImage);
        closeImage.setImageResource(R.drawable.closepicture);

        smallStudyImage=(ImageView)studyFloatLayout.findViewById(R.id.smallstudy);
        smallStudyImage.setImageResource(R.drawable.smallstudy);

        studyProgressbar=(ProgressBar)studyFloatLayout.findViewById(R.id.studyProgressbar);
        studyProgressbar.setProgress(0);

        text=(TextView)studyFloatLayout.findViewById(R.id.Text);
        textView30min=(TextView)studyFloatLayout.findViewById(R.id.textView30min);
        textView60min=(TextView)studyFloatLayout.findViewById(R.id.textView60min);
        stopStudy=(TextView)studyFloatLayout.findViewById(R.id.stopStudy);

        returnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//返回
                removeStudyView();
                //menuView.drawMenuView();
                testView.drawTestView();

            }
        });
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeStudyView();
                clickBoolean=false;
            }
        });


        studyFloatLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {//拖动字典界面
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        clickX=(int)event.getX();
                        clickY=(int)event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        sParams.x=(int)(event.getRawX()-clickX);
                        sParams.y=(int)(event.getRawY()-clickY- statusBarHeight);//网上的算法烂的跟屎一样 还tm写博客
                        break;
                }
                mWindowMangager.updateViewLayout(studyFloatLayout,sParams); //刷新悬浮窗的显示
                return false;
            }
        });

        textView30min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=1;//代表正在学习
                studyTime=30;
                studyTimer=new Timer(true);
                TimerTask studyTask=new TimerTask() {
                    @Override
                    public void run() {
                        studyProgressbar.incrementProgressBy(1);
                        if(studyProgressbar.getProgress()>=100){
                            Log.i("studyView","执行学习完成的代码");
                            handler.post(studyRunnable);

                            studyTimer.cancel();
                            studyProgressbar.setProgress(0);
                            status=0;//代表空闲了
                        }

                    }
                };
                studyTimer.schedule(studyTask,0,18000);//30分钟 bar长到100 每次增加百分之一

                removeStudyView();
                testView.drawTestView();

            }
        });

        textView60min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=1;//代表正在学习
                studyTime=60;
                studyTimer=new Timer(true);
                TimerTask studyTask=new TimerTask() {
                    @Override
                    public void run() {
                        studyProgressbar.incrementProgressBy(1);
                        if(studyProgressbar.getProgress()>=100){
                            Log.i("studyView","执行学习完成的代码");
                            handler.post(studyRunnable);

                            studyTimer.cancel();
                            studyProgressbar.setProgress(0);
                            status=0;//代表空闲了
                        }
                    }
                };
                studyTimer.schedule(studyTask,0,36000);//60分钟 bar长到100 每次增加百分之一

                removeStudyView();
                testView.drawTestView();

            }
        });

        stopStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status=0;//代表空闲了
                studyTimer.cancel();
                studyProgressbar.setProgress(0);//进度条归零

                removeStudyView();
                testView.drawTestView();
            }
        });




    }

    public void drawStudyView(){
        sParams.x = (int) (wmParams.x + petImage.getWidth() + 10);
        sParams.y = (int) (wmParams.y - 80);
        if (sParams.x > screenWidth - bgImage.getWidth()) {
            sParams.x = (int) (wmParams.x + petImage.getWidth() - bgImage.getWidth() - 190);
            sParams.y = (int) (wmParams.y - 80);
        }


        if(status==1){//1代表正在学习
            text.setText("正在学习. . .");
            textView30min.setVisibility(View.GONE);
            textView60min.setVisibility(View.GONE);
            studyProgressbar.setVisibility(View.VISIBLE);
            stopStudy.setVisibility(View.VISIBLE);
        }
        else if(status==0){//0代表空闲
            text.setText("请选择学习的时间");
            textView30min.setVisibility(View.VISIBLE);
            textView60min.setVisibility(View.VISIBLE);
            studyProgressbar.setVisibility(View.GONE);
            stopStudy.setVisibility(View.GONE);
        }


        mWindowMangager.addView(studyFloatLayout,sParams);
        bgImage.startAnimation(alphaAnimation(1));
    }

    public void removeStudyView(){
        mWindowMangager.removeView(studyFloatLayout);
    }


}
