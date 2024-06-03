package com.maciej_switalski.projekt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EntryAdapter extends RecyclerView.Adapter<EntryViewHolder> {
    private final List<Entry> entryList;

    public EntryAdapter(List<Entry> entryList) {
        this.entryList = entryList;
        sortByDate();
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        return new EntryViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        Entry entry = entryList.get(position);
        holder.bind(entry); // Bind the Entry object to the ViewHolder
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public void updateList() {
        notifyDataSetChanged();
        sortByDate();
    }

    private void sortByDate() {
        entryList.sort(new Comparator<Entry>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            @Override
            public int compare(Entry entry1, Entry entry2) {
                try {
                    Date date1 = dateFormat.parse(entry1.getDate());
                    Date date2 = dateFormat.parse(entry2.getDate());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0; // or handle the exception as needed
                }
            }
        });
        notifyDataSetChanged(); // Notify RecyclerView of the data set change
    }

    public void removeEntry(int position) {
        entryList.remove(position);
        notifyItemRemoved(position);
    }


}
