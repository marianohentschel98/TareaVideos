package com.example.tareavideos;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Crear lista de ítems
        itemList = new ArrayList<>();
        itemList.add(new Item("Cristiano Ronaldo", "video", "android.resource://" + getPackageName() + "/" + R.raw.elbicho));
        itemList.add(new Item("Real Madrid", "video", "android.resource://" + getPackageName() + "/" + R.raw.remontada));
        itemList.add(new Item("Xokas", "audio", "android.resource://" + getPackageName() + "/" + R.raw.quequieresquehagapereira));
        itemList.add(new Item("Gato", "audio", "android.resource://" + getPackageName() + "/" + R.raw.gatito));
        itemList.add(new Item("Google", "web", "https://www.google.com"));
        itemList.add(new Item("YouTube", "web", "https://www.youtube.com"));

        // Configurar adaptador
        itemAdapter = new ItemAdapter(itemList, item -> {
            // Aquí, en lugar de abrir una nueva actividad, vamos a abrir el diálogo
            showContentDialog(item);
        });

        recyclerView.setAdapter(itemAdapter);
    }

    private void showContentDialog(Item item) {
        // Crear un diálogo personalizado
        final Dialog contentDialog = new Dialog(this);
        contentDialog.setContentView(R.layout.dialog_content);  // Layout que contiene los elementos (video, audio, etc.)
        contentDialog.setCancelable(true);

        // Obtener las vistas del diálogo
        VideoView videoView = contentDialog.findViewById(R.id.videoView);
        WebView webView = contentDialog.findViewById(R.id.webView);
        Button closeButton = contentDialog.findViewById(R.id.closeButton);

        videoView.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);

        if ("video".equals(item.getType())) {
            // Si es un video, configuramos el VideoView
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(item.getUrl()));
            videoView.start();
        } else if ("audio".equals(item.getType())) {
            // Si es un audio, configuramos el MediaPlayer
            mediaPlayer = MediaPlayer.create(this, Uri.parse(item.getUrl()));
            mediaPlayer.start();
        } else if ("web".equals(item.getType())) {
            // Si es un enlace web, mostramos en el WebView
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(item.getUrl());
        }

        // Configurar el botón de cierre
        closeButton.setOnClickListener(v -> {
            if ("audio".equals(item.getType()) && mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            contentDialog.dismiss();
        });

        // Mostrar el diálogo
        contentDialog.show();
    }
}