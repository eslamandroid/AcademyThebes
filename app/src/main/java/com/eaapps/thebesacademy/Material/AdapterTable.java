package com.eaapps.thebesacademy.Material;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eaapps.thebesacademy.R;

import java.util.List;

/**
 * Created by eslamandroid on 3/14/18.
 */

public class AdapterTable extends RecyclerView.Adapter<AdapterTable.viewHolder> {
    Context context;
    List<ModelTable> modelTableList;

    public AdapterTable(Context context, List<ModelTable> modelTableList) {
        this.context = context;
        this.modelTableList = modelTableList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.item_table_add, parent, false));
    }

    @SuppressLint("setTextI18n")
    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        ModelTable modelTable = modelTableList.get(position);

        holder.tName.setText("  Name Doctor : " + modelTable.getName_teacher());
        holder.tMaterial.setText("  Name Material : " + modelTable.getName_material());
        holder.tDay.setText("  Day : " + modelTable.getDay());
        holder.tTime.setText("  Time : " + modelTable.getTime());
        holder.deleteItem.setOnClickListener(v -> {
            modelTableList.remove(position);
            AddTable.callBackTable.onInfoTable(modelTableList, position);
            notifyDataSetChanged();
        });


    }

    @Override
    public int getItemCount() {
        return modelTableList.size();
    }


    class viewHolder extends RecyclerView.ViewHolder {
        TextView tName, tMaterial, tDay, tTime;
        Button deleteItem;

        public viewHolder(View itemView) {
            super(itemView);
            tName = itemView.findViewById(R.id.nameTeacher);
            tMaterial = itemView.findViewById(R.id.nameMaterial);
            tDay = itemView.findViewById(R.id.nameDay);
            tTime = itemView.findViewById(R.id.nameTime);
            deleteItem = itemView.findViewById(R.id.deleteItem);
        }
    }
}
