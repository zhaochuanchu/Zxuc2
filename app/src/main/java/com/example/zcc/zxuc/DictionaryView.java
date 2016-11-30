package com.example.zcc.zxuc;

import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.zcc.zxuc.FxService.clickBoolean;
import static com.example.zcc.zxuc.FxService.dictionaryView;
import static com.example.zcc.zxuc.FxService.getTranslation;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.menuView;
import static com.example.zcc.zxuc.FxService.onView;
import static com.example.zcc.zxuc.FxService.screenWidth;
import static com.example.zcc.zxuc.FxService.statusBarHeight;
import static com.example.zcc.zxuc.FxService.testView;
import static com.example.zcc.zxuc.PetView.petImage;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ToolsMethod.alphaAnimation;
import static com.example.zcc.zxuc.ToolsMethod.setParams;

public class DictionaryView {

    private TextView resultTextView;
    private EditText inputEditText;
    private ImageView returnImage;
    private ImageView closeImage;
    private ImageView translateImage;
    private ImageView bgDictionary;

    private WindowManager.LayoutParams dParams;//对应字典的params
    private LinearLayout dictionaryFloatLayout;//字典界面布局
    private int clickX,clickY;


    public DictionaryView(){
        dParams=new WindowManager.LayoutParams();
        setParams(dParams,0,0);
        dParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        dictionaryFloatLayout=(LinearLayout)inflater.inflate(R.layout.dictionary,null);
        resultTextView=(TextView)dictionaryFloatLayout.findViewById(R.id.resultTextView);
        resultTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        inputEditText=(EditText)dictionaryFloatLayout.findViewById(R.id.inputEditText);

        returnImage=(ImageView) dictionaryFloatLayout.findViewById(R.id.returnImage);
        returnImage.setImageResource(R.drawable.returnpicture);

        closeImage=(ImageView) dictionaryFloatLayout.findViewById(R.id.closeImage);
        closeImage.setImageResource(R.drawable.closepicture);

        translateImage=(ImageView)dictionaryFloatLayout.findViewById(R.id.translateImage);
        translateImage.setImageResource(R.drawable.search);

        bgDictionary=(ImageView)dictionaryFloatLayout.findViewById(R.id.bgDictionary);
        bgDictionary.setImageResource(R.drawable.bg);

        dictionaryFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        translateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//翻译
                /*dParams.x=550;
                dParams.y=550;*/
                String input=inputEditText.getText().toString();
                String result=getTranslation(input);
                resultTextView.setText(result);
                mWindowMangager.updateViewLayout(dictionaryFloatLayout,dParams);
            }
        });

        returnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//返回
                removeDictionary();
                //menuView.drawMenuView();
                testView.drawTestView();

            }
        });
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDictionary();
                clickBoolean=false;
            }
        });


        dictionaryFloatLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {//拖动字典界面
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        clickX=(int)event.getX();
                        clickY=(int)event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        dParams.x=(int)(event.getRawX()-clickX);
                        dParams.y=(int)(event.getRawY()-clickY- statusBarHeight);//网上的算法烂的跟屎一样 还tm写博客
                        break;
                }
                mWindowMangager.updateViewLayout(dictionaryFloatLayout,dParams); //刷新悬浮窗的显示
                return false;
            }
        });

    }//加载字典布局

    public void drawDictionary(){

        //dParams.x = (int) (wmParams.x + petImage.getWidth() - 70);
        //dParams.y = (int) (wmParams.y - 550);

        onView=1;
        dParams.x = (int) (wmParams.x + petImage.getWidth() +10);
        dParams.y = (int) (wmParams.y-80);
        if(dParams.x>screenWidth-bgDictionary.getWidth()){
            dParams.x = (int) (wmParams.x + petImage.getWidth() - bgDictionary.getWidth()-190);
            dParams.y = (int) (wmParams.y-80);
        }


        mWindowMangager.addView(dictionaryFloatLayout,dParams);
        bgDictionary.startAnimation(alphaAnimation(1));
    }//绘制字典界面

    public void removeDictionary(){
        mWindowMangager.removeView(dictionaryFloatLayout);
    }


}
