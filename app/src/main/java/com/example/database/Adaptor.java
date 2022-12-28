package com.example.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adaptor extends RecyclerView.Adapter<Adaptor.ViewHolder> implements Filterable {

    MyClick myClick;

    MyLongClick myLongClick;

    public void setMyLongClick(MyLongClick myLongClick) {
        this.myLongClick = myLongClick;
    }

    public void setMyClick(MyClick myClick) {
        this.myClick = myClick;
    }

    List<ModelClass> modelClassList;

    List<ModelClass> modelClassListBackup;

    public Adaptor(List<ModelClass> modelClassList) {
        this.modelClassList = modelClassList;
        this.modelClassListBackup=new ArrayList<>(modelClassList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_layout,null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelClass listPosition=modelClassList.get(position);
        holder.feeTv.setText(Float.toString(listPosition.getFee()));
        holder.nameTv.setText(listPosition.getName());
        holder.rollNoTv.setText(Integer.toString(listPosition.getRollNo()));
    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyWord) {
            List<ModelClass> filteredList=new ArrayList<>();
            if (keyWord.toString().isEmpty()){
                filteredList.addAll(modelClassListBackup);
            }
            else {
                for (ModelClass object: modelClassListBackup){
                    if (object.getName().toString().toLowerCase().contains(keyWord.toString().toLowerCase())){
                        filteredList.add(object);
                    }

                }
            }

            FilterResults results=new FilterResults();
            results.values=filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            modelClassList.clear();
            modelClassList.addAll((List<ModelClass>)results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv,rollNoTv,feeTv;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv=itemView.findViewById(R.id.nameTv);
            rollNoTv=itemView.findViewById(R.id.rollNoTv);
            feeTv=itemView.findViewById(R.id.feeTv);
            cardView=itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myClick!=null){
                        myClick.Click(view,getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (myLongClick!=null){
                        myLongClick.LongClick(view,getAdapterPosition());
                    }

                    return true;
                }
            });
        }
    }

    interface MyClick{
        void Click(View v,int index);
    }

    interface MyLongClick{
        void LongClick(View view,int index);
    }
}
