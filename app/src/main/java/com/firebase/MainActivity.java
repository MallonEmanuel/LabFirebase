package com.firebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firetest.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();

    private DocumentReference mDocRed = FirebaseFirestore.getInstance().collection("ejemplo").document("inspiration");
    private TextView mTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView);

    }

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("ejemplo").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                List<CuotaDeInspiracion> list = queryDocumentSnapshots.toObjects(CuotaDeInspiracion.class);
                Log.e(TAG,list.toString());
            }
        });

        mDocRed.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
                if(documentSnapshot.exists()){
                    CuotaDeInspiracion cuota = documentSnapshot.toObject(CuotaDeInspiracion.class);
                    mTextView.setText(cuota.toString());
                }
            }
        });
    }

    public void fetch(View view){
        mDocRed.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
                if(documentSnapshot.exists()){
                    CuotaDeInspiracion cuota = documentSnapshot.toObject(CuotaDeInspiracion.class);
                    mTextView.setText(cuota.toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error al leer la BD",Toast.LENGTH_LONG).show();
                Log.e(TAG,"Error al leer la bd...");
            }
        });
    }

    public void save(View view){
        EditText autorT = (EditText) findViewById(R.id.autor);
        EditText fraseT = (EditText) findViewById(R.id.frase);

        Log.e(TAG,"save onClick");
        if(autorT.getText().toString().isEmpty() || fraseT.toString().isEmpty()){
            return;
        }

        Map<String , Object> data = new HashMap<>();

        data.put("autor", autorT.getText().toString());
        data.put("frase", fraseT.getText().toString());

        mDocRed.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG,"Todo OK");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Error");
            }
        });
    }


}
