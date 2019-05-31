package com.lcjiang.im.cjeaseuidemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

public class MyChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {

    public MyChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setChatFragmentHelper(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        UserCacheManager.setMsgExt(message);
    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {
//        Toast.makeText(getActivity(),"头像被点击了",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }




}