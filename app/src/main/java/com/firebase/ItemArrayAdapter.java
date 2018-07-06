package com.firebase;

/**
 * Created by Ema on 03/10/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firetest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase se ocupa de adaptar una lista de Listable, para brindar una vista amigable para el usuario.
 *
 */
public class ItemArrayAdapter extends ArrayAdapter<CuotaDeInspiracion> {
    private List items;

    public ItemArrayAdapter(Context context, List<CuotaDeInspiracion> objects) {
        super(context, 0, objects);
        items = new ArrayList<CuotaDeInspiracion>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            listItemView = inflater.inflate(
                    R.layout.list_item, parent, false);
        }

        //Obteniendo instancias de los elementos
        TextView title = (TextView) listItemView.findViewById(R.id.title);
        TextView subtitle = (TextView) listItemView.findViewById(R.id.subtitle);
        ImageView img = (ImageView) listItemView.findViewById(R.id.img);

        //Obteniendo instancia de la Commerce en la posición actual
        CuotaDeInspiracion item = getItem(position);

        title.setText(item.getFrase());
        subtitle.setText(item.toString());

        //Devolver al ListView la fila creada
        return listItemView;
    }
}