package com.example.zcc.zxuc;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.util.Log;

import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.os.Build.VERSION.SDK;
import static com.example.zcc.zxuc.NatureView.nParams;
import static com.example.zcc.zxuc.NatureView.natureFloatLayout;
import static com.example.zcc.zxuc.PetView.mFloatLayout;
import static com.example.zcc.zxuc.PetView.wmParams;
import static com.example.zcc.zxuc.ShopView.sParams;
import static com.example.zcc.zxuc.ShopView.shopFloatLayout;
import static com.example.zcc.zxuc.TestView.tParams;
import static com.example.zcc.zxuc.TestView.testFloatLayout;


public class FxService extends Service {

    private static final int NOTIFICATION_ID=100;//通知栏id
    public static WindowManager mWindowMangager;
    public static LayoutInflater inflater;
    public static int statusBarHeight;//该手机状态栏的高度
    public static int screenHight;//该手机屏幕高度
    public static int screenWidth;//该手机屏幕宽度
    private static final String TAG="FxService";
    public static boolean permit;

    public static Timer theTimer;//计时器 计算宠物的生长时间
    public static boolean clickBoolean;//避免快速双击

    public static int onView;//从1开始 不同数字代表正在显示不同的界面；

    public static int status;//宠物目前的状态 0代表空闲 1代表学习 2代表打工 3代表探险

    public static SQLiteDatabase dbRead;
    public static SQLiteDatabase dbWrite;
    public static SQLiteDatabase dbStatement;
    public static SQLiteDatabase dbDictionatry;
    public DbHelper helper;

    public static Notification notification;

    public static PetView petView;//宠物界面对象
    public static XiaoQiPaoView xiaoQiPaoView;//小气泡界面对象
    public static QiPaoView qiPaoView;//大气泡界面对象
    public static DictionaryView dictionaryView;//快捷词典界面对象
    public static NatureView natureView;//状态栏界面对象
    public static MenuView menuView;//菜单界面对象
    public static TestView testView;//测试界面对象
    public static TextbookView textbookView;//记事本对象
    public static ShopView shopView;//商店对象
    public static StudyView studyView;//学习界面对象
    public static WorkView workView;//打工界面对象
    public static AdventureView adventureView;//探险界面对象


    public static Handler handler=null;//接受控制生长的*子线程*发送的message 并更新主线程FxService的UI界面

    public static Runnable runnableSlowLoad=new Runnable() {
        @Override
        public void run() {
            mWindowMangager.updateViewLayout(natureFloatLayout,nParams);//刷新
        }
    };
    public static Runnable runnablePlayBalloon=new Runnable() {
        @Override
        public void run() {
            mWindowMangager.updateViewLayout(mFloatLayout,wmParams);
        }
    };
    public static Runnable runnableSlowMenu=new Runnable() {
        @Override
        public void run() {
            mWindowMangager.updateViewLayout(testFloatLayout,tParams);
        }
    };
    public static Runnable runnableShop=new Runnable() {
        @Override
        public void run() {
            mWindowMangager.updateViewLayout(shopFloatLayout,sParams);
        }
    };

    public static Runnable wakeRunnable=new Runnable() {
        @Override
        public void run() {
            petView.wake();
        }
    };

    public static Runnable actionRunnable=new Runnable() {
        @Override
        public void run() {
            int a=new Random().nextInt(5);//0,1,2,3,4
            switch(a) {
                case 0:petView.playWave();break;
                case 1:petView.blink();break;
                case 2:petView.playing();break;
                case 3:petView.sleep();break;
                case 4:petView.wake();break;
            }
        }
    };
    public static Runnable fallRunnable=new Runnable() {
        @Override
        public void run() {
            petView.petImage.setImageResource(R.drawable.blinknoblink);
        }
    };

    public static Runnable sleepRunnable=new Runnable() {
        @Override
        public void run() {
            petView.sleep();
        }
    };

    public static Runnable studyRunnable=new Runnable() {
        @Override
        public void run() {

            int a=0;//随机增加0-1点智力
            switch (studyView.studyTime){
                case 30:a=new Random().nextInt(2);break;
                case 60:a=new Random().nextInt(2)+1;break;
            }
            dbWrite.execSQL("update user set value = ? where name = 'intelligence'",new Object[]{getValue("intelligence")+a});
            intelligencejudge();
            qiPaoView.textView1.setText(""+getValuebyid(9)+"通过学习增加了"+a+"点智力值！");
            qiPaoView.drawQiPao();
        }
    };
    public static Runnable workRunnable=new Runnable() {
        @Override
        public void run() {

            int a=5*getValue("intelligence");
            switch (workView.workTime){
                case 30:a=a*1;break;
                case 60:a=2*a;break;
            }
            dbWrite.execSQL("update user set value = ? where name = 'money'",new Object[]{getValue("money")+a});
            qiPaoView.textView1.setText(""+getValuebyid(9)+"打工赚得"+a+"铜币！");
            qiPaoView.drawQiPao();
        }
    };

