package com.example.travelwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.travelwithme.pojo.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StaticMapActivity extends AppCompatActivity {

    private Date date;

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
            date = new Date();
            Post newPost = new Post(ProfileFragment.getUser().getId(), 1L, date.toString(), postDescriptipn.getText().toString(),
                    0L, bmp, mapData);
            ProfileFragment.postAdapter.setItems(Collections.singletonList(newPost));

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.8:9090")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            AddPostApi addPostApi = retrofit.create(AddPostApi.class);
            Call<BaseResponse> call = addPostApi.addPost(newPost);
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.isSuccessful()) {
                        Log.i("sucsess", "sucsess");
                    } else {
                        Log.i("eeeerrror", "error1");
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });


            finish();
        });
    }
}