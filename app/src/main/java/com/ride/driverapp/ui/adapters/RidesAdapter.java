package com.ride.driverapp.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ride.driverapp.model.RideContract;
import com.ride.driverapp.R;
import com.ride.driverapp.viewmodel.RidesViewModel;

import java.util.ArrayList;

public class RidesAdapter  extends RecyclerView.Adapter<RidesAdapter.MyViewHolder>  {

    private ArrayList<RideContract> mDataset;
    private  RidesViewModel viewModel;
    private static final String TAG = RidesAdapter.class.getSimpleName();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView destination;
        public TextView pickup;
        public TextView customer;

        public MyViewHolder(@NonNull View itemView, ArrayList<RideContract> rideList) {
            super(itemView);

                customer = itemView.findViewById(R.id.customer_name);

                ImageView arrowDown = itemView.findViewById(R.id.arrowdown);
                arrowDown.setOnClickListener(v -> {
                    viewModel.getShowDetails().postValue(true);
                });

                itemView.setOnClickListener(v -> {
                    int adapterPosition = getAdapterPosition();
                    Log.w(TAG, "clicked: " + adapterPosition);
                    viewModel.setAdapterItem(rideList.get(adapterPosition));
                });

        }
    }


    public RidesAdapter(ArrayList<RideContract> myDataset, RidesViewModel vm){
           this.mDataset = myDataset;
            this.viewModel = vm;
    }



    @NonNull
    @Override
    public RidesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.ride_item, parent, false);
        return new MyViewHolder(view, this.mDataset);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        RideContract ride = mDataset.get(position);
        Log.w(TAG, ride.toString());

        //holder.customer.setText(ride.getCustomer_name().split(" ",2)[0]);
        holder.customer.setText(ride.getCustomer_name());

        //TODO: something something

    }



    @Override
    public int getItemCount() {
        if(mDataset != null){
            return mDataset.size();
        } else {
            return 0;
        }

    }


}
