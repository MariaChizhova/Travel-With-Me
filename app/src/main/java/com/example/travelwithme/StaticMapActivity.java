package com.example.travelwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.travelwithme.pojo.Post;

import java.util.Collections;

public class StaticMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_map);
        byte[] data = getIntent().getExtras().getByteArray("data");
        Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);
        ImageView image = findViewById(R.id.imageView);
        image.setImageBitmap(bmp);

        MapData mapData = getIntent().getParcelableExtra("mapData");

        final EditText postDescriptipn = findViewById(R.id.ed_post_description);

        final Button post = findViewById(R.id.b_post);
        post.setOnClickListener(v -> {
            MainProfileFragment.postAdapter.setItems(Collections.singletonList(
                    new Post(MainProfileFragment.getUser(), 1L, "Thu Apr 1 07:31:08 +0000 2021", postDescriptipn.getText().toString(),
                            0L, 0L, bmp, mapData)));
            finish();
        });
    }
}