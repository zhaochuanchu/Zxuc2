package com.example.zcc.zxuc;

import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.zcc.zxuc.FxService.clickBoolean;
import static com.example.zcc.zxuc.FxService.dictionaryView;
import static com.example.zcc.zxuc.FxService.fallRunnable;
import static com.example.zcc.zxuc.FxService.getValue;
import static com.example.zcc.zxuc.FxService.handler;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.menuView;
import static com.example.zcc.zxuc.FxService.natureView;
import static com.example.zcc.zxuc.FxService.qiPaoView;
import static com.example.zcc.zxuc.FxService.readBitMap;
import static com.example.zcc.zxuc.FxService.runnablePlayBalloon;
import static com.example.zcc.zxuc.FxService.screenHight;
import static com.example.zcc.zxuc.FxService.status;
import static com.example.zcc.zxuc.FxService.statusBarHeight;
import static com.example.zcc.zxuc.FxService.testView;
import static com.example.zcc.zxuc.FxService.xiaoQiPaoView;
import static com.example.zcc.zxuc.ToolsMethod.alphaAnimation;
import static com.example.zcc.zxuc.ToolsMethod.setParams;


public class PetView {

    public static ImageView petImage;
    public static WindowManager.LayoutParams wmParams;//创建浮动窗口设置布局参数的对象
    public static LinearLayout mFloatLayout;//宠物布局
    private int clickX,clickY;
    private AnimationDrawable sleepAnim;
    private AnimationDrawable actionAnim;


    private static RelativeLayout inFloatLayout;

    private ImageView balloonImage;
    private boolean sleepboolean;//判断是否在睡觉
    private boolean playballoonboolean;//判断是否在玩气球
    private boolean exitballoonbooleam;//判断是否存在气球

    public Timer playballoontimer;
    public Timer falltimer;

    private int touchx,touchy;
    private boolean isMove;//判断移动了没


