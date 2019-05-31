package com.hyphenate.easeui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
    @SuppressLint("CheckResult")
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	EaseUser user = getUserInfo(username);

        RequestOptions optionsCircle = new RequestOptions();
        DrawableTransitionOptions transitionOptions = new DrawableTransitionOptions();
        optionsCircle.placeholder(R.drawable.icon_head);
        optionsCircle.transform(new GlideCircleTransform());
        optionsCircle.diskCacheStrategy(DiskCacheStrategy.ALL);

        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context)
                        .load(avatarResId)
                        .apply(optionsCircle)
//                        .transition(transitionOptions.crossFade())
                        .into(imageView);

            } catch (Exception e) {

                Glide.with(context)
                        .load(user.getAvatar())
                        .apply(optionsCircle)
//                        .transition(transitionOptions.crossFade())
                        .into(imageView);
            }
        }else Glide.with(context)
                    .load(R.drawable.icon_head)
                    .apply(optionsCircle)
//                    .transition(transitionOptions.crossFade())
                    .into(imageView);

    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNickname() != null){
        		textView.setText(user.getNickname());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    
}
