package com.ben.binderclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 客户端发送基本数据
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LXP_MainActivity";

    private static final int CODE = 1980;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendData = findViewById(R.id.send_data);
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.ben.binderserver",
                        "com.ben.binderserver.RemoteService"));
                bindService(intent, conn, BIND_AUTO_CREATE);
            }
        });
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() name = " + name + ", service = " + service);
            if(service != null) {
                //发送的数据data，返回值reply
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                //客户端按照这个顺序写出，服务端必须按照这个顺序读入
                data.writeInt(2023);
                data.writeString("Hello I'm from client!");
                try {
                    service.transact(CODE, data, reply, 0);
                    //transact执行之后拿到返回值returnMsg
                    String returnMsg = reply.readString();
                    Log.d(TAG, "returnMsg = " + returnMsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected() name = " + name);
        }
    };
}