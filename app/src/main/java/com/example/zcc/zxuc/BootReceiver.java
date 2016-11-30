package com.example.zcc.zxuc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import static com.example.zcc.zxuc.FxService.getValue;


public class BootReceiver extends BroadcastReceiver {

    public static String BOOT_S = "android.intent.action.BOOT_COMPLETED";

    DbHelper helper3;
    SQLiteDatabase dbRead3;
    SQLiteDatabase dbWrite3;
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
     //在这里干想干的事 比如启动SerVice

        helper3=new DbHelper(context);
        dbRead3=helper3.getReadableDatabase();//如果没有数据库 会新建一个
        dbWrite3=helper3.getWritableDatabase();
        //if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
        if(getValue3("haveit")==1) {//如果已经创建过宠物
            Intent i = new Intent(context, FxService.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//网上说必须添加这个标记？
            context.startService(i);
            Toast.makeText(context, "开机自动启动宠物", Toast.LENGTH_SHORT).show();
        }
        dbRead3.close();
        dbWrite3.close();
        //}
    }

    public int getValue3(String name){
        Cursor cursor = dbRead3.query("user", new String[]{"name","value"}, "name=?", new String[]{name}, null, null, null, null);
        int oldValue=999;
        while(cursor.moveToNext()) {
            oldValue=cursor.getInt(cursor.getColumnIndex("value"));
        }
        cursor.close();
        return oldValue;
    }
}
