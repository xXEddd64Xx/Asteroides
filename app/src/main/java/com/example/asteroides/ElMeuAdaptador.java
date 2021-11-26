package com.example.asteroides;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ElMeuAdaptador extends BaseAdapter {
    private final Activity activitat;
    private final List<String> llista;

    public ElMeuAdaptador(Activity activitat, List<String>
            llista) {
        super();
        this.activitat = activitat;
        this.llista = llista;
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {

        LayoutInflater inflater =
                activitat.getLayoutInflater();
        View view = inflater.inflate(R.layout.element_llista,
                null, true);
        TextView textView
                =(TextView)view.findViewById(R.id.titol);
        textView.setText(llista.get(position));
        ImageView
                imageView=(ImageView)view.findViewById(R.id.icono);
        switch (Math.round((float)Math.random()*3)){
            case 0:
                imageView.setImageResource(R.drawable.asteroide1);
                break;
            case 1:
                imageView.setImageResource(R.drawable.asteroide2);
                break;
            default:
                imageView.setImageResource(R.drawable.asteroide3);
                break;
        }
        return view;
    }

    public int getCount() {
        return llista.size();
    }

    public Object getItem(int arg0) {

        return llista.get(arg0);
    }

    public long getItemId(int position) {
        return position;
    }
}