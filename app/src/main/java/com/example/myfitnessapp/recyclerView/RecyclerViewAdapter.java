package com.example.myfitnessapp.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessapp.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NutrientViewholder> {

    List<NutrientItem> nutrientList;
    Context context;

    public RecyclerViewAdapter( List<NutrientItem> nutrientList, Context context){
        this.nutrientList=nutrientList;
        this.context=context;
    }


    @NonNull
    @Override
    public NutrientViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrientrow,parent,false);
        return new NutrientViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NutrientViewholder holder, int position) {

        NutrientItem nutrientItem= nutrientList.get(position);
        holder.nutrientNameTextView.setText(nutrientItem.getName());
        holder.totalTextView.setText(String.valueOf(nutrientItem.getValue()));

    }

    @Override
    public int getItemCount() {
        return nutrientList.size();
    }
    public static class NutrientViewholder extends RecyclerView.ViewHolder {

        CardView rowCardView;
        TextView nutrientNameTextView,totalTextView,goalTextView;

        public NutrientViewholder(@NonNull View itemView) {
            super(itemView);

            rowCardView=itemView.findViewById(R.id.rowCardView);
            nutrientNameTextView=itemView.findViewById(R.id.nutrientNameTextView);
            totalTextView=itemView.findViewById(R.id.totalTextView);
            goalTextView=itemView.findViewById(R.id.goalTextView);


        }
    }


}

