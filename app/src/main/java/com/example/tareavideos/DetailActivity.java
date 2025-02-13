package com.example.tareavideos;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private String title, type, url;
    private MediaPlayer mediaPlayer;
    private VideoView videoView;
    private WebView webView;
    private Dialog audioDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Obtener datos del intent
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("url");

        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(title);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        videoView = findViewById(R.id.videoView);
        webView = findViewById(R.id.webView);

        if ("video".equals(type)) {
            setupVideoPlayer();
        } else if ("audio".equals(type)) {
            showAudioDialog();
        } else if ("web".equals(type)) {
            setupWebView();
        }
    }

    // Configura el VideoView con MediaController
    private void setupVideoPlayer() {
        videoView.setVisibility(View.VISIBLE);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(url));
        videoView.start();
    }

    // Configura el WebView para mostrar la página web
    private void setupWebView() {
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    // Muestra un diálogo para controlar el audio
    private void showAudioDialog() {
        audioDialog = new Dialog(this);
        audioDialog.setContentView(R.layout.dialog_audio);
        audioDialog.setCancelable(true);

        Button playButton = audioDialog.findViewById(R.id.playButton);
        Button pauseButton = audioDialog.findViewById(R.id.pauseButton);
        Button stopButton = audioDialog.findViewById(R.id.stopButton);

        // Obtener el ID del recurso de audio
        int resID = getResources().getIdentifier(url, "raw", getPackageName());

        if (resID == 0) {
            Log.e("DetailActivity", "No se encontró el archivo de audio: " + url);
            return;
        } else {
            Log.d("DetailActivity", "Archivo de audio encontrado: " + url);
        }

        // Crear el MediaPlayer con el recurso
        mediaPlayer = MediaPlayer.create(this, resID);

        if (mediaPlayer == null) {
            Log.e("DetailActivity", "Error al inicializar MediaPlayer");
            return;
        } else {
            Log.d("DetailActivity", "MediaPlayer inicializado correctamente");
        }

        // Asegurar volumen máximo
        mediaPlayer.setVolume(1.0f, 1.0f);

        // Reproducir el audio cuando el botón de play se presione
        playButton.setOnClickListener(v -> {
            if (!mediaPlayer.isPlaying()) {
                Log.d("DetailActivity", "Reproduciendo audio");
                mediaPlayer.start();
            }
        });

        // Pausar el audio cuando se presione el botón de pause
        pauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                Log.d("DetailActivity", "Pausando audio");
                mediaPlayer.pause();
            }
        });

        // Detener el audio cuando se presione el botón de stop
        stopButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                Log.d("DetailActivity", "Deteniendo audio");
                mediaPlayer.stop();
            }
            mediaPlayer.release();  // Liberar el MediaPlayer después de detenerlo
            mediaPlayer = null;
            audioDialog.dismiss();  // Cerrar el diálogo
        });

        // Agregar un listener para liberar el MediaPlayer cuando el audio termine
        mediaPlayer.setOnCompletionListener(mp -> {
            Log.d("DetailActivity", "Audio completado");
            mp.release();  // Liberar recursos
            mediaPlayer = null;
        });

        // Mostrar el diálogo
        audioDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Asegurarse de liberar el MediaPlayer cuando la actividad se destruya
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
