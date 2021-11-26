package com.example.asteroides;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Puntuacions extends ListActivity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuacions);
        setListAdapter(new ElMeuAdaptador(this, MainActivity.magatzem.llistaPuntuacions(10)));
    }
    @Override protected void onListItemClick(ListView listView,
                                             View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Object o = getListAdapter().getItem(position);
        Toast.makeText(this, "Selecció: " + Integer.toString(position)

                + " - " + o.toString(),Toast.LENGTH_LONG).show();
    }
}