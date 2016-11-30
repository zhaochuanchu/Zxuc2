package com.example.zcc.zxuc;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.zcc.zxuc.FxService.clickBoolean;
import static com.example.zcc.zxuc.FxService.dbWrite;
import static com.example.zcc.zxuc.FxService.getValue;
import static com.example.zcc.zxuc.FxService.getValuebyid;
import static com.example.zcc.zxuc.FxService.handler;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.menuView;
import static com.example.zcc.zxuc.FxService.natureView;
import static com.example.zcc.zxuc.FxService.runnableSlowLoad;
import static com.example.zcc.zxuc.FxService.screenWidth;
import static com.example.zcc.zxuc.FxService.status;
import static com.example.zcc.zxuc.FxService.statusBarHeight;
import static com.example.zcc.zxuc.FxService.testView;
import static com.example.zcc.zxuc.MenuView.mParams;
import static com.example.zcc.zxuc.MenuView.menuFloatLayout;
import static com.example.zcc.zxuc.PetView.petImage;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ToolsMethod.alphaAnimation;
import static com.example.zcc.zxuc.ToolsMethod.getDp;
import static com.example.zcc.zxuc.ToolsMethod.setParams;

public class NatureView {

    public static LinearLayout natureFloatLayout;
    public static WindowManager.LayoutParams nParams;

    private TextView petGrowNumber;//宠物生长值
    public  ProgressBar growNumberProgressBar;

    private TextView petIntelligence;//宠物智力
    private ProgressBar intelligenceProgressBar;

    private TextView petForce;//宠物强壮
    private ProgressBar forceProgressBar;

    private TextView petMood;//宠物心情
    private ProgressBar moodProgressBar;

    private TextView petHealth;//宠物健康值
    private ProgressBar healthProgressBar;

    private TextView petMoney;//宠物钱

    private TextView rankTextView;
    //private TextView petRank;//宠物等级
    private LinearLayout rankLinearLayout;//显示宠物等级

    private TextView petName;//宠物名字
    private TextView petTitle;//宠物称号
    private TextView petTitle2;//宠物称号2
    private TextView petTitle3;//宠物称号3


    private TextView petStatus;//宠物状态

    private ImageView bgImageView;//背景图片
    private ImageView exitImage;

    private ImageView returnImage;

    public static boolean slowLoading;

    public boolean displayBoolean;
    private int clickX,clickY;