    public static Runnable adventureRunnable=new Runnable() {
        @Override
        public void run() {
            adventure(adventureView.adventureType);
        }
    };

    /**************************************************************************************************************************/
    @Override
    public void onCreate() {
        mWindowMangager=(WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);//*****获取的是WindowManagerImpl.CompatModeWrapper而不是localWindowManager
        inflater=LayoutInflater.from(getApplication());//加载布局
        statusBarHeight=getStatusBarHeight();//状态栏高度
        permit=true;
        handler=new Handler();
        getScreenSize();

        super.onCreate();//父类的方法
        onView=0;//刚开始只有宠物界面

        loadDb();
        brand();
        petView =new PetView();
        xiaoQiPaoView=new XiaoQiPaoView();
        qiPaoView=new QiPaoView();
        dictionaryView=new DictionaryView();
        natureView=new NatureView();
        menuView=new MenuView();
        testView=new TestView();
        textbookView=new TextbookView();
        shopView=new ShopView();
        studyView=new StudyView();
        workView=new WorkView();
        adventureView=new AdventureView();

        creatNotification();//创建并加载通知栏

        try {
            mWindowMangager.addView(mFloatLayout, wmParams);//加载宠物界面

            timetask();
            if(getValue("haveit")==0) {
                dbWrite.execSQL("update user set value = 1 where name = 'haveit'");
            }
        }catch(android.view.WindowManager.BadTokenException e){
            Toast.makeText(this,"请开启悬浮窗权限",Toast.LENGTH_SHORT).show();
            Log.i("asdfaa","请开启悬浮窗权限");
            permit=false;
            Intent intent=new Intent(this,FxService.class);
            stopService(intent);
        }

    }

    public FxService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    @Override
    public void onDestroy(){

        super.onDestroy();
        if(permit) {
            theTimer.cancel();
            dbDictionatry.close();
            dbStatement.close();
            dbRead.close();
            dbWrite.close();
        }
        if(permit==true){
            closeView();
        }

    }

    private void creatNotification(){

        if(Build.VERSION.SDK_INT>=16) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.appicon);
            builder.setContentTitle("【中兴精灵】");
            builder.setContentText("服务正在运行");
            notification = builder.build();
            startForeground(NOTIFICATION_ID, notification);
        }

    }//加载并创建通知栏

/*************************工具类方法********************************************/
    private int getStatusBarHeight() {//获取状态栏高度
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }//获取状态栏高度

    private void getScreenSize(){
        DisplayMetrics dm =getResources().getDisplayMetrics();
        screenHight=dm.heightPixels;
        screenWidth=dm.widthPixels;
    }//获取屏幕的宽和高(适配不同分辨率)
/*************************工具类方法********************************************/



/************************数据库***************************/
    public static int getValue(String name){
        Cursor cursor = dbRead.query("user", new String[]{"name","value"}, "name=?", new String[]{name}, null, null, null, null);
        int oldValue=999;
        while(cursor.moveToNext()) {
            oldValue=cursor.getInt(cursor.getColumnIndex("value"));
        }
        cursor.close();
        return oldValue;
    }//取得name的value(int)值

    public static String getValuebyid(int id){
        Cursor cursor = dbRead.query("user", new String[]{"id","name"}, "id=?", new String[]{Integer.toString(id)}, null, null, null, null);
        String oldValue="error";
        while(cursor.moveToNext()) {
            oldValue=cursor.getString(cursor.getColumnIndex("name"));
        }
        cursor.close();
        return oldValue;
    }//取得name的value(int)值

    public static String getStatement(int id){
        Cursor cursor = dbStatement.query("afdg", new String[]{"id","statement"}, "id=?", new String[]{String.valueOf(id)}, null, null, null, null);
        String oldValue="ERROR!";
        while(cursor.moveToNext()) {
            oldValue=cursor.getString(cursor.getColumnIndex("statement"));
        }
        cursor.close();
        return oldValue;
    }//取得任意一句话

    public static String getTranslation(String english){
        String oldValue="ERROR!";
        Cursor cursor = dbDictionatry.query("dictTB", new String[]{"en","cn"}, "en=?", new String[]{english}, null, null, null, null);
        while(cursor.moveToNext()) {
            oldValue=cursor.getString(cursor.getColumnIndex("cn"));
        }
        oldValue=oldValue.replace("\\n","\n");
        cursor.close();
        return oldValue;

    }

    private void loadDb(){

        helper=new DbHelper(FxService.this);
        dbRead=helper.getReadableDatabase();//如果没有数据库 会新建一个
        dbWrite=helper.getWritableDatabase();

        DBManager dbManager=new DBManager(this);
        dbManager.openDateBase();
        dbManager.closeDatebase();
        dbStatement=SQLiteDatabase.openOrCreateDatabase(DBManager.STATEMENT_DB_PATH+"/"+DBManager.STATEMENT_DB_NAME,null);

        DictionaryManager dbManager2=new DictionaryManager(this);
        dbManager2.openDateBase();
        dbManager2.closeDatebase();
        dbDictionatry=SQLiteDatabase.openOrCreateDatabase(DictionaryManager.DICTIONARY_DB_PATH+"/"+ DictionaryManager.DICTIONARY_DB_NAME,null);



    }//加载所有数据库
