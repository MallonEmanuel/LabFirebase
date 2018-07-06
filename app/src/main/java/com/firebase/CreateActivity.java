package com.firebase;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firetest.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends BaseActivity {
    private static String TAG = CreateActivity.class.getSimpleName();

    private EditText fraseEditText;
    private EditText autorEditText;
    private EditText propietarioEditText;
    private RatingBar ratingBar;
    private TextView votosTextViewNumeric;
    private Button button;

    private TextView propietarioTextView;
    private TextView ratingTextView;
    private TextView votosTextView;

    private CuotaDeInspiracion cuota;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        fraseEditText = (EditText) findViewById(R.id.frase);
        autorEditText = (EditText) findViewById(R.id.autor);
        propietarioEditText = (EditText) findViewById(R.id.propietario);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        votosTextViewNumeric = (TextView) findViewById(R.id.votosTextViewNumeric);
        button = (Button) findViewById(R.id.button);

        propietarioTextView = (TextView) findViewById(R.id.propietarioTextView);
        ratingTextView = (TextView) findViewById(R.id.calificacionTextView);
        votosTextView = (TextView) findViewById(R.id.votosTextView);

        email = getIntent().getExtras().getString("email");

        cuota = (CuotaDeInspiracion) getIntent().getExtras().getSerializable("cuota");
        if(cuota != null){
            Log.e(TAG,"Cuota: "+cuota);
            setView();
            Toast.makeText(getApplicationContext(),"Usted creo esta Nota, solo puede ver su calificación.",Toast.LENGTH_SHORT).show();
        }else{
            setSave();
            Toast.makeText(getApplicationContext(),"Usted debe crear una Nota.",Toast.LENGTH_SHORT).show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });
    }

    private void setSave() {
        autorEditText.setEnabled(true);
        fraseEditText.setEnabled(true);
        propietarioTextView.setVisibility(View.GONE);
        propietarioEditText.setVisibility(View.GONE);
        ratingTextView.setVisibility(View.GONE);
        ratingBar.setVisibility(View.GONE);
        votosTextView.setVisibility(View.GONE);
        votosTextViewNumeric.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }

    private void setView(){
        autorEditText.setEnabled(false);
        autorEditText.setText(cuota.getAutor());
        fraseEditText.setEnabled(false);
        fraseEditText.setText(cuota.getFrase());
        propietarioEditText.setEnabled(false);
        propietarioEditText.setText(cuota.getPropietario());
        if(cuota.getVotos() == 0 || cuota.getCalificacion()==0){
            ratingBar.setRating(0);
        }else {

            ratingBar.setRating(cuota.getCalificacion()/cuota.getVotos());
        }
        ratingBar.setVisibility(View.VISIBLE);
        ratingBar.setEnabled(false);
        votosTextViewNumeric.setText(String.valueOf(cuota.getVotos()));
        button.setVisibility(View.GONE);
    }

    public void save(View view){
        if(autorEditText.getText().toString().isEmpty() || fraseEditText.toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Por favor agregue un autor y su frase.",Toast.LENGTH_SHORT).show();
            return;
        }

        Date fecha = new Date();
        String key = email+ "_"+fecha.toString();
        Map<String , Object> data = new HashMap<>();
        data.put("autor", autorEditText.getText().toString());
        data.put("frase", fraseEditText.getText().toString());
        data.put("propietario",email);
        data.put("calificacion", 0);
        data.put("votos", 0);
        data.put("fecha", fecha.toString());
//        db.collection("ejemplo").document().set(cuota).;

        showProgressDialog();
        db.collection("ejemplo").document(key).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Guardado exitosamente",Toast.LENGTH_SHORT).show();
                hideProgressDialog();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error al guardar",Toast.LENGTH_SHORT).show();
            }
        });

//        db.collection("ejemplo").document(key).set(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Toast.makeText(getApplicationContext(),"Guardado exitosamente",Toast.LENGTH_SHORT).show();
//                hideProgressDialog();
//                finish();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(),"Error al guardar",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

}


