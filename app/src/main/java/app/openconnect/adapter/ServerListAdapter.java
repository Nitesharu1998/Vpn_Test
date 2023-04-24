package app.openconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.vpnpro.dubaivpn.R;

import java.util.List;
import java.util.Objects;



import org.jetbrains.annotations.NotNull;

import app.openconnect.core.OpenConnectManagementThread;
import app.openconnect.core.OpenVpnService;
import app.openconnect.core.VPNConnector;
import app.openconnect.interfaces.OnItemClickListener;
import app.openconnect.model.Server;
import app.openconnect.utils.ItemAnimation;

public class ServerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private List<Server> itemList;
    private OnItemClickListener mOnItemClickListener;
    private VPNConnector mConn;
    private int mConnectionState = OpenConnectManagementThread.STATE_DISCONNECTED;
    public void setOnConnectClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }


    public ServerListAdapter(Context context, List<Server> itemList, VPNConnector mConn) {
        this.itemList = itemList;
        this.context = context;
        this.mConn = mConn;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View view;
        RecyclerView.ViewHolder holder = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connect_item_layout, parent, false);
        holder = new ServerListAdapter.ViewHolder(view);

        return Objects.requireNonNull(holder);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        ServerListAdapter.ViewHolder view = (ServerListAdapter.ViewHolder) holder;
//            ItemMe item = itemList.get(position);
        ((ServerListAdapter.ViewHolder) holder).setInformation(itemList.get(position), context);
        view.connect.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, itemList.get(position), position);
            }
        });


        Server server = itemList.get(position);

        if (server.isStatus()){
            view.connect.setText("Disconnect");
            view.connect.setBackground(context.getResources().getDrawable(R.drawable.shape_disconnected_));
            view.connect.setTextColor(context.getResources().getColor(R.color.redColor));
        }else{
            view.connect.setBackground(context.getResources().getDrawable(R.drawable.shape_diconnected_back));
            view.connect.setTextColor(context.getResources().getColor(R.color.white));
            view.connect.setText("Connect");
        }

        setAnimation(view.itemView, position);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tweak_name,connect;




        ViewHolder(View itemView) {
            super(itemView);
            tweak_name = itemView.findViewById(R.id.tweak_name);
            connect = itemView.findViewById(R.id.connect);

        }

        void setInformation(Server item, Context context) {
            tweak_name.setText(item.getName());
        }

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    private int lastPosition = -1;
    private boolean on_attach = true;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, ItemAnimation.FADE_IN);
            lastPosition = position;
        }
    }

}
