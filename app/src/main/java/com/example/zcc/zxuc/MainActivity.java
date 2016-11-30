package com.example.zcc.zxuc;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

import static com.example.zcc.zxuc.FxService.dbWrite;
import static com.example.zcc.zxuc.FxService.getValue;
import static com.example.zcc.zxuc.FxService.permit;

public class MainActivity extends Activity {

    public static int haveit;

    public RelativeLayout mainLayout;
    public RelativeLayout smallLayout;

    private TextView startTextView;
    private TextView removeTextView;
    private TextView helpTextView;
    private TextView attentionTextView;
    private TextView restartTextView;
    private TextView aboutTextView;
    private TextView exitTextView;


    public static EditText name;
    public static EditText mastername;
    private TextView okTextView;

    private TextView helpText;
    private TextView aboutText;
    private TextView attentionText;

    private int clicktwo;



    DbHelper helper2;
    SQLiteDatabase dbRead2;
    SQLiteDatabase dbWrite2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(getAppOps(this)){
            Log.i("有悬浮窗权限","有悬浮窗权限");
            permit=true;
        }
        else {
            permit=false;
            Toast.makeText(this,"要先开启悬浮窗权限哦",Toast.LENGTH_SHORT).show();
        }

        clicktwo=0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper2=new DbHelper(this);
        dbRead2=helper2.getReadableDatabase();//如果没有数据库 会新建一个
        dbWrite2=helper2.getWritableDatabase();


        mainLayout=(RelativeLayout)findViewById(R.id.activity_main);
        smallLayout=(RelativeLayout)findViewById(R.id.smallLayout);

        name=(EditText)smallLayout.findViewById(R.id.name);
        mastername=(EditText)smallLayout.findViewById(R.id.mastername);
        okTextView=(TextView)smallLayout.findViewById(R.id.ok_id);


        startTextView =(TextView)findViewById(R.id.start_id);

        removeTextView=(TextView)findViewById(R.id.remove_id);

        helpTextView=(TextView)findViewById(R.id.help_id);

        attentionTextView=(TextView)findViewById(R.id.attention_id);

        //restartTextView=(TextView)findViewById(R.id.restart_id);

        aboutTextView=(TextView)findViewById(R.id.about_id);

        exitTextView=(TextView)findViewById(R.id.exit_id);

        helpText=(TextView)findViewById(R.id.helpText);
        aboutText=(TextView)findViewById(R.id.aboutText);
        attentionText=(TextView)findViewById(R.id.attentionText);

        helpText.setMovementMethod(ScrollingMovementMethod.getInstance());
        aboutText.setMovementMethod(ScrollingMovementMethod.getInstance());
        attentionText.setMovementMethod(ScrollingMovementMethod.getInstance());


        startTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                permit=getAppOps(MainActivity.this);
                if(permit) {
                    if (getValue2("haveit") == 0) {
                        draw1();
                    } else {
                        Intent intent = new Intent(MainActivity.this, FxService.class);
                        //启动FxService
                        startService(intent);
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"要先开启悬浮窗权限哦",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},24);
                }
            }
        });

        removeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,FxService.class);
                //终止FxService
                stopService(intent);
            }
        });

        helpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicktwo==1){
                    helpText.setVisibility(View.INVISIBLE);
                    clicktwo=0;
                }
                else {
                    attentionText.setVisibility(View.GONE);
                    aboutText.setVisibility(View.GONE);
                    helpText.setText(R.string.help);
                    helpText.setVisibility(View.VISIBLE);
                    clicktwo=1;
                }
            }
        });

        attentionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicktwo==2){
                    attentionText.setVisibility(View.INVISIBLE);
                    clicktwo=0;
                }
                else {
                    helpText.setVisibility(View.GONE);
                    aboutText.setVisibility(View.GONE);
                    attentionText.setText(R.string.attention);
                    attentionText.setVisibility(View.VISIBLE);
                    clicktwo = 2;
                }
            }
        });


        aboutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicktwo==3){
                    aboutText.setVisibility(View.INVISIBLE);
                    clicktwo=0;
                }
                else {
                    helpText.setVisibility(View.GONE);
                    attentionText.setVisibility(View.GONE);
                    aboutText.setText(R.string.about);
                    aboutText.setVisibility(View.VISIBLE);
                    clicktwo=3;
                }
            }
        });

        exitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRead2.close();
                dbWrite2.close();
                finish();
            }
        });

        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FxService.class);
                startService(intent);
                dbWrite2.execSQL("update user set name = ? where id=9", new Object[]{name.getText()});
                dbWrite2.execSQL("update user set name = ? where id=10", new Object[]{mastername.getText()});
                Toast.makeText(MainActivity.this,"创建宠物成功",Toast.LENGTH_LONG);
                draw2();
                if(!permit){
                    //dialog();
                }
            }
        });

        smallLayout.setVisibility(View.GONE);

    }

    private void xuhuabg(){

        RelativeLayout mainLayout=(RelativeLayout)findViewById(R.id.activity_main);
        Bitmap bgbitmap= BitmapFactory.decodeResource(getResources(),R.drawable.activitybg);

        int scaleRatio = 10;
        int blurRadius = 8;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bgbitmap,
                bgbitmap.getWidth() / scaleRatio,
                bgbitmap.getHeight() / scaleRatio,
                false);
        Bitmap blurBitmap = FastBlurUtil.doBlur(scaledBitmap, blurRadius, true);

        Drawable bgDrawable=new BitmapDrawable(blurBitmap);

        if(Build.VERSION.SDK_INT>=16) {
            mainLayout.setBackground(bgDrawable);
        }
        else mainLayout.setBackgroundDrawable(bgDrawable);
    }//虚化背景图片

    public void draw1(){
        startTextView.setVisibility(View.GONE);
        removeTextView.setVisibility(View.GONE);
        helpTextView.setVisibility(View.GONE);
        attentionTextView.setVisibility(View.GONE);

        aboutTextView.setVisibility(View.GONE);
        exitTextView.setVisibility(View.GONE);

        helpText.setVisibility(View.GONE);
        attentionText.setVisibility(View.GONE);
        aboutText.setVisibility(View.GONE);


        smallLayout.setVisibility(View.VISIBLE);
        if(Build.VERSION.SDK_INT>=16){
            xuhuabg();
        }
    }

    public void draw2(){
        smallLayout.setVisibility(View.GONE);

        startTextView.setVisibility(View.VISIBLE);
        removeTextView.setVisibility(View.VISIBLE);
        helpTextView.setVisibility(View.VISIBLE);
        attentionTextView.setVisibility(View.VISIBLE);
        aboutTextView.setVisibility(View.VISIBLE);
        exitTextView.setVisibility(View.VISIBLE);


        mainLayout.setBackgroundResource(R.drawable.activitybg);
    }

    private int getValue2(String name){
        Cursor cursor = dbRead2.query("user", new String[]{"name","value"}, "name=?", new String[]{name}, null, null, null, null);
        int oldValue=999;
        while(cursor.moveToNext()) {
            oldValue=cursor.getInt(cursor.getColumnIndex("value"));
        }
        cursor.close();
        return oldValue;
    }//取得name的value(int)值

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("请为本应用提供悬浮窗权限");
        builder.setTitle("提示！");

        builder.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
               }
           });
        builder.create().show();
        }

    public static boolean getAppOps(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);//24代表的悬浮窗权限
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }



}
