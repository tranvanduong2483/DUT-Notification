package com.duong.anyquestion.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.duong.anyquestion.R;
import com.duong.anyquestion.Tool.ToolSupport;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SentMessageHolder extends RecyclerView.ViewHolder {
    TextView messageText, timeText, nameText;
    ImageView profileImage, iv_image, iv_image_loading;

    RelativeLayout rl_image;
    ProgressBar pb_loading_image;


    SentMessageHolder(View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.text_message_body);
        timeText =  itemView.findViewById(R.id.text_message_time);
        iv_image = itemView.findViewById(R.id.iv_image);
        //pb_loading_image = itemView.findViewById(R.id.pb_loading_image);
        iv_image_loading = itemView.findViewById(R.id.iv_image_loading);


        //nameText =  itemView.findViewById(R.id.text_message_name);
       // profileImage =  itemView.findViewById(R.id.image_message_profile);
    }

    void bind(Message message) {
        messageText.setText(message.getMessage());

        // Format the stored timestamp into a readable String using method.
        timeText.setText(message.getHourMin());


        if (message.isTypeImage()){
            iv_image.setVisibility(View.VISIBLE);
            messageText.setVisibility(View.GONE);
            Bitmap bitmap;
            if (!message.getMessage().isEmpty()) {
                byte[] bytes_image = Base64.decode(message.getMessage(), Base64.DEFAULT);
                bitmap = ToolSupport.convertByteArrayToBitmap(bytes_image);
            } else {
                bitmap = BitmapFactory.decodeResource(itemView.getResources(), R.drawable.error_image);
            }

            int p120dp = (int) itemView.getResources().getDimension(R.dimen.test_120dp_no_delete);
            bitmap = ToolSupport.resize(bitmap, p120dp, p120dp);
            iv_image.setImageBitmap(ToolSupport.BitmapWithRoundedCorners(bitmap));


            Drawable drawable = new BitmapDrawable(itemView.getResources(), bitmap);
            iv_image_loading.setBackground(drawable);

            if (message.isStatus()) {
                iv_image_loading.setVisibility(View.GONE);
                //pb_loading_image.setVisibility(View.GONE);
            } else {
                //pb_loading_image.setVisibility(View.VISIBLE);
                iv_image_loading.setVisibility(View.VISIBLE);
            }

        }else {
            iv_image.setVisibility(View.GONE);
            messageText.setVisibility(View.VISIBLE);
        }


        // Insert the profile image from the URL into the ImageView.
        //DateUtils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
    }
}
