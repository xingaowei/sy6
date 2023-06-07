package com.tomorrowlo.exp6client;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    // 定义界面上的两个文本框
    EditText input, show;
    // 定义界面上的一个按钮
    Button send;
    OutputStream os;
    private Socket s;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            // 如果消息来自于子线程
            if (msg.what == 0x123){
                // 将读取的内容追加显示在文本框中
                show.append("\n" + msg.obj.toString());
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText) findViewById(R.id.sendText);
        send = (Button) findViewById(R.id.button);
        show = (EditText) findViewById(R.id.receiveText);

        initSocket();

        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(){
                    @Override
                    public void run(){
                        try{
                            // 将用户在文本框内输入的内容写入网络
                            os.write((input.getText().toString()+"\r\n").getBytes("utf-8"));
                            // 清空input文本框
                            input.setText("");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

    }

    private void initSocket(){
        new Thread(){
            @Override
            public void run(){
                try{
                    s = new Socket("192.168.101.229",5000);

                    new Thread(new ClientThread(s,handler)).start();
                    os = s.getOutputStream();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }


}
