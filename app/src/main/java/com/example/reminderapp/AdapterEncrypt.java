package com.example.reminderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class AdapterEncrypt extends RecyclerView.Adapter<AdapterEncrypt.EncryViewHolder>
{

    EncryptScreen encryptScreen;
    List<HashMap<String, String>> itemList;


    public AdapterEncrypt(EncryptScreen c, List<HashMap<String, String>> it) {
        this.encryptScreen = c;
        this.itemList = it;
    }

    @NonNull
    @Override
    public EncryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EncryViewHolder(LayoutInflater.from(this.encryptScreen).inflate(R.layout.encryptlayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EncryViewHolder holder, final int position) {
        holder.encrytitle.setText(itemList.get(position).get("title"));
        
        holder.encryptdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encryptScreen.deleteEncrypt(itemList.get(position).get("id"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class EncryViewHolder extends RecyclerView.ViewHolder
    {

        TextView encrytitle, encrydata;
        Button encryptdelete;


        public EncryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            encrytitle = itemView.findViewById(R.id.encrytitle);
            encryptdelete = itemView.findViewById(R.id.encryptdelete);
//            encrydata =  itemView.findViewById(R.id.encrydata);
        }
    }
}
