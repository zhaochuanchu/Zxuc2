package com.example.zcc.zxuc;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static com.example.zcc.zxuc.FxService.clickBoolean;
import static com.example.zcc.zxuc.FxService.dbWrite;
import static com.example.zcc.zxuc.FxService.forcejudge;
import static com.example.zcc.zxuc.FxService.getValue;
import static com.example.zcc.zxuc.FxService.growNumberjudge;
import static com.example.zcc.zxuc.FxService.handler;
import static com.example.zcc.zxuc.FxService.healthjudge;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.intelligencejudge;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.moneyjudge;
import static com.example.zcc.zxuc.FxService.moodjudge;
import static com.example.zcc.zxuc.FxService.runnableShop;
import static com.example.zcc.zxuc.FxService.screenWidth;
import static com.example.zcc.zxuc.FxService.statusBarHeight;
import static com.example.zcc.zxuc.FxService.testView;
import static com.example.zcc.zxuc.PetView.petImage;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ToolsMethod.alphaAnimation;
import static com.example.zcc.zxuc.ToolsMethod.getDp;
import static com.example.zcc.zxuc.ToolsMethod.setParams;

/**
 * Created by zcc on 2016/11/12.
 */

public class ShopView {
    private ImageView returnImage;
    private ImageView closeImage;
    private ImageView bgImage;

    private ImageView bigImageView;
    private TextView bigTextView;
    private TextView buyItTextView;

    private ViewGroup viewGroup;

    private int i;
    private int onClickId;

    public static WindowManager.LayoutParams sParams;//对应字典的params
    public static LinearLayout shopFloatLayout;//字典界面布局

    private HorizontalScrollView horizontalScrollView;//横向滚动商品菜单
    private LinearLayout scrollLinearLayout;//菜单的横向布局

    private int[] goodIds=new int[]{
            R.drawable.food1,R.drawable.food2,R.drawable.food3,R.drawable.food4,R.drawable.food5,
            R.drawable.food6,R.drawable.food7,R.drawable.food8,R.drawable.food9,R.drawable.book1,
            R.drawable.book2,R.drawable.book3,R.drawable.book4,
    };


    private int clickX,clickY;

