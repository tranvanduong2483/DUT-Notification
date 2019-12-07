package com.duong.anyquestion.classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duong.anyquestion.R;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<Message> mMessageList;
    private SessionManager sessionManager;
    private Bitmap bitmap_you;
    private String id_user, id_expert;


    public MessageListAdapter(Context mContext, List<Message> messageList) {
        mMessageList = messageList;
        this.mContext = mContext;
        sessionManager = new SessionManager((Activity) mContext);
        // this.bitmap_you =  bitmap_you;
        me = sessionManager.getAccount();
    }

    public MessageListAdapter(Context mContext, List<Message> messageList, String id_user, String id_expert) {
        mMessageList = messageList;
        this.mContext = mContext;
        sessionManager = new SessionManager((Activity) mContext);
        // this.bitmap_you =  bitmap_you;
        me = sessionManager.getAccount();
        this.id_user = id_user;
        this.id_expert = id_expert;

        me = "user".equals(sessionManager.getType()) ? id_user : id_expert;

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    String me = "", you = "";
    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);
        if (message.getFrom().equals(me)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            you = message.getFrom();
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message =  mMessageList.get(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
        }
    }

}