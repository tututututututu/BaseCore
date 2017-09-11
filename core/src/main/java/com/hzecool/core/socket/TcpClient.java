package com.hzecool.core.socket;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hzecool.core.log.L;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.future.Cancellable;

/**
 * Created by tutu on 2017/6/14.
 */

public class TcpClient {
    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (cancellable != null) {
                L.iTag("TcpClient", "主动断开连接");
                cancellable.cancel();
                mCallBack.onConnectError("连接超时");
            }
        }
    };

    private Cancellable cancellable;
    private CallBack mCallBack;



    public void go(String ip, int port, String msg, CallBack callBack) {
        mCallBack = callBack;
        Looper.prepare();
        handler = new Handler();
        cancellable = AsyncServer.getDefault().connectSocket(ip, port, (ex, socket) -> {
            if (ex != null) {
                handler.removeCallbacks(runnable);
                mCallBack.onConnectError(ex.getMessage());
                return;
            }
            socket.setDataCallback((emitter, bb) -> L.iTag("TcpClient", new String(bb.getAllByteArray())));
            socket.setClosedCallback(ex1 -> {
                if (ex1 != null) {
                    L.iTag("TcpClient", "setClosedCallback出错");
                    return;
                }
                L.iTag("TcpClient", "setClosedCallback");
            });
            socket.setEndCallback(ex12 -> {
                if (ex12 != null) {
                    L.iTag("TcpClient", "setEndCallback出错");
                    return;
                }
                L.iTag("TcpClient", "setEndCallback");
            });
            socket.setWriteableCallback(() -> Log.d("TcpClient", "onWriteable"));


            byte[] dataContent = msg.getBytes();
            byte[] end = new byte[]{0x0a};
            byte[] start = new byte[]{0x0a};
            byte[] data = new byte[dataContent.length + start.length + end.length];

            try {
                System.arraycopy(start, 0, data, 0, start.length);
                System.arraycopy(dataContent, 0, data, start.length, dataContent.length);
                System.arraycopy(end, 0, data, start.length + dataContent.length, end.length);
            } catch (Exception e) {
                L.logFile(e.getMessage() + "\n" + e.getStackTrace().toString());
                data = dataContent;
            }

            L.iTag("TcpClient", data.toString());
            L.i("TcpClient", data);

            Util.writeAll(socket, data, ex13 -> {
                if (ex13 != null) {
                    L.iTag("TcpClient", "writeAll出错");
                    handler.removeCallbacks(runnable);
                    mCallBack.onWriteError(ex13.getMessage());
                    return;
                }
                handler.removeCallbacks(runnable);
                mCallBack.onSuccess();
                L.iTag("TcpClient", "writeAll");
            });
        });

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 5000);
        Looper.loop();
    }

    public interface CallBack {
        void onConnectError(String msg);

        void onWriteError(String msg);

        void onSuccess();
    }
}
