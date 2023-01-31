package com.example.juegoparejas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int[] imgViewArray = {R.id.imgView1, R.id.imgView2, R.id.imgView3, R.id.imgView4, R.id.imgView5,
            R.id.imgView6, R.id.imgView7, R.id.imgView8, R.id.imgView9, R.id.imgView10, R.id.imgView11, R.id.imgView12,
            R.id.imgView13, R.id.imgView14, R.id.imgView15, R.id.imgView16, R.id.imgView17, R.id.imgView18, R.id.imgView19,
            R.id.imgView20};

    private static final int[] imgNumeros = {R.drawable._0_, R.drawable._1_, R.drawable._2_, R.drawable._3_,
            R.drawable._4_, R.drawable._5_, R.drawable._6_, R.drawable._7_, R.drawable._8_,
            R.drawable._9_};

    private static final int[] posicionPartida = new int[imgViewArray.length];
    private ImageView cartas[] = new ImageView[imgViewArray.length];

    private static int destapada[] = new int[20];

    private static int imagenAnteriorDestapada = 0;
    private static int posicionAnteriorDestapada = 99;
    private ImageView imagenViewAnteriorDestapada= null;
    public static int puntuacion = 0;
    public static int puntuacionTiempo = 1;
    public static int cartasAcertadas = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        puntuacion = 0;
        puntuacionTiempo = 1;
        cartasAcertadas = 0;
        barajea();
        iniZERO();
        inicia();

        final TextView txtPuntuacion = findViewById(R.id.txtPuntuacion);
        final TextView txtTiempo = findViewById(R.id.txtTiempo1);
        final Button bntIniciar = findViewById(R.id.btnEnviar);
        //Contador en Segundos

        bntIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bntIniciar.setEnabled(false);
                new CountDownTimer(120000, 1000){
                    private boolean mensajeEmergente = true;
                    @Override
                    public void onTick(long milisegundosFinalizados) {
                        txtTiempo.setText("Tiempo: " + milisegundosFinalizados / 1000 + "s");
                        if(cartasAcertadas == 10){
                            puntuacionTiempo = Math.round(milisegundosFinalizados / 1000);
                            cancel();
                            txtPuntuacion.setText("Puntuacion: " + puntuacion);
                            bntIniciar.setEnabled(true);
                            reiniciarActivity(MainActivity.this);
                            Intent intento = new Intent(MainActivity.this, DatosJugador.class);
                            startActivity(intento);
                            //puntuacion = puntuacionTiempo + puntuacion;
                        }
                    }

                    @Override
                    public void onFinish() {
                        txtTiempo.setText("!Se termino el tiempo¡");
                        puntuacionTiempo = 0;
                        reiniciarActivity(MainActivity.this);
                        Intent intento = new Intent(MainActivity.this, DatosJugador.class);
                        startActivity(intento);
                        bntIniciar.setEnabled(true);
                    }
                }.start();


                for( int nn = 0; nn<imgViewArray.length; nn++){
                    cartas[nn] = (ImageView)findViewById(imgViewArray[nn]);
                    cartas[nn].setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View viewMiCarta) {

                            int posEnArray = damePos(viewMiCarta.getId());

                            if (cartasDestapadas() < 2 ) {
                                // atención aquí a los paréntesis
                                ((ImageView) viewMiCarta).setImageResource(posicionPartida[posEnArray]);
                                destapada[posEnArray] = 1;

                                if (imagenAnteriorDestapada == posicionPartida[posEnArray] && cartasDestapadas() ==2) {
                                    new CountDownTimer(1000, 1000) {
                                        public void onTick(long millisUntilFinished) {  }
                                        public void onFinish() {
                                            ((ImageView) viewMiCarta).setImageResource(R.drawable._acierto_);
                                            cartasAcertadas ++;
                                            puntuacion += 5;
                                            txtPuntuacion.setTextColor(Color.rgb(0, 146, 20));
                                            txtPuntuacion.setText("Puntuacion: " + puntuacion);
                                            //imagenViewAnteriorDestapada.setImageResource(R.drawable._acierto_);
                                        }
                                    }.start();

                                    //((ImageView) viewMiCarta).setImageResource(R.drawable._acierto_);
                                    imagenViewAnteriorDestapada.setImageResource(R.drawable._acierto_);

                                    destapada[posEnArray] = 2;
                                    destapada[posicionAnteriorDestapada] = 2;
                                }

                                // almaceno el id de la imagen destapada. En este caso se refiere a la foto, es decir al valor que está mostrando
                                imagenAnteriorDestapada = posicionPartida[posEnArray];
                                // almaceno la posición que estaba destapada
                                posicionAnteriorDestapada = posEnArray;
                                // almaceno el ImageView que estaba destapado... creo que esto lo puedo simplificar
                                imagenViewAnteriorDestapada = (ImageView) viewMiCarta;
                            }
                            else {      // si no hay menos de dos cartas destapadas se inicia() la baraja. Se refiere a q se tapan todas las cartas
                                inicia();
                                puntuacion -= 1;
                                txtPuntuacion.setTextColor(Color.rgb(255, 0, 0));
                                txtPuntuacion.setText("Puntuacion: " + puntuacion);
                            }
                        }
                    });     // Cierre del método .setOnClickListener()
                }
            }
        });
    }

    //reinicia una Activity
    public void reiniciarActivity(Activity actividad){
        Intent intent=new Intent();
        intent.setClass(actividad, actividad.getClass());
        //llamamos a la actividad
        actividad.startActivity(intent);
        //finalizamos la actividad actual
        actividad.finish();
    }

    private void barajea(){
        // el tamaño de la baraja es 10... en este ejemplo
        // de momento no barajeo para saber donde están las parejas
        Random r = new Random();
        ArrayList<Integer> lista2 = new ArrayList<>();
        for(int j = 0; j < imgNumeros.length; j++) {
            posicionPartida[j] = imgNumeros[j];
        }
        // relleno de la segunda mitad
        for(int j = 10; j < imgNumeros.length *2; j++) {
            posicionPartida[j] = imgNumeros[19-j];
        }

        for (int j = 0; j < posicionPartida.length; j++) {
            lista2.add(posicionPartida[j]);
        }
        Collections.shuffle(lista2);
        for (int j = 0; j < posicionPartida.length; j++) {
            posicionPartida[j]= lista2.get(j);
        }

    }

    /*
        Función que devuelve la posición de la carta pulsada
        Se compara el id del ImageView recibido con cada uno de los almacenados
        en el array idArray
     */
    private int damePos(int pIdObjeto) {
        int ii = 0;
        while (pIdObjeto != imgViewArray[ii++]);     // doy por hecho que encuentro el id... si no DESBORDAMIENTO
        return ii-1;
    }

    /*
        devuelve el número de cartas destapadas
     */
    private int cartasDestapadas(){
        int contador = 0;
        for(int ii = 0; ii < destapada.length; ii++){
            if (destapada[ii] ==1) contador++;
        }
        return contador;
    }

    /*
        En realidad inica o reinicia las cartas del reverso
     */
    private void inicia(){
        for(int n = 0; n < 20; n++) {
            if (destapada[n] == 1) {
                cartas[n] = (ImageView) findViewById(imgViewArray[n]);    // accedo a cada objeto ImageView mediante su id previamente almacenado en idArray[]
                cartas[n].setImageResource(R.drawable.rever00);      // establezco en cada ImageView (carta[n]) la imagen del reverso
                destapada[n] = 0;
            }
        }
    }

    /*
        Pone el array destapada todos a 1
        Como el layout está construido con las imágenes de las cartas destapadas... primero relleno la matriz de estado con 1
        para que la función inicia() las tape (cargando la imagen del corcho)
     */
    private void iniZERO(){
        for(int n = 0; n < 20; n++) destapada[n] = 1;
    }
}