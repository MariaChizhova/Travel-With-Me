package com.example.travelwithme.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.map.MarkerDescription;
import com.example.travelwithme.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    MarkerDescription parent;

    private final List<Bitmap> list = new ArrayList<>();

    public ImageAdapter(MarkerDescription markerDescription) {
        parent = markerDescription;
    }


    @Override
    public @NotNull ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_view, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bind(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(Bitmap bitmap) {
        list.add(bitmap);
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

        public void bind(Bitmap bitmap, int position) {
            imageView.setImageBitmap(bitmap);

            cross.setOnClickListener(arg0 -> {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
                parent.removeImage(position);
            });

        }
    }
}
