package com.example.travelwithme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class SettingsProfileActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        profilePhoto = (ImageView) findViewById(R.id.profile_photo);
        EditText name = findViewById(R.id.name);
        EditText username = findViewById(R.id.username);
        EditText description = findViewById(R.id.description);
        EditText location = findViewById(R.id.location);

    }

    public void editPhoto(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void updateProfile(View view) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && null != data) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap chosenImage = BitmapFactory.decodeStream(inputStream);
                chosenImage = Bitmap.createScaledBitmap(chosenImage, 400, 400, false);
                profilePhoto.setImageBitmap(chosenImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}