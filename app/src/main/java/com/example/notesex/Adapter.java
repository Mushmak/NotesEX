package com.example.notesex;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {
/*
    Populate main menu
 */


    LayoutInflater inflater;
    List<Note> notes;
    List<Note> NotesAll;

    /*
    Adapter(Context context, List<Note> notes){
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;

    }*/

    public Adapter(Context context, List<Note> notes){
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
        this.NotesAll = new ArrayList<>(notes);
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.custom_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        String title = notes.get(position).getTitle();
        String date = notes.get(position).getDate();
        String time = notes.get(position).getTime();

        holder.Title.setText(title);
        holder.Date.setText(date);
        holder.Time.setText(time);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Note> filteredList = new ArrayList<>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(NotesAll);
            }else{
                for(Note x: NotesAll){
                    if(x.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(x);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notes.clear();
                notes.addAll((Collection<? extends Note>) filterResults.values);
                notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Title,Date, Time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.Title);
            Time = itemView.findViewById(R.id.time);
            Date = itemView.findViewById(R.id.Date);

          itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent i = new Intent(view.getContext(),Details.class);
                  i.putExtra("ID",notes.get(getAdapterPosition()).getId());
                  view.getContext().startActivity(i);
              }
          });

        }
    }
}
