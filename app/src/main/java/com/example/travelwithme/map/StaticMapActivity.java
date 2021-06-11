package com.example.travelwithme.map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.travelwithme.Api;
import com.example.travelwithme.R;
import com.example.travelwithme.fragments.MainProfileFragment;
import com.example.travelwithme.map.MapData;
import com.example.travelwithme.pojo.Post;

import java.util.Collections;
import java.util.Date;

public class StaticMapActivity extends AppCompatActivity {

    private Date date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_map);
        byte[] data = getIntent().getExtras().getByteArray("data");
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        ImageView image = findViewById(R.id.imageView);
        image.setImageBitmap(bmp);

        MapData mapData = getIntent().getParcelableExtra("mapData");

        final EditText postDescriptipn = findViewById(R.id.ed_post_description);

        final Button post = findViewById(R.id.b_post);
        post.setOnClickListener(v -> {
            date = new Date();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final String email = preferences.getString("user_email", "");

            Api api = new Api();
            api.getUser(email, user -> {
                Post newPost = new Post(user.getUserID(), 1L, date.toString(), postDescriptipn.getText().toString(),
                        0L, bmp, mapData);
                api.addPost(newPost, id -> {
                    newPost.setId(id);
                    MainProfileFragment.postAdapter.setItems(Collections.singletonList(newPost));
                });
                finish();
            });

        });
    }
}