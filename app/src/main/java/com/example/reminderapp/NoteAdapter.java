package com.example.reminderapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    Note noteActivity;
    List<Posts> postsStack;

    public NoteAdapter(Note note, List<Posts> posts) {
        this.noteActivity = note;
        this.postsStack = posts;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(noteActivity).inflate(R.layout.post_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, final int position) {
        holder.posttitle.setText(postsStack.get(position).getPosttitle());
        holder.postdetails.setText(postsStack.get(position).getPostdetails());

        holder.postbtndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteActivity.firebaseFirestore.collection(noteActivity.collectionNames.getNotes())
                        .document(postsStack.get(position).getPosdid())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                noteActivity.loadDataFromFirebase();
                            }
                        });

            }
        });
    }

    @Override
    public int getItemCount() {
        return postsStack.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView posttitle,postdetails;
        Button postbtndelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            posttitle = itemView.findViewById(R.id.posttitle);
            postdetails = itemView.findViewById(R.id.postdetails);
            postbtndelete = itemView.findViewById(R.id.postbtndelete);
        }
    }
}
