package com.cosmas.dev.ug.apps.schoolfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cosmas.dev.ug.apps.schoolfinder.Utils.SchoolAdapter;
import com.cosmas.dev.ug.apps.schoolfinder.Utils.School_List;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewSchool extends AppCompatActivity {
    java.util.List<School_List> List;
    private SchoolAdapter adapter;
    private RecyclerView recyclerView;
    private EditText search_input;
    private DatabaseReference reference;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_school);
         key=getIntent().getExtras().getString("key");
        reference = FirebaseDatabase.getInstance().getReference().child("Schools");
        List = new ArrayList<>();
        adapter = new SchoolAdapter(ViewSchool.this, List);
        recyclerView =findViewById(R.id.school_recycle);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ViewSchool.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


    }
    protected void onResume() {
        super.onResume();
        loadSchools(key);
    }
    public  void loadSchools(String search_key){
        reference.keepSynced(true);
        Query query = reference.orderByChild("category").equalTo(search_key);
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List.clear();
                int x=0;
                for(DataSnapshot ds: snapshot.getChildren()){
                    x++;
                    List.add(new School_List(
                            ds.getKey(),
                            ds.child("name").getValue().toString(),
                            ds.child("location").getValue().toString(),
                            ds.child("contact").getValue().toString(),
                            ds.child("UploadTime").getValue().toString(),
                            ds.child("uploadDate").getValue().toString(),
                            ds.child("category").getValue().toString(),
                            ds.child("type").getValue().toString(),
                            ds.child("postedBy").getValue().toString(),
                            ds.child("image").getValue().toString()
                    ));

                }
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewSchool.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void goBack(View view) {
        onBackPressed();
    }
}