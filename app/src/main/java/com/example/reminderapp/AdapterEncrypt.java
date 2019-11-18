package com.example.reminderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class AdapterEncrypt extends RecyclerView.Adapter<AdapterEncrypt.EncryViewHolder>
{

    Context context;
    List<HashMap<String, String>> itemList;


    public AdapterEncrypt(Context c, List<HashMap<String, String>> it) {
        this.context = c;
        this.itemList = it;
    }

    @NonNull
    @Override
    public EncryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EncryViewHolder(LayoutInflater.from(this.context).inflate(R.layout.encryptlayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EncryViewHolder holder, int position) {
        holder.encrytitle.setText(itemList.get(position).get("title"));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class EncryViewHolder extends RecyclerView.ViewHolder
    {

        TextView encrytitle, encrydata;

        public EncryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            encrytitle = itemView.findViewById(R.id.encrytitle);
//            encrydata =  itemView.findViewById(R.id.encrydata);
        }
    }
}
