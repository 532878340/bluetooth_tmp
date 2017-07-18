package com.kotlin.mvpframe.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kotlin.mvpframe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zijin on 2017/7/14.
 */

public class SampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> list;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE = 2;

    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = PULLUP_LOAD_MORE;


    public SampleAdapter(List<String> list) {
        this.list = new ArrayList<>();
        this.list.addAll(list);
    }

    public void addAll(List<String> data) {
        this.list.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //正常加载
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new FooterViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            //处理布局相关
            ((ItemViewHolder)holder).id_num.setText(list.get(position));

        } else if (holder instanceof FooterViewHolder) {
            switch (mLoadMoreStatus) {
                case PULLUP_LOAD_MORE:
                    ((FooterViewHolder) holder).name.setText("上拉加载更多...");
                    ((FooterViewHolder) holder).layout.setVisibility(View.VISIBLE);
                    break;
                case LOADING_MORE:
                    ((FooterViewHolder) holder).name.setText("正加载更多...");
                    ((FooterViewHolder) holder).layout.setVisibility(View.VISIBLE);
                    break;
                case NO_LOAD_MORE:
                    //隐藏加载更多
                    ((FooterViewHolder) holder).layout.setVisibility(View.GONE);
                    break;
            }
        }
    }

    /**
     * 更新加载更多状态
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        mLoadMoreStatus = status;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position + 1 == getItemCount() ? TYPE_FOOTER : TYPE_ITEM;
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        View layout;

        public FooterViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            name = itemView.findViewById(R.id.name);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView id_num;

        public ItemViewHolder(View itemView) {
            super(itemView);

            id_num = itemView.findViewById(R.id.id_num);
        }
    }
}
