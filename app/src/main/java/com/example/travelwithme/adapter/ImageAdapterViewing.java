package com.example.travelwithme.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapterViewing extends RecyclerView.Adapter<ImageAdapterViewing.ImageViewHolder> {


    private final List<String> list = new ArrayList<>();

    public ImageAdapterViewing() {
    }

    @Override
    public @NotNull ImageAdapterViewing.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_view, parent, false);
        return new ImageAdapterViewing.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapterViewing.ImageViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(String s) {
        list.add(s);
        notifyDataSetChanged();
    }


    class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final ImageView cross;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            cross = itemView.findViewById(R.id.cross);
        }

        public void bind(String s) {
            Picasso.get().load(PostAdapter.S3IMAGES + s).into(imageView);
            cross.setVisibility(View.INVISIBLE);
        }
    }
}
