package com.example.travelwithme;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelwithme.adapter.PostAdapter;
import com.example.travelwithme.pojo.Message;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "MessageViewHolder";

    TextView messageTextView;
    ImageView messageImageView;
    TextView messengerTextView;
    CircleImageView messengerImageView;

    public MessageViewHolder(View v) {
        super(v);
        messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
        messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
        messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
        messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bindMessage(Message message) {
        if (message.getText() != null) {
            messageTextView.setText(message.getText());
        //    messengerTextView.setText(message.getName());
            messageTextView.setVisibility(TextView.VISIBLE);
            messageImageView.setVisibility(ImageView.GONE);
        } else if (message.getImageUrl() != null) {
            String imageUrl = message.getImageUrl();
            if (imageUrl.startsWith("gs://")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            Glide
                                    .with(messageImageView.getContext())
                                    .load(downloadUrl)
                                    .into(messageImageView);
                        })
                        .addOnFailureListener(e -> Log.w(TAG, "Getting download url was not successful.", e));
            } else {
                Glide.with(messageImageView.getContext()).load(message.getImageUrl()).into(messageImageView);
            }
            messageImageView.setVisibility(ImageView.VISIBLE);
            messageTextView.setVisibility(TextView.GONE);
        }

        if (message.getPhotoUrl() != null) {
            Picasso.get().load(PostAdapter.S3IMAGES + message.getPhotoUrl()).into(messengerImageView);
        }
    }
}
