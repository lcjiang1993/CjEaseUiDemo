package com.lcjiang.im.cjeaseuidemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

public class ChatListActivity extends AppCompatActivity {

    private EditText name;
    private EaseConversationListFragment conversationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        name = findViewById(R.id.chat_name);
        conversationFragment = new EaseConversationListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.chat_list,conversationFragment).commit();
        conversationFragment.setConversationListItemClickListener(conversation ->
                startActivity(new Intent(ChatListActivity.this, ChatActivity.class)
                        .putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId()))
        );

        CjImHelper.getInstance().setConvListener(conversationFragment.getConvListener());

    }

    public void goChat(View view) {
        if (TextUtils.isEmpty(name.getText()))
            return;

        startActivity(new Intent(ChatListActivity.this, ChatActivity.class)
                .putExtra(EaseConstant.EXTRA_USER_ID,name.getText().toString().trim()));
    }

}
