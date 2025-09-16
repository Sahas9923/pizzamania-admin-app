package com.example.pizzamaniaadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    private final Context context;
    private final List<BranchesActivity.Branch> branchList;

    public BranchAdapter(Context context, List<BranchesActivity.Branch> branchList) {
        this.context = context;
        this.branchList = branchList;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.branch_item, parent, false);
        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        BranchesActivity.Branch branch = branchList.get(position);

        holder.tvName.setText(branch.getName());
        holder.tvAddress.setText(branch.getAddress());
        holder.tvLatLng.setText("Lat: " + branch.getLatitude() + ", Lng: " + branch.getLongitude());
        holder.tvHoursDays.setText("Hours: " + branch.getOpeningHours() + ", " + branch.getOpeningDays());
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    static class BranchViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvLatLng, tvHoursDays;

        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvBranchName);
            tvAddress = itemView.findViewById(R.id.tvBranchAddress);
            tvLatLng = itemView.findViewById(R.id.tvBranchLatLng);
            tvHoursDays = itemView.findViewById(R.id.tvBranchHoursDays);
        }
    }
}
