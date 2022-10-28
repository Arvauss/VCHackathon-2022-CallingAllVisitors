package com.example.callingallvisitors.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.callingallvisitors.Models.Visitor;
import com.example.callingallvisitors.R;
import com.example.callingallvisitors.StillOnPremises;

import java.util.ArrayList;

public class VisitorListAdapter  extends ArrayAdapter<Visitor> {

    private Context mContext;
    int mVisitor_list_template;
    private ArrayList<Visitor> vList = new ArrayList<>();

    public VisitorListAdapter(Context context, int visitor_list_template, ArrayList<Visitor> visitorsList){
        super(context, visitor_list_template, visitorsList);
        this.mContext = context;
        mVisitor_list_template = visitor_list_template;
        this.vList = visitorsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);

        Visitor v = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.visitor_list_template, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.listitemVisitorName);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.listitemVisitorTime);

            result = convertView;
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtName.setText(v.getName());
        viewHolder.txtTime.setText(v.getDateVisited());


        return result;
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtTime;
    }
}
