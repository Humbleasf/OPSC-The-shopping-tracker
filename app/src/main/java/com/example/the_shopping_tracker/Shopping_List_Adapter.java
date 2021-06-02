package com.example.the_shopping_tracker;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Shopping_List_Adapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    private Context sContext;
    private List<Shopping_Details> sData;

    final Handler mHandler = new Handler();

    public Shopping_List_Adapter(Context sContext, List<Shopping_Details> sdata) {
        this.sContext = sContext;
        this.sData = sdata;
    }
    @NonNull
    @Override
    public RecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(sContext);
        view = mInflater.inflate(R.layout.shopping_list,parent,false);

        return new RecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.MyViewHolder holder2, int position) {
        holder2.tv_shopping_name.setText(sData.get(position).getsName());
        holder2.tv_shopping_description.setText(sData.get(position).getShopping_Description());
        holder2.tv_shopping_Amount.setText(sData.get(position).getShopping_Amount());
        Glide.with(this.sContext).load(sData.get(position).getGsReference()).into(holder2.img_shopping);
    }

    @Override
    public int getItemCount() {
        return sData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_shopping_name;
        TextView tv_shopping_description;
        TextView tv_shopping_Amount;
        ImageView img_shopping;
        //Abhiandroid.com. 2021. ProgressBar Tutorial With Example In Android Studio | Abhi Android. [online] Available at: <https://abhiandroid.com/ui/progressbar#:~:text=To%20add%20a%20progress%20bar,the%20progress%20bar's%20horizontal%20style.> [Accessed 17 May 2021].
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_shopping_name = (TextView) itemView.findViewById(R.id.txtItemName);
            tv_shopping_description = (TextView) itemView.findViewById(R.id.txtDescription);
            tv_shopping_Amount = (TextView) itemView.findViewById(R.id.txtAmount);
            img_shopping = (ImageView) itemView.findViewById(R.id.shoppingImage);
        }
    }
}
