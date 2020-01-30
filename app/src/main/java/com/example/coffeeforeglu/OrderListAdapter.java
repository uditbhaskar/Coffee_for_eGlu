package com.example.coffeeforeglu;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ProgrammingViewHolder> {

    List<DataModel> modelArrayList;

    public OrderListAdapter() {
        modelArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder holder, int position) {

        holder.coffee_type.setText(modelArrayList.get(position).getType());
        holder.coffee_cup_size.setText(modelArrayList.get(position).getSize());
        holder.coffee_cup_price.setText(modelArrayList.get(position).getPrice());
        holder.date.setText(getDateTime(modelArrayList.get(position).getDate()));
        holder.name.setText(modelArrayList.get(position).getName());

    }

    public void setItems(List<DataModel> modelArrayList) {
        this.modelArrayList = modelArrayList;
        notifyDataSetChanged();
    }

    public String getDateTime(Long timeStamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timeStamp);
        String date = DateFormat.format("EEE MMM dd yyyy, hh:mm  ", cal).toString();
        return date;

    }


    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }


    public class ProgrammingViewHolder extends RecyclerView.ViewHolder {
        TextView coffee_type;
        TextView coffee_cup_size;
        TextView coffee_cup_price;
        TextView date;
        TextView name;


        public ProgrammingViewHolder(@NonNull View itemView) {
            super(itemView);
            coffee_type = itemView.findViewById(R.id.coffee_type);
            coffee_cup_size = itemView.findViewById(R.id.coffee_cup_size);
            coffee_cup_price = itemView.findViewById(R.id.coffee_cup_price);
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
        }
    }
}

