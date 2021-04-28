package com.example.travelwithme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.adapter.ImageAdapter;

import java.util.List;

public class ViewingMarkerDescription extends AppCompatDialogFragment {

    View view;
    private RecyclerView imageRecyclerView;
    public static ImageAdapter imageAdapter;
    public String text;
    public List<Bitmap> image;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.viewing_marker_description, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog alertDialog = builder.create();

        Bundle bundle = getArguments();
        text = bundle.getString("text");
        image = bundle.getParcelableArrayList("image");

        TextView textView = (TextView) view.findViewById(R.id.tv_comment);
        textView.setText(text);

        imageRecyclerView = view.findViewById(R.id.list_image_v);
        LinearLayoutManager l = new LinearLayoutManager(view.getContext());
        l.setOrientation(RecyclerView.HORIZONTAL);
        imageRecyclerView.setLayoutManager(l);
        imageAdapter = new ImageAdapter(this);
        imageRecyclerView.setAdapter(imageAdapter);
        for(Bitmap i: image){
            ViewingMarkerDescription.imageAdapter.setItems(i);
        }

        alertDialog.setView(view);
        alertDialog.show();
        return alertDialog;
    }
}