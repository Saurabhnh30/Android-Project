package com.example.reminderapp.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.DocumentListsScreenActivity;
import com.example.reminderapp.NoteDisplayActivity;
import com.example.reminderapp.R;
import com.example.reminderapp.models.Posts;

import java.util.List;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.DocumentListViewHolder> {

    private DocumentListsScreenActivity docListActivity;
    private List<Posts> postsList;


    public DocumentListAdapter(DocumentListsScreenActivity docListActivity, List<Posts> pl) {
        Log.d("DOC_LIST_ADAP", pl.toString());
        this.docListActivity = docListActivity;
        this.postsList = pl;
    }


    @NonNull
    @Override
    public DocumentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DocumentListViewHolder(LayoutInflater.from(docListActivity).inflate(R.layout.document_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentListViewHolder holder, final int position) {
        Log.d("DOC_LIST_ADAP", "about image: " + postsList.get(position).getAboutImage() + " post title: " + postsList.get(position).getPosttitle());

        if (postsList.get(position).getPosttitle() == null) {
            holder.docNotePreviewTV.setText(postsList.get(position).getAboutImage());
        }
        else {
            holder.docNotePreviewTV.setText(postsList.get(position).getPosttitle());
        }


        holder.docNoteWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docListActivity.startActivity(new Intent(docListActivity, NoteDisplayActivity.class).putExtra(Posts.POST_ID, postsList.get(position).getPosdid()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class DocumentListViewHolder extends RecyclerView.ViewHolder {

        TextView docNotePreviewTV;
        LinearLayout docNoteWrapperLL;

        public DocumentListViewHolder(@NonNull View itemView) {
            super(itemView);

            docNotePreviewTV = itemView.findViewById(R.id.docNotePreviewTV);
            docNoteWrapperLL = itemView.findViewById(R.id.docNoteWrapperLL);
        }
    }
}
