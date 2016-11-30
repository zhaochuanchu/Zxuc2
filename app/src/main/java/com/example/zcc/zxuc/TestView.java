package com.example.zcc.zxuc;


import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.zcc.zxuc.FxService.adventureView;
import static com.example.zcc.zxuc.FxService.clickBoolean;
import static com.example.zcc.zxuc.FxService.dictionaryView;
import static com.example.zcc.zxuc.FxService.handler;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.menuView;
import static com.example.zcc.zxuc.FxService.natureView;
import static com.example.zcc.zxuc.FxService.petView;
import static com.example.zcc.zxuc.FxService.runnableSlowMenu;
import static com.example.zcc.zxuc.FxService.shopView;
import static com.example.zcc.zxuc.FxService.status;
import static com.example.zcc.zxuc.FxService.studyView;
import static com.example.zcc.zxuc.FxService.testView;
import static com.example.zcc.zxuc.FxService.textbookView;
import static com.example.zcc.zxuc.FxService.workView;
import static com.example.zcc.zxuc.PetView.petImage;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ToolsMethod.setParams;

public class TestView {

    public static LinearLayout testFloatLayout;
    public static WindowManager.LayoutParams tParams;

    private ImageView conditionImage;
    private ImageView dictImage;
    private ImageView workImage;
    private ImageView studyImage;
    private ImageView adventureImage;
    private ImageView shopImage;
    private ImageView textbookImage;

    private ImageView closeImage;

    public TestView(){
        tParams=new WindowManager.LayoutParams();
        setParams(tParams,0,0);
        testFloatLayout=(LinearLayout)inflater.inflate(R.layout.test,null);

        conditionImage=(ImageView)testFloatLayout.findViewById(R.id.imageViewcondition);
        conditionImage.setImageResource(R.drawable.icon5condition);
        dictImage=(ImageView)testFloatLayout.findViewById(R.id.imageViewdict);
        dictImage.setImageResource(R.drawable.icon5dict);
        workImage=(ImageView)testFloatLayout.findViewById(R.id.imageViewworking);
        workImage.setImageResource(R.drawable.icon5working);
        studyImage=(ImageView)testFloatLayout.findViewById(R.id.imageViewstudy);
        studyImage.setImageResource(R.drawable.icon5study);
        adventureImage=(ImageView)testFloatLayout.findViewById(R.id.imageViewadventure);
        adventureImage.setImageResource(R.drawable.icon5adventure);
        shopImage=(ImageView)testFloatLayout.findViewById(R.id.imageViewshop);
        shopImage.setImageResource(R.drawable.icon5shop);
        textbookImage=(ImageView)testFloatLayout.findViewById(R.id.imageViewtextbook);
        textbookImage.setImageResource(R.drawable.icon5textbook);


        closeImage=(ImageView)testFloatLayout.findViewById(R.id.closeImage);
        closeImage.setImageResource(R.drawable.closepicture);

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowMangager.removeView(testFloatLayout);
                clickBoolean=false;
            }
        });
        dictImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testView.removeTestView();
                dictionaryView.drawDictionary();
            }
        });

        conditionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testView.removeTestView();
                natureView.drawNatureView();
            }
        });

        studyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testView.removeTestView();
                studyView.drawStudyView();
            }
        });

        textbookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testView.removeTestView();
                textbookView.drawTextbookView();
            }
        });

        shopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testView.removeTestView();
                shopView.drawShopView();
            }
        });

        workImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testView.removeTestView();
                workView.drawWorkView();
            }
        });

        adventureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testView.removeTestView();
                adventureView.drawAdventureView();
            }
        });


    }

    public void drawTestView(){
        tParams.x = (int) (wmParams.x + petImage.getWidth() - 100);
        tParams.y = (int) (wmParams.y - 100);
        //tParams.width=1;

        switch(status){
            case 1:
                workImage.setVisibility(View.GONE);
                adventureImage.setVisibility(View.GONE);
                anim(studyImage);
                break;
            case 2:
                studyImage.setVisibility(View.GONE);
                adventureImage.setVisibility(View.GONE);
                anim(workImage);
                break;
            case 3:
                workImage.setVisibility(View.GONE);
                studyImage.setVisibility(View.GONE);
                anim(adventureImage);
                break;
            case 0:
                workImage.setVisibility(View.VISIBLE);
                studyImage.setVisibility(View.VISIBLE);
                adventureImage.setVisibility(View.VISIBLE);
        }

        mWindowMangager.addView(testFloatLayout,tParams);
        /*final Timer timer=new Timer(true);
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                tParams.width+=5;
                handler.post(runnableSlowMenu);
                if(tParams.width>450){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask,300,40);*/
    }

    public void removeTestView(){
        mWindowMangager.removeView(testFloatLayout);
    }

    public void anim(ImageView imageView){
        Animation operatingAnim = AnimationUtils.loadAnimation(MyApplication.getContext(), R.anim.tip);
        AccelerateDecelerateInterpolator lin = new AccelerateDecelerateInterpolator();
        operatingAnim.setInterpolator(lin);
        imageView.startAnimation(operatingAnim);
    }




}
