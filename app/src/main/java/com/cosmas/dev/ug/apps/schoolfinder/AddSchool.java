package com.cosmas.dev.ug.apps.schoolfinder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddSchool extends AppCompatActivity {
    private SearchableSpinner catSpinner,typeSpiner;
    private ArrayList<String> cat,type;
    private static final int MY_CAMERA_PERMISSION_CODE = 12;
    private Uri imageUri;
    ImageView school_sample_img, open_camera_or_gallery;
    private EditText school_name,school_location, school_contact;
    private FirebaseAuth auth;
    private  FirebaseUser user;
    private  String user_email;
    private TextView info;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_school);
        dialog = new ProgressDialog(AddSchool.this);
        info=findViewById(R.id.info);
        school_name=findViewById(R.id.school_name);
        school_location=findViewById(R.id.school_location);
        school_contact=findViewById(R.id.school_contact);
        catSpinner=findViewById(R.id.categorySpinner);
        typeSpiner=findViewById(R.id.typeSpinner);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        user_email=user.getEmail().toString();
        info.setText("Post will be tracked by your email: "+user_email);

        school_sample_img =findViewById(R.id.school_sample_img);
        open_camera_or_gallery =findViewById(R.id.open_camera_or_gallery);

        open_camera_or_gallery.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pickImageNow();
            }

        });
        cat=new ArrayList<>();
        type=new ArrayList<>();
        type.add("Select Type");
        type.add("Boarding");
        type.add("Day");
        type.add("Mixed");

        typeSpiner.setAdapter(new ArrayAdapter<>(AddSchool.this,
                android.R.layout.simple_spinner_dropdown_item,type
        ));

        cat.add("Select Category");
        cat.add("University/Institution");
        cat.add("Secondary School");
        cat.add("Primary School");
        cat.add("Nursery School");
        cat.add("Primary and Nursery");
        catSpinner.setAdapter(new ArrayAdapter<>(AddSchool.this,
                android.R.layout.simple_spinner_dropdown_item,cat
        ));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void pickImageNow() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(AddSchool.this);
    }

    public void back_to_home(View view) {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK ) {
                Uri resultUri = result.getUri();
                school_sample_img.setImageURI(resultUri);
                imageUri = resultUri;
            }
            else{
                Toast.makeText(this, "School Image not loaded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void add_new_school(View view) {
        String name=school_name.getText().toString().trim();
        String location=school_location.getText().toString().trim();
        String contact=school_contact.getText().toString().trim();
        final String category = (String) catSpinner.getSelectedItem();
        final String type = (String) typeSpiner.getSelectedItem();
        if (imageUri == null){
            Snackbar.make(view,"Select School Image",Snackbar.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(name) ){
            Snackbar.make(view,"School Name not entered",Snackbar.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(location) ){
            Snackbar.make(view,"School Location not entered",Snackbar.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(contact)){
            Snackbar.make(view,"School Contact not entered",Snackbar.LENGTH_LONG).show();
        }
        else if(catSpinner.getSelectedItemPosition() == 0){
            Snackbar.make(view,"Select Category",Snackbar.LENGTH_LONG).show();
        }
        else if(typeSpiner.getSelectedItemPosition() == 0){
            Snackbar.make(view,"Select School Type",Snackbar.LENGTH_LONG).show();
        }
        else{
            dialog.show();
            StorageReference reference = FirebaseStorage.getInstance().getReference().child("Images");
            StorageReference file_path=reference.child("School").child(""+imageUri.getLastPathSegment());
            file_path.putFile(imageUri).addOnSuccessListener(taskSnapshot -> file_path.getDownloadUrl().addOnSuccessListener(uri -> {
                dialog.setMessage("Image in progress.....");
                DatabaseReference database= FirebaseDatabase.getInstance().getReference().child("Schools");
                final DatabaseReference  ref = database.push();
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                Date date=new Date();
                calendar.setTime(date);
                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int minute=calendar.get(Calendar.MINUTE);
                int second=calendar.get(Calendar.SECOND);
                String time= hour + ":" + minute + ":" + second;
                HashMap map = new HashMap();
                map.put("UploadTime",time);
                map.put("uploadDate",currentDate);
                map.put("contact",contact);
                map.put("location",location);
                map.put("type",type);
                map.put("category",category);
                map.put("image", String.valueOf(uri));
                map.put("name",name);
                map.put("postedBy", user_email);
                ref.updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onSuccess(Object o) {
                        dialog.dismiss();
                        imageUri= null;
                        school_sample_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                        school_name.setText("");
                        school_contact.setText("");
                        school_location.setText("");
                        Snackbar.make(view,"School Uploaded",Snackbar.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(AddSchool.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                });
            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(AddSchool.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
            })).addOnProgressListener(snapshot -> {
                int progess = (int)((snapshot.getBytesTransferred()/snapshot.getTotalByteCount())*100);
                dialog.setMessage("Image progress "+progess+"%");
            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(AddSchool.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
            });
        }

    }
}