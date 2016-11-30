package com.example.zcc.zxuc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Objects;
import java.util.Random;


public class DbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String TABLE_NAME="PetData";

    public DbHelper(Context context){
        super(context,TABLE_NAME,null,VERSION);
    }

    //辅助类建立时运行该方法
    @Override
    public void onCreate(SQLiteDatabase db){
        String sql ="create table IF NOT EXISTS user(id integer primary key autoincrement,name varchar(20),value integer)";
        db.execSQL(sql);
        /********在这里初始化宠物属性*******/
        db.execSQL("insert into user(id,name,value) values(1,'growNumber',0)");//初始生长值 0
        db.execSQL("insert into user(id,name,value) values(2,'money',0)");//初始元宝 0
        db.execSQL("insert into user(id,name,value) values(3,'intelligence',?)",new Object[]{new Random().nextInt(20)+30});//初始智力30-50
        db.execSQL("insert into user(id,name,value) values(4,'force',?)",new Object[]{new Random().nextInt(20)+30});//初始武力30-50
        db.execSQL("insert into user(id,name,value) values(5,'health',60)");//初始健康值60
        db.execSQL("insert into user(id,name,value) values(6,'mood',6)");//初始心情6
        db.execSQL("insert into user(id,name,value) values(7,'rank',1)");//初始等级1

        db.execSQL("insert into user(id,name,value) values(8,'haveit',0)");//初始为0 表示它尚未创建 当其为1时 表示已经创建了宠物

        db.execSQL("insert into user(id,name,value) values(9,'name',null)");//9号 初始name null
        db.execSQL("insert into user(id,name,value) values(10,'mastername',null)");//10号 初始称呼用户的名字 null

        db.execSQL("insert into user(id,name,value) values(11,'',null)");//10号 初始称号 为空‘’

        db.execSQL("insert into user(id,name,value) values(12,'book3',0)");//book3是提高30%增长速度的

        Log.i("asd","数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        Log.i("asf","数据库已经更新");
    }


}
