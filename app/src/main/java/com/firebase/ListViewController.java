package com.firebase;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Esta clase ayuda a controlar una lista
 */
public class ListViewController {

    private ListView listView;
    private ArrayAdapter adapter;

    public ListViewController(final Context context, ListView listView, List list) {
        this.listView = listView;
        this.adapter = new ItemArrayAdapter(context, list);
        this.listView.setAdapter(adapter);
    }

    public ListView getListView() {
        return listView;
    }

    public ArrayAdapter getAdapter() {
        return adapter;
    }
}