    public NatureView(){
        nParams=new WindowManager.LayoutParams();
        setParams(nParams,0,0);
        natureFloatLayout=(LinearLayout)inflater.inflate(R.layout.nature_layout,null);

        growNumberProgressBar=(ProgressBar)natureFloatLayout.findViewById(R.id.growNumberProgressBar);
        growNumberProgressBar.setMax(getValue("rank")*20);

        intelligenceProgressBar=(ProgressBar)natureFloatLayout.findViewById(R.id.intelligenceProgressBar);
        forceProgressBar=(ProgressBar)natureFloatLayout.findViewById(R.id.forceProgressBar);
        moodProgressBar=(ProgressBar)natureFloatLayout.findViewById(R.id.moodProgressBar);
        healthProgressBar=(ProgressBar)natureFloatLayout.findViewById(R.id.healthProgressBar);

        //petRank=(TextView)natureFloatLayout.findViewById(R.id.petRank);
        petGrowNumber=(TextView)natureFloatLayout.findViewById(R.id.petGrowNumber);
        petIntelligence=(TextView)natureFloatLayout.findViewById(R.id.petIntelligence);
        petForce=(TextView)natureFloatLayout.findViewById(R.id.petForce);
        petHealth=(TextView)natureFloatLayout.findViewById(R.id.petHealth);
        petMood=(TextView)natureFloatLayout.findViewById(R.id.petMood);
        petMoney=(TextView)natureFloatLayout.findViewById(R.id.petMoney);
        petName=(TextView)natureFloatLayout.findViewById(R.id.petName);
        petName.setText(getValuebyid(9)+getValuebyid(11));
        petStatus=(TextView)natureFloatLayout.findViewById(R.id.petStatus);
        petStatus.setText("无");

        petTitle=(TextView)natureFloatLayout.findViewById(R.id.petTitle);
        petTitle.setText("");
        petTitle2=(TextView)natureFloatLayout.findViewById(R.id.petTitle2);
        petTitle3=(TextView)natureFloatLayout.findViewById(R.id.petTitle3);

        bgImageView=(ImageView)natureFloatLayout.findViewById(R.id.bgImageView);
        bgImageView.setImageResource(R.drawable.bg);
        slowLoading=false;

        exitImage=(ImageView)natureFloatLayout.findViewById(R.id.exitImage);
        exitImage.setImageResource(R.drawable.closepicture);
        exitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!slowLoading) {
                    removeNatureView();
                    clickBoolean=false;
                    //menuView.drawMenuView();
                }
            }
        });

        returnImage=(ImageView) natureFloatLayout.findViewById(R.id.returnImage);
        returnImage.setImageResource(R.drawable.returnpicture);
        returnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//返回
                removeNatureView();
                testView.drawTestView();

            }
        });

        natureFloatLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        clickX=(int)event.getX();
                        clickY=(int)event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        nParams.x=(int)(event.getRawX()-clickX);
                        nParams.y=(int)(event.getRawY()-clickY- statusBarHeight);//网上的算法烂的跟屎一样 还tm写博客
                        break;
                }
                mWindowMangager.updateViewLayout(natureFloatLayout,nParams); //刷新悬浮窗的显示
                return false;
            }
        });

        displayBoolean=false;

        updataText();
        updataProgressBar();

        //jiacuTextView();//加粗字体

        natureFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


    }//构造函数 创建Fxservice的时候会创建这个natureView对象 在此初始化所有的数据

    public void drawNatureView(){

        updataText();
        updataProgressBar();
        /*nParams.x = (int) (wmParams.x + petImage.getWidth() - 70);
        nParams.y = (int) (wmParams.y - 550);*/

        nParams.x = (int) (wmParams.x + petImage.getWidth() +10);
        nParams.y = (int) (wmParams.y-80);
        if(nParams.x>screenWidth-bgImageView.getWidth()){
            nParams.x = (int) (wmParams.x + petImage.getWidth() - bgImageView.getWidth()-190);
            nParams.y = (int) (wmParams.y-80);
        }
        drawRank();

        mWindowMangager.addView(natureFloatLayout, nParams);
        bgImageView.startAnimation(alphaAnimation(1));
        displayBoolean=true;
        slowLoad();



    }//绘制NatureView

    public void removeNatureView(){
        mWindowMangager.removeView(natureFloatLayout);
    }//移除NatureVIew

    private void jiacuTextView(){
        TextPaint textPaint1=rankTextView.getPaint();
        textPaint1.setFakeBoldText(true);
        //TextPaint textPaint2=petRank.getPaint();
        //textPaint2.setFakeBoldText(true);
    }//加粗textView

    public void updataText(){
        if(getValue("money")>=10000){
            petTitle.setText("[富]");
        }
        if(getValue("intelligence")>=100){
            petTitle2.setText("[智]");
        }
        if(getValue("intelligence")>=100){
            petTitle3.setText("[勇]");
        }


        petGrowNumber.setText(String.valueOf(getValue("growNumber"))+"/"+String.valueOf(getValue("rank")*20));
        petForce.setText(String.valueOf(getValue("force")));
        petHealth.setText(""+String.valueOf(getValue("health"))+"/100");
        petIntelligence.setText(String.valueOf(getValue("intelligence")));
        petMood.setText(""+String.valueOf(getValue("mood"))+"/10");
        petMoney.setText(String.valueOf(getValue("money")));
        //petRank.setText(String.valueOf("   "+getValue("rank")));


        switch(status){
            case 0:petStatus.setText("无聊中");break;
            case 1:petStatus.setText("正在学习...");break;
            case 2:petStatus.setText("正在打工...");break;
            case 3:petStatus.setText("正在探险...");break;
        }

    }//更新所有的text数值

    public void updataProgressBar(){

        growNumberProgressBar.setProgress(getValue("growNumber"));
        growNumberProgressBar.setMax(getValue("rank")*20);

        intelligenceProgressBar.setProgress(getValue("intelligence"));
        intelligenceProgressBar.setMax(100);

        forceProgressBar.setProgress(getValue("force"));
        forceProgressBar.setMax(100);

        moodProgressBar.setProgress(getValue("mood"));
        moodProgressBar.setMax(10);

        healthProgressBar.setProgress(getValue("health"));
        healthProgressBar.setMax(100);

    }//更新所有的progressBar

    public void updataGrow(){
        petGrowNumber.setText(String.valueOf(getValue("growNumber"))+"/"+String.valueOf(getValue("rank")*20));
        growNumberProgressBar.setProgress(getValue("growNumber"));
        growNumberProgressBar.setMax(getValue("rank")*20);
    }//宠物生长的时候需要更新的数据

    public void slowLoad(){

        slowLoading=true;
        growNumberProgressBar.setProgress(0);
        intelligenceProgressBar.setProgress(0);
        forceProgressBar.setProgress(0);
        moodProgressBar.setProgress(0);
        healthProgressBar.setProgress(0);//执行动画前 所有bar归零

        growNumberProgressBar.setMax(10000);
        intelligenceProgressBar.setMax(10000);
        forceProgressBar.setMax(10000);
        moodProgressBar.setMax(10000);
        healthProgressBar.setMax(10000);

        final Timer timer=new Timer(true);
        TimerTask slowLoadTask=new TimerTask() {
            boolean complete=false;
            boolean bool1=false;
            boolean bool2=false;
            boolean bool3=false;
            boolean bool4=false;
            boolean bool5=false;

            @Override
            public void run() {//要保持同步到达！
                if (growNumberProgressBar.getProgress() < getValue("growNumber")*10000/(getValue("rank")*20)) {
                    growNumberProgressBar.incrementProgressBy(getValue("growNumber")*100/(getValue("rank")*20)*10);//每次长百分之一
                }else bool1=true;
                if (intelligenceProgressBar.getProgress() < getValue("intelligence")*10000/100) {
                    intelligenceProgressBar.incrementProgressBy( getValue("intelligence")*10);
                }else bool2=true;
                if (forceProgressBar.getProgress() < getValue("force")*10000/100) {
                    forceProgressBar.incrementProgressBy( getValue("force")*10);
                }else bool3=true;
                if (moodProgressBar.getProgress() < getValue("mood")*10000/10) {
                    moodProgressBar.incrementProgressBy(getValue("mood")*10*10);
                }else bool4=true;
                if (healthProgressBar.getProgress() < getValue("health")*10000/100) {
                    healthProgressBar.incrementProgressBy(getValue("health")*10);
                }else bool5=true;

                handler.post(runnableSlowLoad);
                if(bool1&&bool2&&bool3&&bool4&&bool5){
                    timer.cancel();
                    updataProgressBar();
                    slowLoading=false;
                }
            }
        };
        timer.schedule(slowLoadTask,300,40);

    }//用来实现打开状态栏时进度条动态变长 还需提高流畅性(通过背景渐进 可以提高视觉上的流畅度）

    public void drawRank(){
        int rank=getValue("rank");


        int bone4=rank/27;
        int bone3=(rank-bone4*27)/9;
        int bone2=(rank-bone4*27-bone3*9)/3;
        int bone1=(rank-bone4*27-bone3*9-bone2*3);

        ImageView imageView;
        LinearLayout.LayoutParams imageViewLayoutParams;
        rankLinearLayout=(LinearLayout)natureFloatLayout.findViewById(R.id.petRankLayout);
        rankLinearLayout.removeAllViews();

        for(int i=0;i<bone4;i++){
            imageView=new ImageView(MyApplication.getContext());
            imageView.setImageResource(R.drawable.bone4);

            rankLinearLayout.addView(imageView);

            imageViewLayoutParams=(LinearLayout.LayoutParams)imageView.getLayoutParams();
            imageViewLayoutParams.width=getDp(12);
            imageViewLayoutParams.height=getDp(17);
        }
        for(int i=0;i<bone3;i++){
            imageView=new ImageView(MyApplication.getContext());
            imageView.setImageResource(R.drawable.bone3);

            rankLinearLayout.addView(imageView);

            imageViewLayoutParams=(LinearLayout.LayoutParams)imageView.getLayoutParams();
            imageViewLayoutParams.width=getDp(12);
            imageViewLayoutParams.height=getDp(17);


        }
        for(int i=0;i<bone2;i++){
            imageView=new ImageView(MyApplication.getContext());
            imageView.setImageResource(R.drawable.bone2);

            rankLinearLayout.addView(imageView);


            imageViewLayoutParams=(LinearLayout.LayoutParams)imageView.getLayoutParams();
            imageViewLayoutParams.width=getDp(12);
            imageViewLayoutParams.height=getDp(17);
        }
        for(int i=0;i<bone1;i++){
            imageView=new ImageView(MyApplication.getContext());
            imageView.setImageResource(R.drawable.bone1);

            rankLinearLayout.addView(imageView);

            imageViewLayoutParams=(LinearLayout.LayoutParams)imageView.getLayoutParams();
            imageViewLayoutParams.width=getDp(12);
            imageViewLayoutParams.height=getDp(17);
        }

    }//用骨头显示宠物的等级 满级80



}
