package com.example.tareavideos;

import android.content.Intent;
import android.os.Bundle;

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

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar RecyclerView
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
            // Pasar información del ítem seleccionado a DetailActivity
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("type", item.getType());
            intent.putExtra("url", item.getUrl());
            startActivity(intent);
        });

        recyclerView.setAdapter(itemAdapter);
    }
}