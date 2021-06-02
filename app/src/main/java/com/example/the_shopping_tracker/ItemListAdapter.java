package com.example.the_shopping_tracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<com.example.the_shopping_tracker.ItemListAdapter.ViewHolder> {

    List<ItemModel> itemList;

    public ItemListAdapter(List<ItemModel> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //to generate the rows on view and call on data
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        void bindData(int position){
            TextView txtName = itemView.findViewById(R.id.txtName);
            txtName.setText(itemList.get(position).getName());

            TextView txtDescriptionItem = itemView.findViewById(R.id.txtDescriptionItem);
            txtDescriptionItem.setText(itemList.get(position).getDescription());

            ImageView imageView = itemView.findViewById(R.id.imageView);
            Glide.with(imageView).load(itemList.get(position).getImage()).into(imageView);

            //blank it
            TextView txtStockT = itemView.findViewById(R.id.txtStockT);
            TextView txtGoal = itemView.findViewById(R.id.txtGoal);
           // txtStockT.setText(itemList.get(position).getStock());
            // txtGoal.setText(itemList.get(position).getGoal());
            //how to get amount

            ProgressBar progressBar = itemView.findViewById(R.id.progressBar);
            progressBar.setMax(Integer.valueOf(itemList.get(position).getGoal()));
            progressBar.setProgress(Integer.valueOf(itemList.get(position).getStock()));
            //should be stock out of goal

            progressBar.getProgressDrawable().setColorFilter(
            Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            //https://stackoverflow.com/questions/2020882/how-to-change-progress-bars-progress-color-in-android

        }
    }
}
