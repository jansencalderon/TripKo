package com.tripko.ui.trips;

import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tripko.R;
import com.tripko.databinding.ItemReservationNewBinding;
import com.tripko.model.data.Reservation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jansen on 2/19/2018.
 */

public class TripsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private TripsView view;
    private List<Reservation> list;
    private static final int VIEW_TYPE_DEFAULT = 0;

    public TripsAdapter(TripsView view) {
        this.view = view;
        list = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemReservationNewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_reservation_new, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Reservation item = list.get(position);
        viewHolder.binding.setReservation(item);
        viewHolder.binding.setView(view);
        switch (item.getStatus()) {
            case "R":
                viewHolder.binding.status.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.orange));
                break;
            case "P":
                viewHolder.binding.status.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.blue));
                break;
            case "A":
                viewHolder.binding.status.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.greenSuccess));
                break;
            case "D":
                viewHolder.binding.status.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.redFailed));
                break;
        }

    }

    public void setList(List<Reservation> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemReservationNewBinding binding;

        public ViewHolder(ItemReservationNewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}