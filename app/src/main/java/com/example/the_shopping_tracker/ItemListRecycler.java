package com.example.the_shopping_tracker;

import android.content.Context;
import android.os.Handler;
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

public class ItemListRecycler extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    private Context iContext;
    private List<ItemModel> iData;
    final Handler mHandler = new Handler();
    String strItemName;
    public ItemListRecycler(Context iContext, List<ItemModel> idata) {
        this.iContext = iContext;
        this.iData = idata;
    }
    @NonNull
    @Override
    public RecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(iContext);
        view = mInflater.inflate(R.layout.item_listview,parent,false);

        return new RecycleViewAdapter.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.MyViewHolder holder3, int position) {
        holder3.txtName.setText(iData.get(position).getName());
        holder3.txtDescriptionItem.setText(iData.get(position).getDescription());

        Glide.with(this.iContext).load(iData.get(position).getImage()).into(holder3.imageView);
        holder3.progressBarItem.setProgress(iData.get(position).getStock());
        holder3.progressBarItem.setMax(iData.get(position).getGoal());
        holder3.txtStockT.setText(iData.get(position).getStock()+"/"+ iData.get(position).getGoal());
    }
    @Override
    public int getItemCount() {
        return iData.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtStockT;
        TextView txtDescriptionItem;
        ProgressBar progressBarItem;
        ImageView imageView;
        //Abhiandroid.com. 2021. ProgressBar Tutorial With Example In Android Studio | Abhi Android. [online] Available at: <https://abhiandroid.com/ui/progressbar#:~:text=To%20add%20a%20progress%20bar,the%20progress%20bar's%20horizontal%20style.> [Accessed 17 May 2021].
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtStockT = (TextView) itemView.findViewById(R.id.txtStockT);
            txtDescriptionItem = (TextView) itemView.findViewById(R.id.txtDescriptionItem);
            progressBarItem = (ProgressBar) itemView.findViewById(R.id.progressBar);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
