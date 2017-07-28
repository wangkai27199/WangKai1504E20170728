package com.baway.wangkai;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.aspsine.irecyclerview.IRecyclerView;
import com.baway.wangkai.utils.CrashHandler;
import com.baway.wangkai.utils.NetUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 王锴
 * @Date 2017年7月28日14:51:24
 * 功能：操作主界面的类 用于显示数据
 */
public class MainActivity extends Activity implements MyAdapter.onItemClickListener{

    private IRecyclerView mainRecyclerView;
    private int count = 0;
    private MyAdapter adapter;
    String url = "http://www.yulin520.com/a2a/impressApi/news/mergeList?sign=C7548DE604BCB8A17592EFB9006F9265&pageSize=20&gender=2&ts=1871746850&page=1";
    private List<Bean.DataBean> list = new ArrayList<Bean.DataBean>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String result = (String) msg.obj;
                System.out.println("result = " + result);
                //打印JSON数据
                Logger.json(result);

                Gson gson = new Gson();
                Bean bean = gson.fromJson(result, Bean.class);
                List<Bean.DataBean> data = bean.data;

                list.addAll(data);
                if (list != null) {
                    //打印解析后的集合
                    for (int i = 0;i<list.size();i++){
                        Logger.d(list.get(i).toString());
                    }
                    adapter = new MyAdapter(MainActivity.this,list);
                    mainRecyclerView.setIAdapter(adapter);
                    adapter.setOnItemClickListener(MainActivity.this);
                }
            }
        }
    };
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化Logger
        Logger.addLogAdapter(new AndroidLogAdapter());
        CrashHandler.getInstance().init(MainActivity.this);
        //判断是否有网络
        boolean networkAvailable = NetUtils.isNetworkAvailable(MainActivity.this);
        if (networkAvailable){
            //加载数据
            initData();
            //加载控件
            initView();
            Toast.makeText(this, "您已连接网络", Toast.LENGTH_SHORT).show();
        }else{
            //没有网络时 跳转设置页面
            AlertDialog.Builder b=new AlertDialog.Builder(this).setTitle("没有可用的网络").setMessage("请开启GPRS或WIFI网路连接");
            b.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent mIntent=new Intent("android.settings.WIRELESS_SETTINGS");
                    startActivity(mIntent);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub dialog.cancel();
                }
            }).create(); b.show();
        }
    }

    private void initData() {
        //新建一条线程
        new Thread() {
            @Override
            public void run() {
                super.run();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    //Logger.d("response",response.body().string());
                    // System.out.println("response.body() " + response.body().string());
                    String str = response.body().string();
                    //发送到主线程
                    Message message = Message.obtain();
                    message.obj = str;
                    message.what = 0;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initView() {
        mainRecyclerView = (IRecyclerView) findViewById(R.id.main_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mainRecyclerView.setLayoutManager(linearLayoutManager);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0){
                    count += 1;
                    Toast.makeText(MainActivity.this, ""+count, Toast.LENGTH_SHORT).show();
                }else if (count == 1){
                    Toast.makeText(MainActivity.this, "发生了异常", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(CrashHandler.TAG + "自己抛出的异常");

                }
            }
        });

    }


    @Override
    public void onItemClickListener(View view, int position) {
        Toast.makeText(MainActivity.this, list.get(position).introduction, Toast.LENGTH_SHORT).show();

    }
}
