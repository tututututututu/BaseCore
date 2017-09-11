package com.hzecool.core.print;

import android.text.TextUtils;

import com.hzecool.core.R;
import com.hzecool.core.common.utils.HandlerUtil;
import com.hzecool.core.common.utils.ResourceUtils;
import com.hzecool.core.common.utils.ToastUtils;
import com.hzecool.core.log.L;
import com.hzecool.core.socket.TcpClient;
import com.hzecool.core.sp.FinalSPOperation;


/**
 * 打印
 * Created by song on 2017/5/10.
 */

public class PrintService implements IPrint {

    private void ShowToast(String content) {
        HandlerUtil.post(() -> ToastUtils.showLongToast(content));
    }

    @Override
    public void print(String printData, PrintResutlCallBack printResutlCallBack) {
        L.logFile("PrintService  调用打印 print() 方法" + " printData=" + printData);
        L.logStackTraceToFile(new Throwable());

        try {
            String printPoint = FinalSPOperation.getString("printPort", "22222");
            int port = Integer.parseInt(printPoint);
            String ip = FinalSPOperation.getString("printAddress", "");

            if (CheckSocketAddressUtils.checkAddress(port,ip)) {
                ShowToast(ResourceUtils.getString(R.string.print_port_ip_error));
                if (printResutlCallBack != null) {
                    printResutlCallBack.onFailed(ResourceUtils.getString(R.string.print_port_ip_error));
                }
                return;
            }

            printReal(printData,ip,port, printResutlCallBack);

        } catch (Exception e) {
            if (printResutlCallBack != null) {
                printResutlCallBack.onFailed(ResourceUtils.getString(R.string.link_print_faild));
            }
            return;
        }
    }

    private void printReal(String msg, String ip, int port, PrintResutlCallBack printResutlCallBack) {
        new Thread() {
            @Override
            public void run() {
                try {
                    L.logFile("PrintService  printReal()---> 打印数据:" + msg);
                    if (TextUtils.isEmpty(msg)) {
                        ShowToast(ResourceUtils.getString(R.string.empty_print_data));
                        return;
                    }

                    new TcpClient().go(ip, port, msg, new TcpClient.CallBack() {
                        @Override
                        public void onConnectError(String msg) {
                            ToastUtils.showShortToastSafe(ResourceUtils.getString(R.string.link_print_faild));
                            printResutlCallBack.onFailed(ResourceUtils.getString(R.string.link_print_faild));
                        }

                        @Override
                        public void onWriteError(String msg) {
                            ToastUtils.showShortToastSafe(ResourceUtils.getString(R.string.print_data_faild));
                            printResutlCallBack.onFailed(ResourceUtils.getString(R.string.print_data_faild));
                        }

                        @Override
                        public void onSuccess() {
                            printResutlCallBack.onSuccess();
                        }
                    });
                } catch (Exception e) {
                    ToastUtils.showShortToastSafe(ResourceUtils.getString(R.string.check_print_setting));
                    printResutlCallBack.onFailed(ResourceUtils.getString(R.string.check_print_setting));
                }
            }
        }.start();
    }

    public interface PrintResutlCallBack {
        void onSuccess();

        void onFailed(String msg);
    }
}
