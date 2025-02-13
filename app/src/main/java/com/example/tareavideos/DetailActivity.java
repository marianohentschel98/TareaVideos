package com.example.tareavideos;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        playButton.setOnClickListener(v -> mediaPlayer.start());
        pauseButton.setOnClickListener(v -> mediaPlayer.pause());
        stopButton.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            audioDialog.dismiss();
        });

        audioDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
