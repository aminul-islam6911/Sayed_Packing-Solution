package com.sayed.packingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sayed.packingsolution.adapter.ViewAllAdapter;
import com.sayed.packingsolution.dataholder.DataHolder;

public class ViewAllActivity extends AppCompatActivity {

    BottomNavigationView bottomNavContainer;
    private RecyclerView recView;
    private EditText searchtext;
    public ViewAllAdapter viewAllAdapter;


    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference post_node = db.getReference("post");
    private final DatabaseReference settings_node = db.getReference("settings");

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);





        //Bottom Menu
        bottomNavContainer = findViewById(R.id.bottomContainer);
        bottomNavContainer.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        break;
                }
                return true;
            }
        });


        recView = findViewById(R.id.recview_view_post);
        recView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<DataHolder> options = new FirebaseRecyclerOptions.Builder<DataHolder>()
                        .setQuery(post_node.orderByChild("status"), DataHolder.class)
                        .build();


        viewAllAdapter = new ViewAllAdapter(options);
        viewAllAdapter.startListening();
        recView.setAdapter(viewAllAdapter);




    }
}