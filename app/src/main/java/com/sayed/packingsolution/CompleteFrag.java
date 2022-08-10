package com.sayed.packingsolution;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sayed.packingsolution.adapter.ViewComAdapter;
import com.sayed.packingsolution.dataholder.DataHolder;


public class CompleteFrag extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    BottomNavigationView bottomNavContainer;

    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://packing-solution-default-rtdb.asia-southeast1.firebasedatabase.app");
    private final DatabaseReference post_node = db.getReference("post");
    private final DatabaseReference settings_node = db.getReference("settings");


    private RecyclerView recView;
    private EditText searchtext;
    public ViewComAdapter viewComAdapter;







    public CompleteFrag() {

    }


    public static CompleteFrag newInstance(String param1, String param2) {
        CompleteFrag fragment = new CompleteFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_complete, container, false);

        searchtext = view.findViewById(R.id.search_txt_c);
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
                    loadComData();

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        //Bottom Menu
        bottomNavContainer = view.findViewById(R.id.bottomContainer);
        bottomNavContainer.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        startActivity(new Intent(view.getContext(),MainActivity.class));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        getActivity().finish();
                        break;

                    case R.id.menu_setting:
                        startActivity(new Intent(view.getContext(),SettingActivity.class));
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        getActivity().finish();
                        break;
                }
                return true;
            }
        });



        recView = view.findViewById(R.id.recview_comp_post);
        recView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        loadComData();



        return view;

    }


    void searchbyDesc(String query){

        FirebaseRecyclerOptions<DataHolder> options =
                new FirebaseRecyclerOptions.Builder<DataHolder>()
                        .setQuery(post_node.orderByChild("description").startAt(query).endAt(query+"~"), DataHolder.class)
                        .build();

        viewComAdapter = new ViewComAdapter(options);
        viewComAdapter.startListening();
        recView.setAdapter(viewComAdapter);


    }



    void loadComData(){
        FirebaseRecyclerOptions<DataHolder> options = new FirebaseRecyclerOptions.Builder<DataHolder>()
                .setQuery(post_node.orderByChild("status").equalTo("b_complete"), DataHolder.class)
                .build();


        viewComAdapter = new ViewComAdapter(options);
        viewComAdapter.startListening();
        recView.setAdapter(viewComAdapter);
    }


}