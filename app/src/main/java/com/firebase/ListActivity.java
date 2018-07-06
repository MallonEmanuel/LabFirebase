package com.firebase;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firetest.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ListActivity extends BaseActivity {
    private static final String TAG = ListActivity.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private List<CuotaDeInspiracion> list;
    private ListViewController listViewController;
    private String email;
    private CuotaDeInspiracion cuota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        email = getIntent().getExtras().getString("email");

        showProgressDialog();
        db.collection("ejemplo").get().addOnSuccessListener( new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                list = queryDocumentSnapshots.toObjects(CuotaDeInspiracion.class);
                ListView listView = (ListView) findViewById(R.id.listView);
                listViewController = new ListViewController(getApplicationContext(),listView,list);
                listViewController.getAdapter().notifyDataSetChanged();
                hideProgressDialog();
                initListener();
                initControls();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(),"Error al cargar Notas.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initListener(){
        db.collection("ejemplo").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                list.clear();
                list.addAll(queryDocumentSnapshots.toObjects(CuotaDeInspiracion.class));
                listViewController.getAdapter().notifyDataSetChanged();
                Log.e(TAG, list.toString());
            }
        });
    }

    private  void initControls(){
        listViewController.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == -1){
                    return;
                }
                 cuota = (CuotaDeInspiracion) adapterView.getAdapter().getItem(i);

                if(cuota.getPropietario().compareTo(email) == 0){
                    Intent intent = new Intent(ListActivity.this, CreateActivity.class);
                    intent.putExtra("cuota",cuota);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }else {

                    String key = cuota.getPropietario()+"_"+cuota.getFecha();
                    db.collection("ejemplo").document(key).collection("votantes").whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.e(TAG, String.valueOf(queryDocumentSnapshots.getDocuments().isEmpty()));
                            Intent intent = new Intent(ListActivity.this, RatingActivity.class);
                            intent.putExtra("cuota",cuota);
                            intent.putExtra("email", email);
                            if(!queryDocumentSnapshots.isEmpty()){
                                List<Voto>list = queryDocumentSnapshots.toObjects(Voto.class);
                                intent.putExtra("voto",list.get(0));
                            }
                            startActivity(intent);
                        }
                    });

                }
            }
        });
    }

    public void addCuota(View view){
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
        Intent intent = new Intent(ListActivity.this, CreateActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
        .setMessage("¿Esta seguro que desea salir?")
        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }})
        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();

    }

}
