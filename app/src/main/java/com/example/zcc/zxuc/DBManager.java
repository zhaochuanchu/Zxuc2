package com.example.zcc.zxuc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;


public class DBManager {
    private final int BUFFER_SIZE=400000;
    public final static String STATEMENT_DB_NAME="test.db";
    public final static String PACKAGE_NAME="com.example.zcc.zxuc";
    public final static String STATEMENT_DB_PATH="/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;  //在手机里存放数据库的位置
    private SQLiteDatabase db;
    private Context context;

    DBManager(Context context){
        this.context=context;
    }

    public void openDateBase(){
        this.db=this.openDateBase(STATEMENT_DB_PATH + "/" + STATEMENT_DB_NAME);
    }

    public SQLiteDatabase openDateBase(String dbfile){
        try{
            if(!(new File(dbfile).exists())){
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.test); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
                    null);
            return db;
        }catch (FileNotFoundException e){
            Log.e("Database", "File not found");
            e.printStackTrace();
        }catch (IOException e){
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }

    public void closeDatebase(){
        this.db.close();
    }

}