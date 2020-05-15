package com.dh.testproject.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dh.testproject.R;
import com.dh.testproject.utils.Constants;

import java.util.List;
import java.util.Map;

public class RvViewpagerAdapter extends RecyclerView.Adapter<RvViewpagerAdapter.ViewHolder> implements View.OnClickListener {
    private List<Map<String, Object>> list;
    private OnItemClickListener listener;

    public RvViewpagerAdapter(List<Map<String, Object>> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> map = list.get(position);
        holder.tvTitle.setText((CharSequence) map.get(Constants.TITLE_KEY));

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (listener != null) {
            Log.e("onClick", "点击位置： " + position);
            listener.onItemClick(position, (Intent) list.get(position).get(Constants.INTENT_KEY));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Intent intent);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
