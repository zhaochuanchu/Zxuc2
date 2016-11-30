package com.example.zcc.zxuc;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ToolsMethod {

    public static void setParams(WindowManager.LayoutParams params, int x, int y){

        params.type= WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//设置window type最高显示优先级
        params.format= PixelFormat.RGBA_8888;//设置图片格式 效果为背景透明
        params.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        params.gravity= Gravity.LEFT|Gravity.TOP;//调整悬浮窗显示的停靠位置为左侧置顶
        params.x=x;params.y=y;//以屏幕左上角为原点 设置x，y的初始值 相对于gravity
        params.width=WindowManager.LayoutParams.WRAP_CONTENT;//自适应
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;//设置悬浮窗口长宽数据 自适应
    }

    public static AlphaAnimation alphaAnimation(int i){

        AlphaAnimation a=null;

        AnimationSet animationSet1 = new AnimationSet(true);
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0, 1);
        alphaAnimation1.setDuration(1000);
        animationSet1.addAnimation(alphaAnimation1);

        AnimationSet animationSet2 = new AnimationSet(true);
        AlphaAnimation alphaAnimation2 = new AlphaAnimation(1, 0);
        alphaAnimation2.setDuration(1000);
        animationSet2.addAnimation(alphaAnimation2);

        switch(i){
            case 1:a=alphaAnimation1;break;
            case 2:a=alphaAnimation2;break;
        }
        return a;
    }//加载渐入渐出特效

    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public static int getDp(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, MyApplication.getContext().getResources().getDisplayMetrics());
    }//获取动态布局中设置的dp值 即pix——>dp

}
