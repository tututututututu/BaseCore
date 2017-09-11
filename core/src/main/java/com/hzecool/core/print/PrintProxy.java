package com.hzecool.core.print;

/**
 * Created by wangzhiguo on 2017/8/3
 */
public class PrintProxy implements IPrint {

    private IPrint mIPrint;

    public PrintProxy(IPrint iPrint) {
        mIPrint = iPrint;
    }

    @Override
    public void print(String printData, PrintService.PrintResutlCallBack printResutlCallBack) {
        mIPrint.print(printData, printResutlCallBack);
    }
}
