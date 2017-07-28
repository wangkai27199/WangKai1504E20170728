package com.baway.wangkai;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by DELL on 2017/7/28.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.IViewHolder> {

    private Context context;
    private List<Bean.DataBean> list;
    private onItemClickListener listener;

    public MyAdapter(Context context, List<Bean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public IViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        IViewHolder viewHolder = new IViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IViewHolder holder, final int position) {
        //加载图片
        ImageLoader.getInstance().displayImage(list.get(position).img, holder.itemImage);
        //给控件赋值
        holder.itemUserName.setText(list.get(position).userName + "| 已实名认证");
        holder.itemAge.setText(list.get(position).userAge + "");
        holder.itemOccupation.setText(list.get(position).occupation);
        holder.itemIntroduction.setText(list.get(position).introduction);
        //设置监听事件
        holder.itemIntroduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClickListener(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class IViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemUserName;
        TextView itemAge;
        TextView itemOccupation;
        TextView itemIntroduction;

        public IViewHolder(View view) {
            super(view);
            itemImage = (ImageView) view.findViewById(R.id.item_image);
            itemUserName = (TextView) view.findViewById(R.id.item_username);
            itemAge = (TextView) view.findViewById(R.id.item_age);
            itemOccupation = (TextView) view.findViewById(R.id.item_occupation);
            itemIntroduction = (TextView) view.findViewById(R.id.item_introduction);
        }
    }

    //自定义接口
    public interface onItemClickListener{
        void onItemClickListener(View view,int position);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
}
