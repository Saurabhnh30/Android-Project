package com.example.reminderapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.AddContactsHome;
import com.example.reminderapp.R;
import com.example.reminderapp.models.Contacts;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    List<Contacts> contactsList;
    Context context;

    public ContactsAdapter(Context c, List<Contacts> clist) {
        this.context = c;
        this.contactsList = clist;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(this.context).inflate(R.layout.contacts_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, final int position) {
        holder.contactnumber.setText(contactsList.get(position).getContactPhone());
        holder.contactname.setText(contactsList.get(position).getContactFirstname());

        if (contactsList.get(position).getContactAvatar() != null) {
            Picasso.get().load(contactsList.get(position).getContactAvatar()).into(holder.contactImageView);
        }

        holder.contactListLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AddContactsHome.class)
                .putExtra(Contacts.CONTACT_ID, contactsList.get(position)._getContactId())
                .putExtra(Contacts.CONTACT_FIRSTNAME, contactsList.get(position).getContactFirstname())
                .putExtra(Contacts.CONTACT_SIRNAME, contactsList.get(position).getContactSirname())
                .putExtra(Contacts.CONTACT_COMPANY, contactsList.get(position).getContactCompany())
                .putExtra(Contacts.CONTACT_EMAIL, contactsList.get(position).getContactEmail())
                .putExtra(Contacts.CONTACT_PHONE, contactsList.get(position).getContactPhone())
                .putExtra(Contacts.CONTACT_AVATAR, contactsList.get(position).getContactAvatar())
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.contactsList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView contactImageView;
        TextView contactname, contactnumber;
        LinearLayout contactListLL;

        public ContactViewHolder(@NonNull View iv) {
            super(iv);

            contactImageView = iv.findViewById(R.id.contactImageView);
            contactname = iv.findViewById(R.id.contactname);
            contactnumber = iv.findViewById(R.id.contactnumber);
            contactListLL = iv.findViewById(R.id.contactListLL);
        }
    }
}
