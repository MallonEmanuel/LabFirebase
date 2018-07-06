package com.firebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by emanuel on 05/07/18.
 */
public class RatingActivity extends BaseActivity {
    private static String TAG = RatingActivity.class.getSimpleName();

    private CuotaDeInspiracion cuota;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String email;

    private EditText fraseEditText;
    private EditText autorEditText;
    private EditText propietarioEditText;
    private RatingBar ratingBar;
    private RatingBar ratingBar2;
    private TextView votosTextViewNumeric;
    private Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);


        fraseEditText = (EditText) findViewById(R.id.frase);
        autorEditText = (EditText) findViewById(R.id.autor);
        propietarioEditText = (EditText) findViewById(R.id.propietario);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);

        votosTextViewNumeric = (TextView) findViewById(R.id.votosTextViewNumeric);
        button = (Button) findViewById(R.id.button);

        email = getIntent().getExtras().getString("email");
        cuota = (CuotaDeInspiracion) getIntent().getExtras().getSerializable("cuota");
        Voto voto = (Voto) getIntent().getExtras().getSerializable("voto");
        setRating();
        if(voto != null){
            button.setVisibility(View.GONE);
            ratingBar2.setEnabled(false);
            ratingBar2.setRating(voto.getVoto());
            Toast.makeText(getApplicationContext(),"Usted voto esta Nota anteriormente, ya no puede votar.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Usted puede votar en esta Nota.",Toast.LENGTH_SHORT).show();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });
    }

    private void setRating(){
        autorEditText.setEnabled(false);
        autorEditText.setText(cuota.getAutor());
        fraseEditText.setEnabled(false);
        fraseEditText.setText(cuota.getFrase());
        propietarioEditText.setEnabled(false);
        propietarioEditText.setText(cuota.getPropietario());
        ratingBar.setEnabled(false);
        if(cuota.getVotos()==0 || cuota.getCalificacion()==0){
            ratingBar.setRating(0);
        }else {
            ratingBar.setRating((float)(cuota.getCalificacion()/cuota.getVotos()));
        }


        ratingBar2.setVisibility(View.VISIBLE);
        ratingBar2.setEnabled(true);
        ratingBar2.setStepSize(1);
        votosTextViewNumeric.setText(String.valueOf(cuota.getVotos()));
        button.setVisibility(View.VISIBLE);
    }

    private void save(View view){
        if(ratingBar2.getRating() == 0){
            Toast.makeText(getApplicationContext(),"Por favor califique la Nota.",Toast.LENGTH_SHORT).show();
            return;
        }

        cuota.setCalificacion((int) (cuota.getCalificacion()+ratingBar2.getRating()));
        cuota.setVotos(cuota.getVotos()+1);
        showProgressDialog();
        String key = cuota.getPropietario()+"_"+cuota.getFecha();

        db.collection("ejemplo").document(key).set(cuota).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Guardado exitosamente",Toast.LENGTH_SHORT).show();
                hideProgressDialog();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        Map<String , Object> data = new HashMap<>();
        data.put("email", email);
        data.put("voto", ratingBar2.getRating());
        db.collection("ejemplo").document(key).collection("votantes").document(email).set(data);
    }

}
