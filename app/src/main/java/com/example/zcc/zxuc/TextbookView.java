package com.example.zcc.zxuc;

import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static com.example.zcc.zxuc.FxService.clickBoolean;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.screenWidth;
import static com.example.zcc.zxuc.FxService.statusBarHeight;
import static com.example.zcc.zxuc.FxService.testView;
import static com.example.zcc.zxuc.PetView.petImage;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ToolsMethod.alphaAnimation;
import static com.example.zcc.zxuc.ToolsMethod.setParams;

/**
 * Created by zcc on 2016/11/12.
 */

public class TextbookView {
    private EditText saveEditText;
    private ImageView returnImage;
    private ImageView closeImage;
    private ImageView bgImage;

    private WindowManager.LayoutParams tParams;
    private LinearLayout textbookFloatLayout;
    private int clickX,clickY;

    public TextbookView(){
        tParams=new WindowManager.LayoutParams();
        setParams(tParams,0,0);
        tParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        textbookFloatLayout=(LinearLayout)inflater.inflate(R.layout.textbook_layout,null);

        bgImage=(ImageView)textbookFloatLayout.findViewById(R.id.bgImage);
        bgImage.setImageResource(R.drawable.bg);

        saveEditText=(EditText)textbookFloatLayout.findViewById(R.id.saveEditText);
        //saveEditText.setMovementMethod(ScrollingMovementMethod.getInstance());

        returnImage=(ImageView) textbookFloatLayout.findViewById(R.id.returnImage);
        returnImage.setImageResource(R.drawable.returnpicture);

        closeImage=(ImageView) textbookFloatLayout.findViewById(R.id.closeImage);
        closeImage.setImageResource(R.drawable.closepicture);

        textbookFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        textbookFloatLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {//拖动字典界面
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        clickX=(int)event.getX();
                        clickY=(int)event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        tParams.x=(int)(event.getRawX()-clickX);
                        tParams.y=(int)(event.getRawY()-clickY- statusBarHeight);//网上的算法烂的跟屎一样 还tm写博客
                        break;
                }
                mWindowMangager.updateViewLayout(textbookFloatLayout,tParams); //刷新悬浮窗的显示
                return false;
            }
        });

        returnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//返回
                removeTextbookView();
                //menuView.drawMenuView();
                testView.drawTestView();

            }
        });
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTextbookView();
                clickBoolean=false;
            }
        });



    }

    public void drawTextbookView() {

        tParams.x = (int) (wmParams.x + petImage.getWidth() + 10);
        tParams.y = (int) (wmParams.y - 80);
        if (tParams.x > screenWidth - bgImage.getWidth()) {
            tParams.x = (int) (wmParams.x + petImage.getWidth() - bgImage.getWidth() - 190);
            tParams.y = (int) (wmParams.y - 80);
        }

        mWindowMangager.addView(textbookFloatLayout,tParams);
        bgImage.startAnimation(alphaAnimation(1));
    }

    public void removeTextbookView(){
        mWindowMangager.removeView(textbookFloatLayout);
    }


}
