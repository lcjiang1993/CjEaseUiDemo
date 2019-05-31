package com.lcjiang.im.cjeaseuidemo;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hyphenate.EMConversationListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;

import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;

public class CjImHelper {

    private final String LOG_TAG = getClass().getSimpleName();

    private Context mContext;

    private EaseUI easeUI;

    public boolean isVideoCalling;
    public boolean isVoiceCalling;

    // 记录是否已经初始化
    private boolean isInit = false;

    //监听
    private EMConversationListener convListener;

    public void setConvListener(EMConversationListener convListener) {
        this.convListener = convListener;
    }

    private static CjImHelper mInstance;

    private CjImHelper() {

    }

    public synchronized static CjImHelper getInstance() {
        if (mInstance == null) {
            mInstance = new CjImHelper();
        }
        return mInstance;
    }

    /**
     * init helper
     *
     * @param context
     *            application context
     */
    public void init(Context context) {

        this.mContext =context;
        initEasemob();
    }

    private void initEasemob(){
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            Log.e(LOG_TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        if(isInit){
            return ;
        }

        /**
         * SDK初始化的一些配置
         * 关于 EMOptions 可以参考官方的 API 文档
         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
         */
        EMOptions options = new EMOptions();

        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，TODO 这个暂时有bug，上层收不到发送回执
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
        options.setAutoTransferMessageAttachments(true);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);

        // 调用初始化方法初始化sdk
        EaseUI.getInstance().init(mContext, options);

        easeUI = EaseUI.getInstance();

        setEaseUIProviders();
        // 设置开启debug模式
        EMClient.getInstance().setDebugMode(true);

        registerMessageListener();
        // 设置初始化已经完成
        isInit = true;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = mContext.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return null;
    }

    protected void setEaseUIProviders() {
        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(UserCacheManager::getEaseUser);

        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, mContext.getApplicationContext());
                if(message.getType() == EMMessage.Type.TXT){
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if(user != null){
                    if(EaseAtMessageHelper.get().isAtMeMsg(message)){
                        return String.format("%s在群聊中@了你", user.getNickname());
                    }
                    return user.getNickname() + ": " + ticker;
                }else{
                    if(EaseAtMessageHelper.get().isAtMeMsg(message)){
                        return String.format("%s在群聊中@了你", message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(mContext,ChatActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                // open calling activity if there is call
                if(isVideoCalling){
//                    intent = new Intent(appContext, VideoCallActivity.class);
                }else if(isVoiceCalling){
//                    intent = new Intent(appContext, VoiceCallActivity.class);
                }else{
                    EMMessage.ChatType chatType = message.getChatType();
                    if (chatType == EMMessage.ChatType.Chat) { // single chat message
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                    } else { // group chat message
                        // message.getTo() is the group id
                        intent.putExtra("userId", message.getTo());
                        if(chatType == EMMessage.ChatType.GroupChat){
                            intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                        }else{
                            intent.putExtra("chatType", EaseConstant.CHATTYPE_CHATROOM);
                        }

                    }
                }
                return intent;
            }
        });


    }

    EMMessageListener messageListener;
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(LOG_TAG, "onMessageReceived id : " + message.getMsgId());
                    //接收并处理扩展消息
                    String userName = message.getStringAttribute(UserCacheManager.kChatUserNick, "");
                    String userId = message.getStringAttribute(UserCacheManager.kChatUserId, "");
                    String userPic = message.getStringAttribute(UserCacheManager.kChatUserPic, "");

                    String hxIdFrom = message.getFrom();
                    EaseUser easeUser = new EaseUser(hxIdFrom);
                    easeUser.setAvatar(userPic);
                    easeUser.setNickname(userName);

                    UserCacheManager.save(hxIdFrom,userName,userPic);
                    easeUI.getNotifier().notify(message);

                }

                if (convListener!=null)
                    convListener.onCoversationUpdate();

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(LOG_TAG, "receive command message");
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

}