/************************数据库***************************/

    private boolean brand(){
        String phoneName= Build.MODEL;
        String phoneName2=Build.BRAND;
        Log.i("safasf","手机品牌"+phoneName+phoneName2);
        return false;
    }//获取sdk以及手机品牌

    public void timetask(){
        theTimer=new Timer(true);
        TimerTask taskGrow=new TimerTask() {
            @Override
            public void run() {
                if(getValue("book3")==0) {
                    dbWrite.execSQL("update user set value = ? where name = 'growNumber'", new Object[]{getValue("growNumber") + getValue("mood") + 1});
                }
                else dbWrite.execSQL("update user set value = ? where name = 'growNumber'", new Object[]{(int)(1.3*(getValue("growNumber") + getValue("mood") + 1))});//1.3倍生长速度
                dbWrite.execSQL("update user set value = ? where name = 'health'",new Object[]{getValue("health")+getValue("force")/10+1});//健康值缓慢恢复
                healthjudge();
                growNumberjudge();

            }
        };//注意 这是另一个线程了 注意！注意！这是另一个线程！

        TimerTask taskAction=new TimerTask() {
            @Override
            public void run() {
                handler.post(actionRunnable);
                //petView.playing();
                //petView.blink();
                //petView.playWave();
            }
        };//玩气球

        TimerTask taskSleep=new TimerTask() {
            @Override
            public void run() {
               handler.post(sleepRunnable);
            }
        };//睡觉(睡觉时不做其他动作)

        TimerTask taskWake=new TimerTask() {
            @Override
            public void run() {
                handler.post(wakeRunnable);
            }
        };

        theTimer.schedule(taskGrow,0,120000);//120s增加一次生长值
        //theTimer.schedule(taskWake,0,120003);//也就是说 宠物最多睡60s就醒了
        //theTimer.schedule(taskSleep,30000,60001);//最多60s就睡了
        theTimer.schedule(taskAction,3000,60000);//40s做一次动作
    }//Timer

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static void adventure(int i){
        switch (i){
            case 1://森林
                int randnum=new Random().nextInt(4)+1;//随机1,2,3,4
                switch (randnum){
                    case 1:
                        dbWrite.execSQL("update user set value = ? where name = 'money'",new Object[]{getValue("money")+getValue("force")*10});
                        qiPaoView.textView1.setText(""+getValuebyid(9)+"在森林中打猎获得了"+getValue("force")*10+"铜币！");
                        break;
                    case 2:
                        dbWrite.execSQL("update user set value = ? where name = 'mood'",new Object[]{getValue("mood")+1});
                        moodjudge();
                        qiPaoView.textView1.setText(""+getValuebyid(9)+"在森林中转了一圈，心情甚好");
                        break;
                    case 3:
                        dbWrite.execSQL("update user set value = ? where name = 'health'",new Object[]{getValue("health")-10});
                        healthjudge();
                        dbWrite.execSQL("update user set value = ? where name = 'force'",new Object[]{getValue("force")+1});
                        forcejudge();
                        qiPaoView.textView1.setText(""+getValuebyid(9)+"在森林中遇到了老虎，受伤归来，强壮值提高了");
                        break;
                    case 4:
                        dbWrite.execSQL("update user set value = ? where name = 'force'",new Object[]{getValue("force")+2});
                        forcejudge();
                        qiPaoView.textView1.setText(""+getValuebyid(9)+"在森林中遇到了老虎，将其打伤，强壮值提高了");
                        break;
                }

                break;

            case 2://火山
                int randnum2=new Random().nextInt(3)+1;//随机1,2,3
                switch (randnum2){
                    case 1:
                        dbWrite.execSQL("update user set value = ? where name = 'money'",new Object[]{getValue("money")+getValue("force")*30});
                        qiPaoView.textView1.setText(""+getValuebyid(9)+"在火山口捡到了宝贝，买得了"+getValue("force")*30+"铜币！");
                        break;
                    case 2:
                        dbWrite.execSQL("update user set value = ? where name = 'mood'",new Object[]{getValue("mood")-1});
                        moodjudge();
                        qiPaoView.textView1.setText(""+getValuebyid(9)+"在火山口碰到了怪物，吓得不轻");
                        break;
                    case 3:
                        dbWrite.execSQL("update user set value = ? where name = 'health'",new Object[]{getValue("health")-10});
                        healthjudge();
                        qiPaoView.textView1.setText(""+getValuebyid(9)+"在火山口被岩浆所伤，受伤归来");
                        break;

                }

                break;

            case 3://海洋
                int randnum3=new Random().nextInt(4)+1;//随机1,2,3,4
                switch (randnum3){
                    case 1:
                        dbWrite.execSQL("update user set value = ? where name = 'money'",new Object[]{getValue("money")+getValue("force")*15});
                        qiPaoView.textView1.setText(""+getValuebyid(9)+"在海里捞鱼，卖得"+getValue("force")*15+"铜币！");
                        break;
                    case 2:
                        dbWrite.execSQL("update user set value = ? where name = 'money'",new Object[]{getValue("money")+500});
                        qiPaoView.textView1.setText(""+getValuebyid(9)+"在海底发现了珍珠，卖得500铜币");
                        break;
                    case 3:
                        dbWrite.execSQL("update user set value = ? where name = 'health'",new Object[]{getValue("health")-10});
                        healthjudge();
                        qiPaoView.textView1.setText(""+getValuebyid(9)+"在海底遇到了怪物，受伤归来");
                        break;
                    case 4:
                        dbWrite.execSQL("update user set value = ? where name = 'force'",new Object[]{getValue("force")+2});
                        forcejudge();
                        qiPaoView.textView1.setText(""+getValuebyid(9)+"在海底遇到了怪物，将其打伤，强壮值提高了");
                        break;
                }

                break;
        }

        qiPaoView.drawQiPao();

    }

    public static void moodjudge(){
        if(getValue("mood")<=0){
            dbWrite.execSQL("update user set value = 0 where name = 'mood'");
        }
        else if(getValue("mood")>=10){
            dbWrite.execSQL("update user set value = 10 where name = 'mood'");
        }
    }//判断mood的越界
    public static void healthjudge(){
        if(getValue("health")<=0){
            dbWrite.execSQL("update user set value = 0 where name = 'health'");
        }
        else if(getValue("health")>=100){
            dbWrite.execSQL("update user set value = 100 where name = 'health'");
        }
    }//判断health的越界
    public static void intelligencejudge(){
        if(getValue("intelligence")>=100){
            dbWrite.execSQL("update user set value = 100 where name = 'intelligence'");
        }
    }//判断intelligence的越界
    public static void forcejudge(){
        if(getValue("force")>=100){
            dbWrite.execSQL("update user set value = 100 where name = 'force'");
        }
    }//判断force的越界
    public static void growNumberjudge(){
        if(getValue("growNumber")>=getValue("rank")*20){
            dbWrite.execSQL("update user set value = ? where name = 'rank'",new Object[]{getValue("rank")+1});
            dbWrite.execSQL("update user set value = 0 where name = 'growNumber'");
        }
    }//判断growNumber的越界

    public static boolean moneyjudge(int reduceMoney){
        if(getValue("money")>=reduceMoney){
            dbWrite.execSQL("update user set value = ? where name = 'money'",new Object[]{getValue("money")-reduceMoney});
            return true;
        }
        else return false;
    }//判断所剩的钱够不够支付，如果够的话执行扣钱返回true，否则不执行返回false

    public void closeView(){
        if(petView.falltimer!=null){
            petView.falltimer.cancel();
        }
        if(petView.playballoontimer!=null){
            petView.playballoontimer.cancel();
        }

        mWindowMangager.removeView(mFloatLayout);

        try{
            dictionaryView.removeDictionary();
        }
        catch(IllegalArgumentException e){}

        try{
            natureView.removeNatureView();
        }
        catch(IllegalArgumentException e){}

        try{
            testView.removeTestView();
        }
        catch(IllegalArgumentException e){}

        try{
            textbookView.removeTextbookView();
        }
        catch(IllegalArgumentException e){}


        try{
            shopView.removeShopView();
        }
        catch(IllegalArgumentException e){}

        try{
            studyView.removeStudyView();
        }
        catch(IllegalArgumentException e){}

        try{
            workView.removeWorkView();
        }
        catch(IllegalArgumentException e){}

        try{
            adventureView.removeAdventureView();
        }
        catch(IllegalArgumentException e){}

    }
}
