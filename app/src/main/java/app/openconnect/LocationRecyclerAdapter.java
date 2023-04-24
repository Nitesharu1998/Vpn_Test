package app.openconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vpnpro.dubaivpn.R;

public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.LocationRecyclerViewHolder> {

    private String[] S_Data = null;
    private String[] C_Data = null;
    private int[] F_data = null;
    private OnItemListener itemListener;

    public LocationRecyclerAdapter(String[] s_data,String[] c_data, int[] f_data, OnItemListener listener) {
        S_Data = s_data;
        C_Data = c_data;
        itemListener = listener;
        F_data = f_data;
    }


    @NonNull
    @Override
    public LocationRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.location_list_item, parent, false);
        return new LocationRecyclerViewHolder(view, itemListener);
    }


    @Override
    public void onBindViewHolder(@NonNull LocationRecyclerViewHolder holder, int position) {

        holder.LocationName.setText(S_Data[position]);
        holder.city.setText(C_Data[position]);
        holder.Flag.setImageResource(F_data[position]);

        if (DataManager.Server_Types[position] == 2) {
            holder.vip.setVisibility(View.VISIBLE);
        } else {
            holder.vip.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return S_Data.length;
    }

    public class LocationRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView LocationName,city;
        ImageView Flag,vip;

        public LocationRecyclerViewHolder(@NonNull View itemView, final OnItemListener listener) {
            super(itemView);
            Flag = (ImageView) itemView.findViewById(R.id.country_flag);
            vip = (ImageView) itemView.findViewById(R.id.free_iv);
            city = (TextView) itemView.findViewById(R.id.cityName);
            LocationName = (TextView) itemView.findViewById(R.id.location_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(getAdapterPosition());
                }
            });

        }
    }

    public interface OnItemListener {
        void OnItemClick(int index);
    }

}
