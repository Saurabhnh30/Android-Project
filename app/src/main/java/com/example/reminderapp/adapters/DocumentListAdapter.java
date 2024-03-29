package com.example.reminderapp.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.DocumentListsScreenActivity;
import com.example.reminderapp.NoteDisplayActivity;
import com.example.reminderapp.R;
import com.example.reminderapp.models.Documents;
import com.example.reminderapp.models.Notes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.DocumentListViewHolder> implements Filterable {

    private DocumentListsScreenActivity docListActivity;
    private List<Documents> documentsList;
    private List<Documents> allDocumentsList;


    public DocumentListAdapter(DocumentListsScreenActivity docListActivity, List<Documents> pl) {
        Log.d("DOC_LIST_ADAP", pl.toString());
        this.docListActivity = docListActivity;
        this.documentsList = pl;
        this.allDocumentsList = new ArrayList<>(pl);
    }


    @NonNull
    @Override
    public DocumentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DocumentListViewHolder(LayoutInflater.from(docListActivity).inflate(R.layout.document_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentListViewHolder holder, final int position) {
        Log.d("DOC_LIST_ADAP", "about image: " + documentsList.get(position).getDocImage() + " post title: " + documentsList.get(position).getDocTitle());

        holder.docNotePreviewTV.setText(documentsList.get(position).getDocTitle());

        holder.docNoteWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putString("postid", documentsList.get(position).getDocId());
                extras.putString("posttype", "DOC");
                docListActivity.startActivity(new Intent(docListActivity, NoteDisplayActivity.class).putExtras(extras));
            }
        });


        holder.documentDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docListActivity.deleteDocument(documentsList.get(position).getDocId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentsList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Documents> filteredDocuments = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    filteredDocuments.addAll(allDocumentsList);
                }
                else {
                    String filteredPattern = charSequence.toString().toLowerCase().trim();
                    for (Documents u : allDocumentsList) {
                        if (u.getDocTitle().toLowerCase().contains(filteredPattern)) {
                            filteredDocuments.add(u);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDocuments;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                documentsList.clear();
                documentsList.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    class DocumentListViewHolder extends RecyclerView.ViewHolder {

        TextView docNotePreviewTV;
        LinearLayout docNoteWrapperLL;
        ImageButton documentDeleteBtn;

        public DocumentListViewHolder(@NonNull View itemView) {
            super(itemView);

            docNotePreviewTV = itemView.findViewById(R.id.docNotePreviewTV);
            docNoteWrapperLL = itemView.findViewById(R.id.docNoteWrapperLL);
            documentDeleteBtn = itemView.findViewById(R.id.documentDeleteBtn);
        }
    }
}