    public ShopView(){
        sParams=new WindowManager.LayoutParams();
        setParams(sParams,0,0);
        shopFloatLayout=(LinearLayout)inflater.inflate(R.layout.shop_layout,null);

        horizontalScrollView=(HorizontalScrollView)shopFloatLayout.findViewById(R.id.scollView);
        scrollLinearLayout=(LinearLayout)shopFloatLayout.findViewById(R.id.scrollLinearLayout);

        returnImage=(ImageView) shopFloatLayout.findViewById(R.id.returnImage);
        returnImage.setImageResource(R.drawable.returnpicture);
        //returnImage.setVisibility(View.GONE);//先让return走掉

        closeImage=(ImageView) shopFloatLayout.findViewById(R.id.closeImage);
        closeImage.setImageResource(R.drawable.closepicture);

        bgImage=(ImageView)shopFloatLayout.findViewById(R.id.bgShop);
        bgImage.setImageResource(R.drawable.bg);

        bigImageView=(ImageView)shopFloatLayout.findViewById(R.id.bigImageView);

        bigTextView=(TextView)shopFloatLayout.findViewById(R.id.bigTextView);
        bigTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        buyItTextView=(TextView)shopFloatLayout.findViewById(R.id.buyIt);

        onClickId=-1;

        returnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//返回
                removeShopView();
                //menuView.drawMenuView();
                testView.drawTestView();

            }
        });
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeShopView();
                clickBoolean=false;
            }
        });

        buyItTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();
            }
        });

        shopFloatLayout.setOnTouchListener(new View.OnTouchListener() {
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
                mWindowMangager.updateViewLayout(shopFloatLayout,sParams); //刷新悬浮窗的显示
                return false;
            }
        });


    }

    public void drawShopView(){
        sParams.x = (int) (wmParams.x + petImage.getWidth() +10);
        sParams.y = (int) (wmParams.y-80);
        if(sParams.x>screenWidth-bgImage.getWidth()){
            sParams.x = (int) (wmParams.x + petImage.getWidth() - bgImage.getWidth()-190);
            sParams.y = (int) (wmParams.y-80);
        }
        addgoods();

        mWindowMangager.addView(shopFloatLayout,sParams);
        bgImage.startAnimation(alphaAnimation(1));
    }

    public void removeShopView(){
        mWindowMangager.removeView(shopFloatLayout);
    }

    private void addgoods(){
        ImageView imageView;
        LinearLayout.LayoutParams imageViewLayoutParams;

        scrollLinearLayout.removeAllViews();
        viewGroup=scrollLinearLayout;


        for(i=0;i<goodIds.length;i++){
            imageView=new ImageView(MyApplication.getContext());
            imageView.setImageResource(goodIds[i]);
            scrollLinearLayout.addView(imageView);
            imageViewLayoutParams=(LinearLayout.LayoutParams)imageView.getLayoutParams();
            imageViewLayoutParams.width=getDp(25);
            imageViewLayoutParams.height=getDp(25);
            imageView.setLayoutParams(imageViewLayoutParams);//有时候需要set回来（局部定义的layoutparams）,有时候不需要
            Drawable drawable=MyApplication.getContext().getResources().getDrawable(R.drawable.goods);
            if(Build.VERSION.SDK_INT>=16) {
                imageView.setBackground(drawable);
            }
            else imageView.setBackgroundDrawable(drawable);

            setIds(imageView,i);//为控件设置ID

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetGoods(viewGroup);
                    LinearLayout.LayoutParams vParams=(LinearLayout.LayoutParams)v.getLayoutParams();
                    vParams.width=getDp(35);
                    vParams.height=getDp(35);
                    v.setLayoutParams(vParams);
                    setShopClick(v.getId());
                    onClickId=v.getId();
                }
            });

            if(i==0){
                imageView.performClick();//默认点开第一个显示
            }

        }
    }//添加商品

    private void setShopClick(int id){
        switch(id){
            case R.id.food1:
                bigImageView.setImageResource(R.drawable.food1);
                bigTextView.setText(R.string.food1);
                handler.post(runnableShop);
                break;
            case R.id.food2:
                bigImageView.setImageResource(R.drawable.food2);
                bigTextView.setText(R.string.food2);
                handler.post(runnableShop);
                break;
            case R.id.food3:
                bigImageView.setImageResource(R.drawable.food3);
                bigTextView.setText(R.string.food3);
                handler.post(runnableShop);
                break;
            case R.id.food4:
                bigImageView.setImageResource(R.drawable.food4);
                bigTextView.setText(R.string.food4);
                handler.post(runnableShop);
                break;
            case R.id.food5:
                bigImageView.setImageResource(R.drawable.food5);
                bigTextView.setText(R.string.food5);
                handler.post(runnableShop);
                break;
            case R.id.food6:
                bigImageView.setImageResource(R.drawable.food6);
                bigTextView.setText(R.string.food6);
                handler.post(runnableShop);
                break;
            case R.id.food7:
                bigImageView.setImageResource(R.drawable.food7);
                bigTextView.setText(R.string.food7);
                handler.post(runnableShop);
                break;
            case R.id.food8:
                bigImageView.setImageResource(R.drawable.food8);
                bigTextView.setText(R.string.food8);
                handler.post(runnableShop);
                break;
            case R.id.food9:
                bigImageView.setImageResource(R.drawable.food9);
                bigTextView.setText(R.string.food9);
                handler.post(runnableShop);
                break;
            case R.id.book1:
                bigImageView.setImageResource(R.drawable.book1);
                bigTextView.setText(R.string.book1);
                handler.post(runnableShop);
                break;
            case R.id.book2:
                bigImageView.setImageResource(R.drawable.book2);
                bigTextView.setText(R.string.book2);
                handler.post(runnableShop);
                break;
            case R.id.book3:
                bigImageView.setImageResource(R.drawable.book3);
                bigTextView.setText(R.string.book3);
                handler.post(runnableShop);
                break;
            case R.id.book4:
                bigImageView.setImageResource(R.drawable.book4);
                bigTextView.setText(R.string.book4);
                handler.post(runnableShop);
                break;
        }
    }//根据view的id设定相应的点击事件

    private void setIds(ImageView iv,int i){
        switch(i){
            case 0:iv.setId(R.id.food1);break;
            case 1:iv.setId(R.id.food2);break;
            case 2:iv.setId(R.id.food3);break;
            case 3:iv.setId(R.id.food4);break;
            case 4:iv.setId(R.id.food5);break;
            case 5:iv.setId(R.id.food6);break;
            case 6:iv.setId(R.id.food7);break;
            case 7:iv.setId(R.id.food8);break;
            case 8:iv.setId(R.id.food9);break;

            case 9:iv.setId(R.id.book1);break;
            case 10:iv.setId(R.id.book2);break;
            case 11:iv.setId(R.id.book3);break;
            case 12:iv.setId(R.id.book4);break;
            /*case 13:iv.setId(R.id.book5);break;
            case 14:iv.setId(R.id.book6);break;
            case 15:iv.setId(R.id.book7);break;
            case 16:iv.setId(R.id.book8);break;
            case 17:iv.setId(R.id.book9);break;*/
            default:break;
        }
    }//给添加的每一个控件设置id

    private void resetGoods(ViewGroup viewGroup){

        if (viewGroup == null) {
            return;
        }
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ImageView) { // 若是ImageView 还原成30dp大小
                ImageView iv = (ImageView) view;
                ViewGroup.LayoutParams ivParams=iv.getLayoutParams();
                ivParams.width=getDp(25);
                ivParams.height=getDp(25);
                iv.setLayoutParams(ivParams);
            }
        }

    }//遍历所有商品设置成30大小(控件 还可以辨别控件类型进行相应操作 超级赞)

    private void buy(){
        switch (onClickId){
            case R.id.food1:
                if(moneyjudge(380)) {
                    dbWrite.execSQL("update user set value = ? where name = 'growNumber'", new Object[]{getValue("growNumber") + 100});
                    growNumberjudge();
                }
                else voidMoney();
                break;
            case R.id.food2:
                if(moneyjudge(400)) {
                    dbWrite.execSQL("update user set value = ? where name = 'mood'", new Object[]{getValue("mood") + 1});
                    moodjudge();
                }
                else voidMoney();
                break;
            case R.id.food3:
                if(moneyjudge(140)) {
                    dbWrite.execSQL("update user set value = ? where name = 'growNumber'", new Object[]{getValue("growNumber") + 40});
                    growNumberjudge();
                }
                else voidMoney();
                break;
            case R.id.food4:
                if(moneyjudge(520)) {
                    dbWrite.execSQL("update user set value = ? where name = 'growNumber'", new Object[]{getValue("growNumber") + 150});
                    growNumberjudge();
                }
                else voidMoney();
                break;
            case R.id.food5:
                if(moneyjudge(10)) {//坑钱的
                }
                else voidMoney();
                break;
            case R.id.food6:
                if(moneyjudge(500)) {
                    dbWrite.execSQL("update user set value = ? where name = 'health'", new Object[]{getValue("health") + 10});
                   healthjudge();
                }
                else voidMoney();
                break;
            case R.id.food7:
                if(moneyjudge(1000)) {
                    dbWrite.execSQL("update user set value = ? where name = 'mood'", new Object[]{getValue("mood") + 2});
                    moodjudge();
                }
                else voidMoney();
                break;
            case R.id.food8:
                if(moneyjudge(250)) {
                    dbWrite.execSQL("update user set value = ? where name = 'health'", new Object[]{getValue("health") + new Random().nextInt(10)+1});//增加1~10
                    healthjudge();
                }
                else voidMoney();
                break;
            case R.id.food9:
                if(moneyjudge(1000)) {
                    dbWrite.execSQL("update user set value = ? where name = 'growNumber'", new Object[]{getValue("growNumber") + 100});
                    dbWrite.execSQL("update user set value = ? where name = 'health'", new Object[]{getValue("health") + 10});
                    dbWrite.execSQL("update user set value = ? where name = 'mood'", new Object[]{getValue("mood") + 1});
                    growNumberjudge();
                    healthjudge();
                    moodjudge();
                }
                else voidMoney();
                break;
            case R.id.book1:
                if(moneyjudge(1000)) {
                    dbWrite.execSQL("update user set value = ? where name = 'intelligence'", new Object[]{getValue("intelligence") + 1});
                    intelligencejudge();
                }
                else voidMoney();
                break;
            case R.id.book2:
                if(moneyjudge(1000)) {
                    dbWrite.execSQL("update user set value = ? where name = 'force'", new Object[]{getValue("force") + 1});
                    forcejudge();
                }
                else voidMoney();
                break;
            case R.id.book3:
                if(moneyjudge(10000)) {
                    dbWrite.execSQL("update user set value = ? where name = 'book3'", new Object[]{1});
                }
                else voidMoney();
                break;
            case R.id.book4:
                if(moneyjudge(10)) {
                    Toast.makeText(MyApplication.getContext(),"嘛咪嘛咪吼",Toast.LENGTH_SHORT).show();
                }
                else voidMoney();
                break;



            default:break;
        }
    }//点击buyIt后的操作

    private void voidMoney(){
        Toast.makeText(MyApplication.getContext(),"哎呦,钱不够哦",Toast.LENGTH_SHORT).show();
    }


}
