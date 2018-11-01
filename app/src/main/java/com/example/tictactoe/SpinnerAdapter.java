package com.example.tictactoe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter {
    String[] spinnerlabels;
    int[] spinnerImages;
    Context mContext;


    public SpinnerAdapter(@NonNull Context context,String[] labels, int[] symbols) {
        super(context, R.layout.spinner_row);
        this.spinnerlabels = labels;
        this.spinnerImages = symbols;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return spinnerlabels.length;
    }



    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_row, parent, false);
            mViewHolder.mSymbol = (ImageView) convertView.findViewById(R.id.symbol);
            mViewHolder.mSymbolName = (TextView) convertView.findViewById(R.id.symbolName);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mSymbol.setImageResource(spinnerImages[position]);
        mViewHolder.mSymbolName.setText(spinnerlabels[position]);


        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder{
        ImageView mSymbol;
        TextView mSymbolName;
    }
}