    public PetView(){
        //Toast.makeText(this,"宠物启动",Toast.LENGTH_SHORT).show();
        clickBoolean=false;//没点击宠物

        sleepboolean=true;//宠物刚开始就睡觉
        playballoonboolean=false;//刚开始没玩气球
        exitballoonbooleam=false;//刚开始不存在气球
        isMove=false;

        status=0;//刚开始是空闲

        wmParams=new WindowManager.LayoutParams();
        setParams(wmParams,10,10);

        mFloatLayout=(LinearLayout)inflater.inflate(R.layout.float_layout,null);//获取浮动窗口视图所在布局
        inFloatLayout=(RelativeLayout)mFloatLayout.findViewById(R.id.smallLayout);
        petImage=(ImageView)mFloatLayout.findViewById(R.id.mainPicture);//获取这个对象
        balloonImage = (ImageView) mFloatLayout.findViewById(R.id.ball);

        petImage.setImageResource(R.drawable.newblinknoblink);

        /*smallWidth=petImage.getWidth();
        smallHeight=petImage.getHeight();
        bigWidth=(int)(petImage.getWidth()*1.3);
        bigHeight=(int)(petImage.getHeight()*1.3);*/

        petImage.setImageResource(R.drawable.sleep);
        sleepAnim = (AnimationDrawable) petImage.getDrawable();
        sleepAnim.start();

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        petImage.setOnTouchListener(new View.OnTouchListener() {//设置监听浮动窗口的触摸移动
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        clickX=(int)event.getX();
                        clickY=(int)event.getY();
                        touchx=(int)event.getRawX();
                        touchy=(int)event.getRawY();
                        Log.i("捕捉到down","down");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        wmParams.x=(int)(event.getRawX()-clickX);
                        wmParams.y=(int)(event.getRawY()-clickY- statusBarHeight);//网上的算法烂的跟屎一样 还tm写博客
                        if(exitballoonbooleam){
                            wmParams.y-=138;
                            wmParams.x-=47;
                        }
                        if(((touchx-event.getRawX()<5)&&(touchx-event.getRawX())>-5)&&
                                ((touchy-event.getRawY()<5)&&(touchy-event.getRawY())>-5)) {
                            isMove = false;
                        }
                        else isMove=true;//如果移动太小 视为点击
                        //isMove=true;

                        Log.i("asffa","touchxy rawxy分别为"+touchx+" "+touchy+" "+event.getRawX()+" "+event.getRawY());
                        Log.i("wm.x wm.y:"," "+wmParams.x+wmParams.y);
                        Log.i("statusbarhight:",""+statusBarHeight);
                        Log.i("ismove=",""+isMove);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("捕捉到up","up");
                        if(!clickBoolean&&!isMove){
                            clickBoolean=true;
                            xiaoQiPaoView.drawXiaoQiPao();
                            Log.i("petView", "在这里执行onclick事件如何");
                        }
                        isMove=false;
                        break;
                }
                mWindowMangager.updateViewLayout(mFloatLayout,wmParams); //刷新悬浮窗的显示

                return false;//此处必须返回false，否则OnclickListener获取不到监听
            }
        });

        petImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (!clickBoolean&&!isMove) {
                    clickBoolean=true;
                    Log.i("宠物生长值现在为","afs"+getValue("growNumber")+"宠物智力为"+getValue("intelligence"));

                    testView.drawTestView();
                }
                return true;
            }
        });

        balloonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickballoon();
                Log.i("执行了点击气球事件","执行");
            }
        });//点击气球 气球爆炸

    }

    public void playWave(){

        if(!sleepboolean&&!playballoonboolean) {

            MyAnimationDrawable.animateRawManuallyFromXML(R.drawable.waving,
                    petImage, new Runnable() {
                        @Override
                        public void run() {
                            // TODO onStart
                            // 动画开始时回调
                            Log.d("","start");
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            // TODO onComplete
                            // 动画结束时回调
                            Log.d("","end");

                        }
                    });

            /*petImage.setImageResource(R.drawable.waving);
            actionAnim = (AnimationDrawable) petImage.getDrawable();
            actionAnim.start();*/
        }
    }//挥手

    public void playing(){

        if(!sleepboolean&&!playballoonboolean) {
            if(wmParams.y>(screenHight-wmParams.height-340)) {
                wmParams.y = screenHight - wmParams.height - 340;
            }
            playballoonboolean=true;
            exitballoonbooleam=true;
            mWindowMangager.removeView(mFloatLayout);//先移走再添加回来
            //添加气球
            wmParams.y -= 160;

            RelativeLayout.LayoutParams balloonParams = new RelativeLayout.LayoutParams(150, 150);

            balloonImage.setImageResource(R.drawable.balloonblast1);
            balloonImage.setLayoutParams(balloonParams);


            Log.i("宠物宠物宠物petX", ":" + wmParams.x + "petHeight:" + petImage.getHeight());


            RelativeLayout.LayoutParams petImageParams = new RelativeLayout.LayoutParams(petImage.getWidth(), petImage.getHeight());
            petImage.setLayoutParams(petImageParams);
            petImageParams.leftMargin = 47;
            petImageParams.topMargin = 138;

            mWindowMangager.addView(mFloatLayout, wmParams);

            MyAnimationDrawable.animateRawManuallyFromXML(R.drawable.playing,
                    petImage, new Runnable() {
                        @Override
                        public void run() {
                            // TODO onStart
                            // 动画开始时回调
                            Log.d("","start");
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            // TODO onComplete
                            // 动画结束时回调
                            Log.d("","end");

                        }
                    });

            /*petImage.setImageResource(R.drawable.playing);
            actionAnim = (AnimationDrawable) petImage.getDrawable();
            actionAnim.start();*/


            //坐标变换
            playballoontimer = new Timer(true);
            TimerTask playBalloonTask = new TimerTask() {
                @Override
                public void run() {
                    if(wmParams.y>0) {
                        wmParams.y -= 5;
                    }
                    //else Log.i("上到顶时的此时wmparams.y是",""+wmParams.y);
                    handler.post(runnablePlayBalloon);
                    /*if(wmParams.y<0){
                        clickballoon();
                    }*/

                }
            };
            playballoontimer.schedule(playBalloonTask, 0, 100);
        }

    }//玩气球

    public void blink(){
        if(!sleepboolean&&!playballoonboolean) {

            MyAnimationDrawable.animateRawManuallyFromXML(R.drawable.blink,
                    petImage, new Runnable() {
                        @Override
                        public void run() {
                            // TODO onStart
                            // 动画开始时回调
                            Log.d("","start");
                        }
                    }, new Runnable() {
                        @Override
                        public void run() {
                            // TODO onComplete
                            // 动画结束时回调
                            Log.d("","end");

                        }
                    });

            /*petImage.setImageResource(R.drawable.blink);
            actionAnim = (AnimationDrawable) petImage.getDrawable();
            actionAnim.start();*/
        }
    }//眨眼

    public void sleep(){
        if(!sleepboolean&&!playballoonboolean) {
            sleepboolean = true;
            petImage.setImageResource(R.drawable.sleep);
            petImage.setLayoutParams(new RelativeLayout.LayoutParams((int) (petImage.getWidth() / 1.4), (int) (petImage.getHeight() / 1.4)));
            sleepAnim = (AnimationDrawable) petImage.getDrawable();
            sleepAnim.start();
        }
    }//睡觉

    public void wake(){
        if(sleepboolean&&!playballoonboolean){
            petImage.setImageResource(R.drawable.newblinknoblink);
            petImage.setLayoutParams(new RelativeLayout.LayoutParams((int) (petImage.getWidth() * 1.4), (int) (petImage.getHeight() * 1.4)));
            sleepboolean=false;
        }
    }//醒来 恢复正常形态

    public void clickballoon(){
        if(wmParams.y<=0){
            wmParams.y=0;
        }//发现用户手动拖拉到小于等于0 让它等于0
        exitballoonbooleam=false;//点击的瞬间气球不存在了
        RelativeLayout.LayoutParams balloonParams = new RelativeLayout.LayoutParams(0, 0);
        balloonImage.setLayoutParams(balloonParams);
        RelativeLayout.LayoutParams petImageParams=(RelativeLayout.LayoutParams)petImage.getLayoutParams();
        petImageParams.topMargin = 0;
        petImageParams.leftMargin=0;
        wmParams.y+=160;
        mWindowMangager.updateViewLayout(mFloatLayout,wmParams);
        playballoontimer.cancel();

        petImage.setImageResource(R.drawable.newfall);
        inFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        falltimer = new Timer(true);
        TimerTask fallTask = new TimerTask() {
            @Override
            public void run() {

                wmParams.y += 5;
                handler.post(runnablePlayBalloon);
                if(wmParams.y>(screenHight-wmParams.height-340)){
                    wmParams.y=screenHight-wmParams.height-340;//160是补上升的坑
                    Log.i("落到底时的此时wmparams.y是",""+wmParams.y);
                    falltimer.cancel();
                    handler.post(fallRunnable);
                    playballoonboolean=false;
                }
            }
        };
        falltimer.schedule(fallTask, 0, 40);

    }



}
