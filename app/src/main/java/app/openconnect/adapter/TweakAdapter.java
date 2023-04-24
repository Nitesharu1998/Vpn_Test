package app.openconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.vpnpro.dubaivpn.R;

import java.util.List;

import app.openconnect.model.Tweak;

public class TweakAdapter extends RecyclerView.Adapter<TweakAdapter.MyViewHolder> {
    private int VIEW_TYPE_CONNECT = 0;
    private int VIEW_TYPE_DISCONNECT = 1;
    private List<Tweak> item;
    Context context;

    public TweakAdapter(Context context, List<Tweak> data) {
        this.context = context;
        this.item = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_CONNECT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connect_item_layout, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.disconnect_item_layout, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tweak_name.setText(item.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public void setItemType(int type) {
        this.VIEW_TYPE_DISCONNECT = type;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tweak_name,connect;
        public MyViewHolder(View itemView) {
            super(itemView);
            tweak_name = itemView.findViewById(R.id.tweak_name);
            connect = itemView.findViewById(R.id.connect);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setItemType(VIEW_TYPE_DISCONNECT);
                    notifyItemChanged(itemView.getVerticalScrollbarPosition());
                }
            });

        }
    }

}