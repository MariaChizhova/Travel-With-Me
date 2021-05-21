package com.example.travelwithme.requests;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class PhotoCreateRequest {

    private final String photo;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public PhotoCreateRequest(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        image.recycle();
        this.photo = Base64.getEncoder().encodeToString(bytes);
    }


    public String getPhoto() {
        return photo;
    }
}
