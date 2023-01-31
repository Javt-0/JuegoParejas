package com.example.juegoparejas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class DatosJugador extends AppCompatActivity {
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_jugador);

        if (getSupportActionBar() != null)  getSupportActionBar().hide();

        final Button btnEnviar = findViewById(R.id.btnEnviar);
        final EditText editJugador = findViewById(R.id.editJugador);
        final EditText editJuego = findViewById(R.id.editJuego);
        final EditText editTotales = findViewById(R.id.editTotales);
        final EditText editBonus = findViewById(R.id.editBonus);
        final EditText editTiempo = findViewById(R.id.editTiempo);
        final TextView respuestBD = findViewById(R.id.txtRespuestaBD);


        //Para que el usuario no pueda modificar los edittext
        editBonus.setEnabled(false);
        editJuego.setEnabled(false);
        editTotales.setEnabled(false);
        editTiempo.setEnabled(false);

        //Recuperamos valores del main
        editJuego.setText(Integer.toString(MainActivity.puntuacion));
        editTiempo.setText(Integer.toString(MainActivity.puntuacionTiempo));
        editBonus.setText(Integer.toString(MainActivity.puntuacionTiempo * 5));
        editTotales.setText(Integer.toString(MainActivity.puntuacion + (MainActivity.puntuacionTiempo * 5)));

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://digitalgentilis.com/apps/android/actualiza.php";
                StringRequest solicitud = new StringRequest(Request.Method.POST,url,
                        response -> mostrar(response),
                        error -> Toast.makeText(DatosJugador.this, error.toString(), Toast.LENGTH_LONG).show()){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<>();
                        params.put("JU", editJugador.getText().toString());
                        params.put("PT", editTotales.getText().toString());
                        params.put("PJ", editJuego.getText().toString());
                        params.put("BO", editBonus.getText().toString());
                        params.put("TI", editTiempo.getText().toString());
                        return params;

                    }

                };

                requestQueue = Volley.newRequestQueue(DatosJugador.this);
                requestQueue.add(solicitud);

                editJugador.setEnabled(false);
                btnEnviar.setEnabled(false);
            }

        });
    }

    private void mostrar(String r){
        final TextView respuestBD = findViewById(R.id.txtRespuestaBD);
        respuestBD.setMovementMethod(new ScrollingMovementMethod());
        r = r.replace("_", " ");
        respuestBD.setText(r);
    }
}
























