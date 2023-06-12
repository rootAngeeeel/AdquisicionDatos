package com.example.adquisiciondatos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor acce;
    private Sensor gyros;
    private TextView a,g;
    private Button btn;
    private FileWriter fw;
    private boolean grabar = false;
    private long tiempo = 0;
    private float ax,ay,az,gx,gy,gz;
    String fila;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a = findViewById(R.id.Acelerometro);
        g = findViewById(R.id.Giroscopio);
        btn = findViewById(R.id.btn_grabar);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acce = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyros = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        fila = null;

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "DatosAG.csv");
            fw = new FileWriter(file);
            fw.append("Date,").append(" Time,").append(" Ax,").append(" Ay,").append(" Az,").append(" Gx,").append(" Gy,").append(" Gz,").append(" L").append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        btn.setOnClickListener(view -> {
            grabar = !grabar;
            if(grabar){
                tiempo = System.currentTimeMillis();
                btn.setText("Detener");

            }else {
                try {
                    fw.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Archivo Guardado con Exito", Toast.LENGTH_SHORT).show();
                btn.setText("Guardar");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, acce, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, gyros, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            ax = sensorEvent.values[0];
            ay = sensorEvent.values[1];
            az = sensorEvent.values[2];
            @SuppressLint("DefaultLocale") String acele = "Ax: " + String.format("%.2f", ax) + "  Ay:" + String.format("%.2f", ay) + "  Az:" + String.format("%.2f", az);
            a.setText(acele);

        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gx = sensorEvent.values[0];
            gy = sensorEvent.values[1];
            gz = sensorEvent.values[2];
            @SuppressLint("DefaultLocale") String giros = "Gx: " + String.format("%.2f", gx) + "  Gy: " + String.format("%.2f", gy) + "  Gz: " + String.format("%.2f", gz);
            g.setText(giros);
        }

        if(grabar){
            String date = obtenerFechaHora();
            String hh = obtenerHora();
            fila = date + ", " + hh + ", " + ax + ", " + ay + ", " + az + ", " + gx + ", " + gy + ", " + gz + ", " + "L";

            if(fw != null) {
                try{
                    fw.append(fila).append("\n");
                }catch (IOException e){
                    e.printStackTrace();

                }
            }
        }
    }

    private String obtenerFechaHora() {
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int anio = calendar.get(Calendar.YEAR);

        return String.format(Locale.getDefault(), "%02d/%02d/%d", dia, mes, anio);
    }

    private String obtenerHora(){
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);
        int segundo = calendar.get(Calendar.SECOND);
        int milisegundo = calendar.get(Calendar.MILLISECOND);

        return String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d", hora, minuto, segundo, milisegundo);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}