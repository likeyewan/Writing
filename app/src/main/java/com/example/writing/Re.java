package com.example.writing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Re extends Activity implements SeekBar.OnSeekBarChangeListener {
    public String tt="";
    List<List<Test>> lists=new ArrayList<>();

    List<WordPoint>itP=new ArrayList<>();
    List<List<WordPoint>>itP1=new ArrayList<List<WordPoint>>();

    List<Integer>itC=new ArrayList<>();
    List<List<Integer>>itC1=new ArrayList<>();

    List<List<List<WordPoint>>>  listAllPoint=new ArrayList<List<List<WordPoint>>>();
    List<List<List<Integer>>>listAllColor=new ArrayList<>();
    private SeekBar seekBar;
    private Button btnShare,btn_p,btn_n,del;
    private Button btJ;
    private boolean tuo=false;
    private ImageView start_end;
    private boolean ON = false;
    private boolean w=false;
    private boolean ws=false,ws1=false;
    TextView t_pro,t_end;
    private SharedPreferences sp;
    private String account;
    private String password;
    private String filename;
    private int jia=50;
    ListView listFont;
    ReView fvFont;
    GetData getData=new GetData();
    private boolean jd=false;
    ProgressBarAsyncTask progressBarAsyncTask;
    String path="";
    String filepath="";
    public void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.reback);
        Intent intent = getIntent();
        String action = intent.getAction();
        if (intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            //这里是一个把uri转成String类型的路径的工具类
            path = JxdUtils.getPath(Re.this, uri);
            //Log.d("ss","p"+path);
        }
        //标题框
        listFont=(ListView)findViewById(R.id.listFont1);
        fvFont=(ReView)findViewById(R.id.fvFont);
        t_pro=(TextView)findViewById(R.id.t_pro);
        t_end=(TextView)findViewById(R.id.t_all);
        btnShare=(Button)findViewById(R.id.btnshare);
        btJ=(Button)findViewById(R.id.jia);
        btn_p=(Button)findViewById(R.id.btn_p);
        btn_n=(Button)findViewById(R.id.btn_n);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        start_end = (ImageView) findViewById(R.id.s_e);
        del=(Button)findViewById(R.id.del);

        start_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!ON) {
                    if(ws1){
                        ws1=false;
                    }else{
                        if(ws) {
                            flag=0;
                            itemFlag=0;
                            ReWrite();
                        }
                        if(jd||flag1==0) {
                            ReWrite();
                            jd=false;
                        }
                        if(tuo){
                            ReWrite();
                        }
                        ReWrite();
                    }
                    tuo=false;
                    start_end.setImageResource(R.drawable.tingzhi);
                    isPause=false;
                    progressBarAsyncTask = new ProgressBarAsyncTask(listAllPoint,listAllColor);
                    progressBarAsyncTask.execute();
                    ON = true;
                }else if(ON){
                    isPause=true;
                    ws1=true;
                    start_end.setImageResource(R.drawable.kaishi);
                    ON = false;
                }
            }
        });
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        account = sp.getString("USER_NAME", "");
        password =sp.getString("PASSWORD", "");
        filename=sp.getString("FILENAME","");
        filepath=sp.getString("Path","");
        if(filepath!="") {
            try {
                path = Environment.getExternalStorageDirectory()
                        .getCanonicalPath().toString()
                        + "/Writing/PointData/" + filepath;
            } catch (Exception e) {
            }
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Path", "");
        editor.apply();
        String ff="";
        if(account!="") {
            try {
                ff = Environment.getExternalStorageDirectory()
                        .getCanonicalPath().toString()
                        + "/Writing/PointData/" + account + "-" + password + "-" + filename + ".txt";
            } catch (Exception e) {
            }
        }
        String str=getData.readJsonFile(ff,path);
        Gson gson1=new Gson();
        final List<Han> listHan = gson1.fromJson(str, new TypeToken<List<Han>>(){}.getType());
        t_pro.setText("0");
        if(listHan!=null) {

            for (int i = 0; i < listHan.size(); i++) {

                listAllPoint.add(listHan.get(i).getLists());
                listAllColor.add(listHan.get(i).getColor());
                t_pro.setText("1");
            }
        }
      /*  try {
            tt= Environment.getExternalStorageDirectory()
                    .getCanonicalPath().toString()
                    + "/HandWriting/data/" +"点7.txt";
        }catch (Exception e){}
        GetData getData=new GetData();
        String tts=getData.readJsonFile(tt,"");
        //Log.d("dfs","tts="+tts);
        Gson gson6=new Gson();
        List<List<Test1>> wocw = gson6.fromJson(tts, new TypeToken<List<List<Test1>>>(){}.getType());
        Log.d("dfs","tw="+wocw.size());
        if(wocw!=null) {
            for (int i = 0; i < wocw.size(); i++) {
                List<WordPoint> it=new ArrayList<>();
                for(int j=0;j<wocw.get(i).size();j++) {
                    WordPoint wordPoint = new WordPoint();
                   float x=(float)wocw.get(i).get(j).getX()/5;
                    wordPoint.setX(x);
                    float y=(float)wocw.get(i).get(j).getY()/5;
                    wordPoint.setY(y);
                    wordPoint.setWidth(6);
                    it.add(wordPoint);
                    itC.add(-16777216);
                    //listAllColor.add(listHan.get(i).getColor());
                    //t_pro.setText("1");
                }
                itP1.add(it);
                itC1.add(itC);
                Log.d("FF", "FF" + itP1);
            }
            listAllPoint.add(itP1);
            listAllColor.add(itC1);

        }
       */
        initView(0);
        seekBar.setMax(listAllPoint.size()-1);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(this);

        t_end.setText(""+listAllPoint.size());
        fvFont.canDraw=false;
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = sp.getString("USER_NAME", "");
                password =sp.getString("PASSWORD", "");
                filename=sp.getString("FILENAME","");
                String ff=account+"-"+password+"-"+filename+".txt";
                try {
                    File file = new File(Environment.getExternalStorageDirectory()
                            .getCanonicalPath().toString()
                            + "/Writing/PointData"
                            + "/" + ff);
                    // Log.d("FF", "FF" + file);

                    shareFile(file, "*/*");
                } catch (Exception e) {
                }
            }
        });
        btJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btJ.getText().equals("加速")){
                    btJ.setText("恢复");
                    jia=25;
                    //Toast.makeText(Re.this, "加速不稳定，卡死我不管的哦！", Toast.LENGTH_SHORT).show();
                }else{
                    btJ.setText("加速");
                    jia=50;
                }
            }
        });
        if(flag1==0){btn_p.setEnabled(false);}
        btn_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag1==1){btn_p.setEnabled(false);}
                btn_n.setEnabled(true);
                if (flag1>0) {
                    ws1=false;
                    flag1 -= 1;
                    tuo=true;
                    flag=0;
                    itemFlag=0;
                    ReWrite();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    start_end.setImageResource(R.drawable.kaishi);
                    initView(flag1);
                    seekBar.setProgress(flag1);
                }
            }
        });
        if(flag1==listAllPoint.size()-1){btn_n.setEnabled(false);}
        else btn_n.setEnabled(true);
        btn_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_p.setEnabled(true);
                if(flag1==listAllPoint.size()-2)btn_n.setEnabled(false);
                if (flag1 < listAllPoint.size()-1) {
                    flag1 += 1;
                    ws1=false;
                    tuo=true;
                    flag=0;
                    itemFlag=0;
                    ReWrite();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    start_end.setImageResource(R.drawable.kaishi);
                    initView(flag1);
                    seekBar.setProgress(flag1);
                }
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listHan.size() > 1) {
                    ReWrite();
                    listHan.remove(flag1);
                    listAllPoint.clear();
                    listAllColor.clear();
                    t_end.setText("" + listHan.size());
                    seekBar.setMax(listHan.size() - 1);
                    if (listHan != null) {
                        for (int i = 0; i < listHan.size(); i++) {
                            listAllPoint.add(listHan.get(i).getLists());
                            listAllColor.add(listHan.get(i).getColor());
                            //t_pro.setText("1");
                        }
                    }
                    if (flag1 == listAllPoint.size() - 2) btn_n.setEnabled(false);
                    if (flag1 < listAllPoint.size() ) {
                        ws1 = false;
                        tuo = true;
                        flag = 0;
                        itemFlag = 0;
                        ReWrite();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        start_end.setImageResource(R.drawable.kaishi);
                        initView(flag1);
                        seekBar.setProgress(flag1);
                    }
                    try {
                        String s=Environment.getExternalStorageDirectory()
                                .getCanonicalPath().toString()
                                + "/Writing/PointData/" + account + "-" + password + "-" + filename + ".txt";
                        //文件夹不存在和传入的value值为1时，才允许进入创建
                        Gson gson = new Gson();
                        String str = gson.toJson(listHan);
                        File file = new File(s);
                        OutputStream out = new FileOutputStream(file);
                        out.write(str.getBytes());
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(Re.this,"这是我的底线，不能再删了！",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void initView(int item){
        Log.d("dfs","tw="+listAllPoint.get(item));
        ReWrite();
        if(listAllPoint.size()>0) {
            if(listAllPoint.size()==1)seekBar.setProgress(1);
            for (int i = 0; i < listAllPoint.get(item).size(); i++) {
                List<WordPoint> teampPoint = listAllPoint.get(item).get(i);
                List<Integer> teampColor = listAllColor.get(item).get(i);
                fvFont.mPointsB = new ArrayList<WordPoint>();
                fvFont.mColor = new ArrayList<>();
                for (int j = 0; j < teampPoint.size(); j++) {
                    fvFont.mColor.add(teampColor.get(j));
                    fvFont.mPointsB.add(teampPoint.get(j));
                    fvFont.postInvalidate();
                }
                fvFont.colorB.add(fvFont.mColor);
                fvFont.listPointB.add(fvFont.mPointsB);
                fvFont.mPointsB = new ArrayList<WordPoint>();
                fvFont.mColor = new ArrayList<>();
                fvFont.postInvalidate();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /** 分享指定（路径、类型）的文件 */
    public void shareFile( File file,String fileType)
    {

        if (file.exists())
        {
            Uri uri= FileProvider.getUriForFile(this,"com.example.writing.fileProvider", file);
            // Log.d("MM","uri="+uri);
            shareFile(this, uri, fileType);
        }
    }
    final static int SELECT = 1;	// 标记选取
    final static int SHARE = 2;		// 标记分享
    /** Activity执行结果 */
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == SELECT)
            {
                Uri uri = data.getData();
                String fileType = getType(uri);
                shareFile(this, uri, fileType);
                //Toast.makeText(this, "分享文件：" + uri.getPath().toString(), Toast.LENGTH_SHORT).show();
            }
            else if (requestCode == SHARE)
            {
                ShareSuccess();
            }
        }
    }
    /** 获取uri对应的文件类型 */
    private String getType(Uri uri)
    {
        String uriPath = uri.getPath().toString();	// "/document/image:57329"
        int start = uriPath.lastIndexOf("/");
        int end = uriPath.lastIndexOf(":");
        String type = "*/*";
        if (end > start)
        {
            type = uriPath.substring(start + 1, end) + "/*";
        }
        return type;
    }
    /** 分享完成逻辑 */
    public void ShareSuccess()
    {
        Toast.makeText(this, "分享完成！", Toast.LENGTH_SHORT).show();

    }
    /** 调用系統方法, 分享文件 */
    public static void shareFile(Activity context, Uri fileUri, String type)
    {
        if (fileUri != null)
        {
            // Log.d("MM","uri="+fileUri);
            Intent shareInt = new Intent(Intent.ACTION_SEND);
            shareInt.putExtra(Intent.EXTRA_STREAM, fileUri);
            //Toast.makeText(context, "分享文件类型：" + type, Toast.LENGTH_SHORT).show();
            shareInt.setType(type);			// 文件类型
            shareInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareInt.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(shareInt, "分享给"));
        }
        else
        {
            Toast.makeText(context, "分享文件不存在", Toast.LENGTH_SHORT).show();
        }
    }
    public int flag1=0;
    public int flag=0;
    public int itemFlag=0;
    public boolean isPause;
    public void ReWrite()
    {
        fvFont.listPointB.clear();
        fvFont.colorB.clear();
        fvFont.mPointsB.clear();
        fvFont.colorB=new ArrayList<List<Integer>>();
        fvFont.mPointsB=new ArrayList<>();
        fvFont.listPointB=new ArrayList<List<WordPoint>>();
//        fvFont.invalidate();
        if (fvFont.cacheCanvas != null) {
            //方法一
            fvFont.cacheCanvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
           // fvFont.invalidate();
        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        t_pro.setText(""+(progress+1));
        flag1=progress;
        tuo=true;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //jd=true;
        tuo=true;
        flag=0;
        itemFlag=0;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //jd=true;
        ws1=false;
        if(flag1==0)btn_p.setEnabled(false);
        else btn_p.setEnabled(true);
        if(flag1==listAllPoint.size()-1)btn_n.setEnabled(false);
        else btn_n.setEnabled(true);
        tuo=true;
        start_end.setImageResource(R.drawable.kaishi);
        ReWrite();
        if(flag1<=listAllPoint.size()-1) {
            initView(flag1);
        }


    }
    public class ProgressBarAsyncTask extends AsyncTask<Void, Integer, Void> {
        List<List<List<WordPoint>>> points;
        List<List<List<Integer>>> colors;
        public ProgressBarAsyncTask(List<List<List<WordPoint>>> points,List<List<List<Integer>>> colors)
        {
            this.points=points;
            this.colors=colors;
        }
        /**
         * 这里的Integer参数对应AsyncTask中的第一个参数
         * 这里的String返回值对应AsyncTask的第三个参数
         * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
         * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
         */
        @Override
        protected Void doInBackground(Void... params) {

               // Log.d("ss","a="+flag1);
                for (int i = itemFlag; i < points.get(flag1).size(); i++) {
                  //  Log.d("ss","a1="+itemFlag);
                    List<WordPoint> teampPoint = points.get(flag1).get(i);
                    List<Integer> teampColor=colors.get(flag1).get(i);
                    fvFont.mPointsB = new ArrayList<WordPoint>();
                    fvFont.mPointsB=new ArrayList<>();
                    for (int j = flag; j < teampPoint.size(); j++) {
                        if (isPause) {
                            fvFont.colorB.add(fvFont.mColor);
                            fvFont.listPointB.add(fvFont.mPointsB);
                            itemFlag=i;
                            return null;
                        }
                            if (tuo) {
                                tuo = false;
                                itemFlag = 0;
                                flag = 0;
                                return null;
                            }
                        fvFont.mColor.add(teampColor.get(j));
                        fvFont.mPointsB.add(teampPoint.get(j));
                        fvFont.postInvalidate();
                        try {
                            Thread.sleep(jia);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        flag = j;
                    }
                    flag = 0;
                    fvFont.colorB.add(fvFont.mColor);
                    fvFont.listPointB.add(fvFont.mPointsB);
                    fvFont.mPointsB = new ArrayList<WordPoint>();
                    fvFont.mColor=new ArrayList<>();
                    fvFont.postInvalidate();
                }
                itemFlag=0;
            w=true;
            flag=0;
            itemFlag=0;
            return null;
        }
        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(Void result) {
            fvFont.canReDraw=true;
            if(w){
                w=false;
                start_end.setImageResource(R.drawable.chongbo);
                ON = false;
            }else {
                start_end.setImageResource(R.drawable.kaishi);
                ON = false;
            }
        }

        //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPreExecute() {
            fvFont.canReDraw=false;
            fvFont.mColor=new ArrayList<>();
            fvFont.mPointsB=new ArrayList<WordPoint>();
        }
        /**
         * 这里的Intege参数对应AsyncTask中的第二个参数
         * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
         * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            fvFont.invalidate();

        }
    }
    @Override
    protected void onDestroy() {
        tuo=true;
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
        isPause=true;
    }
}
