package com.pingtung.ccstudio.notepadcloud;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by crowd_000 on 10/6/2016.
 */

public class ItemPlusAdapter extends ArrayAdapter<ItemPlus> {
    private Context context;
    private int resource;
    private List<ItemPlus> list;
    private LayoutInflater layoutInflater;

    public ItemPlusAdapter(Context context, int resource, List<ItemPlus> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView;
        ViewHolder viewHolder;
        ItemPlus itemPlus = list.get(position);

        if(convertView==null){
            itemView = layoutInflater.inflate(resource,null);

            viewHolder = new ViewHolder((TextView) itemView.findViewById(R.id.tvTitle),
                    (TextView) itemView.findViewById(R.id.tvContent));

            itemView.setTag(viewHolder);
        }else{
            itemView = convertView;
            viewHolder = (ViewHolder) itemView.getTag();
        }

        viewHolder.tvTitle.setText(itemPlus.getTitle());
        viewHolder.tvContent.setText(itemPlus.getContent());

        return itemView;
    }

    private class ViewHolder{
        TextView tvTitle,tvContent;

        public ViewHolder(TextView tvTitle,TextView tvContent){
            this.tvTitle = tvTitle;
            this.tvContent = tvContent;
        }
    }
}
