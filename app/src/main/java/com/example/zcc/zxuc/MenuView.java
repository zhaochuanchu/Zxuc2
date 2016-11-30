package com.example.zcc.zxuc;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static com.example.zcc.zxuc.FxService.clickBoolean;
import static com.example.zcc.zxuc.FxService.dictionaryView;
import static com.example.zcc.zxuc.FxService.inflater;
import static com.example.zcc.zxuc.FxService.mWindowMangager;
import static com.example.zcc.zxuc.FxService.natureView;
import static com.example.zcc.zxuc.FxService.petView;
import static com.example.zcc.zxuc.PetView.petImage;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ToolsMethod.setParams;

/**
 * Created by zcc on 2016/11/3.
 */

public class MenuView {

    public static LinearLayout menuFloatLayout;//气泡布局
    public static WindowManager.LayoutParams mParams;//对应气泡的Params

    private ImageView bgMenu;
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private ImageView item7;
    private ImageView item8;
    private ImageView item9;
    private ImageView closeImage;

    public MenuView(){
        mParams=new WindowManager.LayoutParams();
        setParams(mParams,0,0);
        menuFloatLayout=(LinearLayout)inflater.inflate(R.layout.menu_layout,null);

        bgMenu=(ImageView)menuFloatLayout.findViewById(R.id.bgMenu);
        bgMenu.setImageResource(R.drawable.bg);

        closeImage=(ImageView) menuFloatLayout.findViewById(R.id.closeImage);
        closeImage.setImageResource(R.drawable.closepicture);

        item1=(ImageView)menuFloatLayout.findViewById(R.id.item1);
        item1.setImageResource(R.drawable.icon2condition);
        item2=(ImageView)menuFloatLayout.findViewById(R.id.item2);
        item2.setImageResource(R.drawable.icon2dic);
        item3=(ImageView)menuFloatLayout.findViewById(R.id.item3);
        item3.setImageResource(R.drawable.icon2study);
        item4=(ImageView)menuFloatLayout.findViewById(R.id.item4);
        item4.setImageResource(R.drawable.icon3working);
        item5=(ImageView)menuFloatLayout.findViewById(R.id.item5);
        //item5.setImageResource(R.mipmap.ic_launcher);
        item6=(ImageView)menuFloatLayout.findViewById(R.id.item6);
        //item6.setImageResource(R.mipmap.ic_launcher);
        item7=(ImageView)menuFloatLayout.findViewById(R.id.item7);
        //item7.setImageResource(R.mipmap.ic_launcher);
        item8=(ImageView)menuFloatLayout.findViewById(R.id.item8);
        //item8.setImageResource(R.mipmap.ic_launcher);
        item9=(ImageView)menuFloatLayout.findViewById(R.id.item9);
        //item9.setImageResource(R.mipmap.ic_launcher);

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowMangager.removeView(menuFloatLayout);
                clickBoolean=false;
            }
        });

        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMenuView();
                natureView.drawNatureView();
            }
        });
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMenuView();
                dictionaryView.drawDictionary();
            }
        });
        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //petView.playing();
            }
        });
    }


    public void drawMenuView(){
        mParams.x = (int) (wmParams.x + petImage.getWidth() - 70);
        mParams.y = (int) (wmParams.y - 550);
        mWindowMangager.addView(menuFloatLayout,mParams);

    }//绘制主菜单界面

    public void removeMenuView(){

        mWindowMangager.removeView(menuFloatLayout);
    }//移除主菜单界面


}
