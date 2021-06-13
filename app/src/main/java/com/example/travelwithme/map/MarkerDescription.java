package com.example.travelwithme.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adamstyrc.cookiecutter.ImageUtils;
import com.example.travelwithme.R;
import com.example.travelwithme.adapter.ImageAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MarkerDescription extends AppCompatDialogFragment {

    private static final int RESULT_LOAD_IMAGE = 1;
    View view;
    private RecyclerView imageRecyclerView;
    public static ImageAdapter imageAdapter;
    public String comments;
    public String name;
    public List<Bitmap> image;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.marker_description, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog alertDialog = builder.create();

        EditText placeName = view.findViewById(R.id.etPlaceName);
        placeName.setText(name);

        EditText editText = view.findViewById(R.id.etComments);
        editText.setText(comments);

        final Button loadPicture = view.findViewById(R.id.b_document_attach);
        loadPicture.setOnClickListener(v -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        });

        imageRecyclerView = view.findViewById(R.id.list_image);
        LinearLayoutManager l = new LinearLayoutManager(view.getContext());
        l.setOrientation(RecyclerView.HORIZONTAL);
        imageRecyclerView.setLayoutManager(l);
        imageAdapter = new ImageAdapter(this);
        imageRecyclerView.setAdapter(imageAdapter);

        if (image != null) {
            for (Bitmap i : image) {
                MarkerDescription.imageAdapter.setItems(i);
            }
        } else {
            image = new ArrayList<>();
        }

        final Button ok = view.findViewById(R.id.b_ok);
        ok.setOnClickListener(v -> {
            comments = editText.getText().toString();
            name = placeName.getText().toString();
            MapActivity activity = (MapActivity) getActivity();
            activity.addDescriptionOnMarker(comments, name, image);
            alertDialog.cancel();
        });

        alertDialog.setView(view);
        alertDialog.show();
        return alertDialog;
    }

    public void removeImage(int position) {
        image.remove(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && null != data) {
            Uri imageUri = data.getData();
            try {

                Point screenSize = ImageUtils.getScreenSize(getContext());
                Bitmap scaledBitmap = ImageUtils.decodeUriToScaledBitmap(getContext(), imageUri, 120, 120);
                image.add(scaledBitmap);
                MarkerDescription.imageAdapter.setItems(scaledBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public static Bitmap cropToSquare(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = Math.min(height, width);
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = Math.max(cropW, 0);
        int cropH = (height - width) / 2;
        cropH = Math.max(cropH, 0);
        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
    }

}
