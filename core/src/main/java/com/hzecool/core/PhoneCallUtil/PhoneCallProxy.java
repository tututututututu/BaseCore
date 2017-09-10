package com.hzecool.core.PhoneCallUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import com.hzecool.core.common.utils.ResourceUtils;
import com.hzecool.core.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

/**
 * Created by wangzhiguo on 2017/8/9
 */
public class PhoneCallProxy implements IPhoneCall {

    private Activity mActivity;

    public PhoneCallProxy(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void call(String phoneNumber, IPhoneCallBack callBack) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                callBack.onPhoneCallBack(ResourceUtils.getResources().getString(R.string.no_call_permission));
                return;
            }
        }

        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.request(Manifest.permission.CALL_PHONE)
                .subscribe(granted -> {
                    if (granted) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        mActivity.startActivity(intent);
                    } else {
                        callBack.onPhoneCallBack(ResourceUtils.getResources().getString(R.string.no_call_permission));
                    }
                });
    }

    @Override
    public void sendMessage(List<String> phoneNumbers, String content, IPhoneCallBack callBack) {

        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.request(Manifest.permission.SEND_SMS)
                .subscribe(granted -> {
                    if (granted) {
                        List<String> numList = phoneNumbers;
                        if (numList.isEmpty() || numList.size() == 0) {
                            callBack.onPhoneCallBack(ResourceUtils.getString(R.string.null_phone_number));
                            return;
                        }
                        String toNumbers = "";
                        for (String s : numList) {
                            toNumbers = toNumbers + s + ";";
                        }
                        toNumbers = toNumbers.substring(0, toNumbers.length() - 1);
                        if (toNumbers == null || toNumbers.matches("[;]+") || content == null) {
                            callBack.onPhoneCallBack(ResourceUtils.getString(R.string.invalid_num));
                            return;
                        } else {
                            Uri smsToUri = Uri.parse("smsto:" + toNumbers);
                            Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                            intent.putExtra("sms_body", content);
                            mActivity.startActivity(intent);
                        }
                    } else {
                        callBack.onPhoneCallBack(ResourceUtils.getString(R.string.no_msg_permission));
                    }
                });
    }
}
