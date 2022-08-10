package com.sayed.packingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sayed.packingsolution.adapter.ViewAllAdapter;
import com.sayed.packingsolution.adapter.ViewIncAdapter;
import com.sayed.packingsolution.dataholder.DataHolder;

public class IncompleteActivity extends AppCompatActivity {


    BottomNavigationView bottomNavContainer;


    private RecyclerView recView;
    private EditText searchtext;
    public ViewIncAdapter viewIncAdapter;




    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference post_node = db.getReference("post");
    private final DatabaseReference settings_node = db.getReference("setting");


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomplete);




        searchtext = findViewById(R.id.search_txt_i);
        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(searchtext.getText().length()!=0){
                    String query = searchtext.getText().toString();
                    searchbyDesc(query);
                }
                else if(searchtext.getText().length()==0){
                    loadIncData();

                }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        //Bottom Menu
        bottomNavContainer = findViewById(R.id.bottomContainer);
        bottomNavContainer.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;

                    case R.id.menu_setting:
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                }
                return true;
            }
        });




        loadIncData();

    }


    void loadIncData(){
        recView = findViewById(R.id.recview_inc_post);
        recView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<DataHolder> options = new FirebaseRecyclerOptions.Builder<DataHolder>()
                .setQuery(post_node.orderByChild("status").equalTo("a_incomplete"), DataHolder.class)
                .build();


        viewIncAdapter = new ViewIncAdapter(options);

        viewIncAdapter.startListening();
        recView.setAdapter(viewIncAdapter);
    }


    void searchbyDesc(String query){

        FirebaseRecyclerOptions<DataHolder> options =
                new FirebaseRecyclerOptions.Builder<DataHolder>()
                        .setQuery(post_node.orderByChild("description").startAt(query).endAt(query+"~"), DataHolder.class)
                        .build();


        viewIncAdapter = new ViewIncAdapter(options);
        viewIncAdapter.startListening();
        recView.setAdapter(viewIncAdapter);

    }


}