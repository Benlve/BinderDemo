package com.ben.binderserver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 服务端接受来自客户端的数据，并返回一个结果
 */
public class RemoteService extends Service {

    private static final String TAG = "LXP_RemoteService";

    public RemoteService() {
        Log.d(TAG, "MyBinder()");
    }

    private static final int CODE = 1980;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() intent = " + intent);
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind() intent = " + intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data,
                                     @Nullable Parcel reply, int flags) throws RemoteException {
            if (code == CODE) {
                //按照客户端写入的顺序读取
                int dataInt = data.readInt();
                String dataStr = data.readString();
                Log.d(TAG, "dataInt = " + dataInt + ", dataStr = " + dataStr);
                //将返回值写出
                reply.writeString("我是服务端，已收到来自你的数据~");
                return true;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}