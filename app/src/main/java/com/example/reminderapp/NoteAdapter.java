package com.example.reminderapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.models.Notes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    Note noteActivity;
    List<Notes> notesList;

    public NoteAdapter(Note note, List<Notes> posts) {
        this.noteActivity = note;
        this.notesList = posts;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(noteActivity).inflate(R.layout.post_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, final int position) {
        holder.noteTitle.setText(notesList.get(position).getNoteTitle());
        holder.noteDetails.setText(notesList.get(position).getNoteDetails());

        holder.postbtndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteActivity.firestore.collection(noteActivity.collectionNames.getNotes())
                        .document(notesList.get(position).getNoteId())
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
        return notesList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle, noteDetails;
        Button postbtndelete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteDetails = itemView.findViewById(R.id.noteDetails);
            postbtndelete = itemView.findViewById(R.id.postbtndelete);
        }
    }
}
