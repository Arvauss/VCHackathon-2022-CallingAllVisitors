package com.example.callingallvisitors.Adapters;

import android.content.Context;
import android.view.View;z
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.callingallvisitors.Models.Visitor;
import com.example.callingallvisitors.StillOnPremises;

import java.util.ArrayList;

public class VisitorListAdAdapter  extends ArrayAdapter<Visitor> {

    private Context mContext;
    int mVisitor_list_template;
    private ArrayList<Visitor> vList = new ArrayList<>();

    public VisitorListAdAdapter(Context context, int visitor_list_template, ArrayList<Visitor> visitorsList){
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



        final View result;

        if (convertView == null){

        }


    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtTime;
    }
}
