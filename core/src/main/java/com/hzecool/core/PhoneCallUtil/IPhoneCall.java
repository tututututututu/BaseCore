package com.hzecool.core.PhoneCallUtil;

import java.util.List;

/**
 * Created by wangzhiguo on 2017/8/9
 */
public interface IPhoneCall {
    void call(String phoneNumber, IPhoneCallBack callBack);
    void sendMessage(List<String> phoneNumbers, String content, IPhoneCallBack callBack);
}
