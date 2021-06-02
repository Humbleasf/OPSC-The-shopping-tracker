package com.example.the_shopping_tracker;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;
import java.util.List;

//https://www.youtube.com/watch?v=SD2t75T5RdY
public class RecycleViewAdapter extends RecyclerView.Adapter<com.example.the_shopping_tracker.RecycleViewAdapter.MyViewHolder> {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Context mContext;
    private List<Category> cData;

    private String strName;
    public RecycleViewAdapter(Context mContext, List<Category> cdata, String name) {
        this.mContext = mContext;
        this.cData = cdata;
        this.strName = name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.list_catagory,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.tv_catagory_name.setText(cData.get(position).getcName());
        Glide.with(this.mContext).load(cData.get(position).getGsReference()).into(holder.img_catagory);
        holder.progressBar.setMax(cData.get(position).getGoalCategory());
        holder.progressBar.setProgress(cData.get(position).getMinCategory());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Log.d("firebase", "key strCIKey:  " + strName);
                Intent itemList = new Intent(mContext, ItemList.class);
                itemList.putExtra("Category",cData.get(position).getcName() );
                itemList.putExtra( "Name", strName);
                mContext.startActivity(itemList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_catagory_name, txtStockT;
        ImageView img_catagory;
        CardView cardView;
        TextView tv_shopping_name;
        TextView tv_shopping_description;
        TextView tv_shopping_Amount;
        ImageView img_shopping;
        CheckBox cb_shopping;
        TextView txtName;
        TextView txtDescriptionItem;
        ImageView imageView;
        ProgressBar progressBarItem;
        //Abhiandroid.com. 2021. ProgressBar Tutorial With Example In Android Studio | Abhi Android. [online] Available at: <https://abhiandroid.com/ui/progressbar#:~:text=To%20add%20a%20progress%20bar,the%20progress%20bar's%20horizontal%20style.> [Accessed 17 May 2021].
        ProgressBar progressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_catagory_name = (TextView) itemView.findViewById(R.id.catagory_name);
            img_catagory = (ImageView) itemView.findViewById(R.id.catagory_image);
            cardView = (CardView) itemView.findViewById(R.id.card_view_id);
            progressBar = (ProgressBar) itemView.findViewById(R.id.category_progress);
            tv_shopping_name = (TextView) itemView.findViewById(R.id.txtItemName);
            tv_shopping_description = (TextView) itemView.findViewById(R.id.txtDescription);
            tv_shopping_Amount = (TextView) itemView.findViewById(R.id.txtAmount);
            img_shopping = (ImageView) itemView.findViewById(R.id.shoppingImage);
            cb_shopping = (CheckBox) itemView.findViewById(R.id.shop_check);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtDescriptionItem = (TextView) itemView.findViewById(R.id.txtDescriptionItem);
            progressBarItem = (ProgressBar) itemView.findViewById(R.id.progressBar);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            txtStockT = (TextView) itemView.findViewById(R.id.txtStockT);
        }
    }



}

