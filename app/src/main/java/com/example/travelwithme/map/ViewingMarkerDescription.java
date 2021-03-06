package com.example.travelwithme.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.R;
import com.example.travelwithme.adapter.ImageAdapterViewing;

import java.util.List;

public class ViewingMarkerDescription extends AppCompatDialogFragment {

    private View view;
    private RecyclerView imageRecyclerView;
    public static ImageAdapterViewing imageAdapter;
    public String comments;
    public String name;
    public List<String> image;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.viewing_marker_description, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog alertDialog = builder.create();

        Bundle bundle = getArguments();
        comments = bundle.getString("text");
        name = bundle.getString("name");
        image = bundle.getStringArrayList("image");

        TextView textView = view.findViewById(R.id.tv_comment);
        textView.setText(comments);

        TextView nameView = view.findViewById(R.id.tv_place_name);
        nameView.setText(name);


        imageRecyclerView = view.findViewById(R.id.list_image_v);
        LinearLayoutManager l = new LinearLayoutManager(view.getContext());
        l.setOrientation(RecyclerView.HORIZONTAL);
        imageRecyclerView.setLayoutManager(l);
        imageAdapter = new ImageAdapterViewing();
        imageRecyclerView.setAdapter(imageAdapter);
        for (String i : image) {
            ViewingMarkerDescription.imageAdapter.setItems(i);
        }

        alertDialog.setView(view);
        alertDialog.show();
        return alertDialog;
    }
}
