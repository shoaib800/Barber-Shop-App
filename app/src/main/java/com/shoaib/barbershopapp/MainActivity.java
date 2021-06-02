package com.shoaib.barbershopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.ButterKnife;
import com.shoaib.barbershopapp.Common.Common;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent1 =new Intent(MainActivity.this, SendOTPActivity.class);
                startActivity(intent1);
            }
        });

//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

//        if(firebaseUser != null) {
//            Dexter.withActivity(this)
//                    .withPermissions(new String[]{
//                            Manifest.permission.READ_CALENDAR,
//                            Manifest.permission.WRITE_CALENDAR
//                    }).withListener(new MultiplePermissionsListener() {
//                @Override
//                public void onPermissionsChecked(MultiplePermissionsReport report) {
//                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//
//                    if (firebaseUser != null) {
//
//
//
//                        //Get Token
//                        FirebaseInstanceId.getInstance()
//                                .getInstanceId()
//                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                                        if (task.isSuccessful()) {
//                                            Common.updateToken(getBaseContext(), task.getResult().getToken());
//
//                                    Log.d("Usama's Token", task.getResult().getToken());
//
//                                            Intent intent = new Intent(MainActivity.this, UserHome.class);
//                                            intent.putExtra(Common.IS_LOGIN, true);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                Intent intent = new Intent(MainActivity.this, UserHome.class);
//                                intent.putExtra(Common.IS_LOGIN, true);
//                                startActivity(intent);
//                                finish();
//
//                            }
//                        });
//
//                    } else {
//                        setContentView(R.layout.activity_main);
//                        ButterKnife.bind(MainActivity.this);
//                    }
//                }
//
//                @Override
//                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//
//                }
//            }).check();
//
//
//        }

    }
    private void printKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES
            );

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}