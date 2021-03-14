package shoaib.dev.barbershopapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import shoaib.dev.barbershopapp.Common.Common;
import shoaib.dev.barbershopapp.Model.User;

public class SetupProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri selectedImage;

    ImageView imageView;
    EditText textName;
    EditText textAddress;
    Button saveBtn;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating Profile...");
        dialog.setCancelable(false);

        imageView = findViewById(R.id.profileImage);
        textName = findViewById(R.id.inputName);
        textAddress = findViewById(R.id.inputAdress);
        saveBtn = findViewById(R.id.buttonSaveProfile);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = textName.getText().toString();

                if (name.isEmpty()){
                    textName.setError("Please type a name");
                    return;
                }

                String address = textAddress.getText().toString();

                if (address.isEmpty()){
                    textAddress.setError("Please type an address");
                    return;
                }

                dialog.show();
                if (selectedImage != null){
                    StorageReference reference =   storage.getReference().child("Users").child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        String uid = auth.getUid();
                                        String phoneNumber = auth.getCurrentUser().getPhoneNumber();
                                        String name = textName.getText().toString();
                                        String  address = textAddress.getText().toString();

                                        User user = new User(uid, imageUrl, name, address, phoneNumber);

                                        database.getReference()
                                                .child("Users")
                                                .child(uid)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialog.dismiss();
                                                        Intent intent =  new Intent(SetupProfileActivity.this, UserHome.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }

                        }
                    });
                } else {

                    String uid = auth.getUid();
                    String phoneNumber = auth.getCurrentUser().getPhoneNumber();

                    User user = new User(uid, "No Image", name, address, phoneNumber);

                    database.getReference()
                            .child("Users")
                            .child(uid)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    Intent intent =  new Intent(SetupProfileActivity.this, UserHome.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null){
            if(data.getData() != null){
                imageView.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }
    }
}