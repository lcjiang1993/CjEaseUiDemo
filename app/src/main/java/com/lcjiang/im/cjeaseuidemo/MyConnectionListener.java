package com.lcjiang.im.cjeaseuidemo;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;

/**
 * 实现ConnectionListener接口-----监听网络状态---在另外一台手机登录----账号被移除
 * Created by Administrator on 2016/8/12.
 */
public class MyConnectionListener implements EMConnectionListener {
    Activity context;

    @Override
    public void onConnected() {
    }

    public MyConnectionListener(Activity context) {
        this.context = context;
    }

    @Override
    public void onDisconnected(final int error) {
        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (error == EMError.USER_REMOVED) {
                    // 显示帐号已经被移除
                    Toast.makeText(context, "帐号已经被移除", Toast.LENGTH_SHORT).show();
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    // 显示帐号在其他设备登录
                    Toast.makeText(context, "帐号在其他设备登录,被迫下线", Toast.LENGTH_SHORT).show();
                    EMClient.getInstance().logout(true, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Log.e("main", "下线成功了");
                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.e("main", "下线失败了！" + s);
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });//下线

                } else {
                    if (NetUtils.hasNetwork(context)) {
                     //连接不到聊天服务器
                        Toast.makeText(context, "连接不到聊天服务器", Toast.LENGTH_SHORT).show();
                    } else {
                        //当前网络不可用，请检查网络设置
                        Toast.makeText(context, "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
