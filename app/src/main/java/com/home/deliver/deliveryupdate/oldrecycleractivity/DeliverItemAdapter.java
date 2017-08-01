package com.home.deliver.deliveryupdate.oldrecycleractivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home.deliver.deliveryupdate.R;

import java.util.List;

/**
 * Created by aravindnga on 16/07/17.
 */

public class DeliverItemAdapter extends RecyclerView.Adapter<DeliverItemAdapter.ViewHolder> {


    private List<DeliverListItem> listData;
    private LayoutInflater layoutInflater;

    private ItemClickCallBack itemClickCallBack;

    public interface ItemClickCallBack {
        void onItemClick(int p);
    }

    public void setItemClickCallBack(final ItemClickCallBack itemClickCallBack){
        this.itemClickCallBack = itemClickCallBack;
    }

    public DeliverItemAdapter(List<DeliverListItem> listData, Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.listData = listData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cardview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeliverListItem item = listData.get(position);

        //holder.title.setText(item.getName());
        //holder.image.setImageResource(item.getImgId());
        holder.distance.setText(String.valueOf(item.getDistance()));
        holder.name.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    //Class that holds the contents togeather !!
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, distance;
        public View container;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameoftheperson);
            distance = itemView.findViewById(R.id.distance);
            container = itemView.findViewById(R.id.cardView);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.cardView){
                itemClickCallBack.onItemClick(getAdapterPosition());
            }

        }
    }


}
