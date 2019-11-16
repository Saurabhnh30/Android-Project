package com.example.reminderapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterEncrypt extends RecyclerView.Adapter<AdapterEncrypt.EncryViewHolder>
{

    Context context;


    public AdapterEncrypt(Context c) {
        this.context = c;
    }

    @NonNull
    @Override
    public EncryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EncryViewHolder(LayoutInflater.from(this.context).inflate(R.layout.encryptlayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EncryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class EncryViewHolder extends RecyclerView.ViewHolder
    {

        TextView encrytitle, encrydata;

        public EncryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            encrytitle = itemView.findViewById(R.id.encrypttitle);
            encrydata =  itemView.findViewById(R.id.encrydata);
        }
    }
}
