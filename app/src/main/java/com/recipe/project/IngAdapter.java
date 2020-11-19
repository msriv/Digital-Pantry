package com.recipe.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngAdapter extends RecyclerView.Adapter<IngAdapter.MyViewHolder> {

    Context context;
    List<String> iList;

    public IngAdapter(List<String> heroList, Context context) {
        this.iList = heroList;
        this.context = context;
    }


    @Override
    public IngAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.input_element, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IngAdapter.MyViewHolder holder, final int position) {
        String item = iList.get(position);
        holder.name.setText(item);

    }

    @Override
    public int getItemCount() {
        return iList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);


        }
    }
}

