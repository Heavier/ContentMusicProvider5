package com.example.javi.contentmusicprovider.utils;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.javi.contentmusicprovider.R;
import com.example.javi.contentmusicprovider.cancion.Cancion;

import java.util.ArrayList;

public class Adaptador extends ArrayAdapter<Cancion> {

    ArrayList<Cancion> lista;

    public Adaptador(Context context, ArrayList<Cancion> objects) {
        super(context, R.layout.item, objects);
        this.lista = objects;
    }

    static class ViewHolder {
        public TextView tvInterprete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Cancion actual = lista.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item, parent, false);
            viewHolder.tvInterprete = (TextView) convertView.findViewById(R.id.tvCancion);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvInterprete.setText(actual.getTitulo());

        return convertView;
    }
}