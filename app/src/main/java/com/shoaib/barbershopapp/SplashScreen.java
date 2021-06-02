package com.shoaib.barbershopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import com.shoaib.barbershopapp.Common.Common;

import java.util.List;

import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    Animation topAnim;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);


        //Hooks
        image = findViewById(R.id.imageView10);

        image.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref =
                        getSharedPreferences("Userlogin",
                                Context.MODE_PRIVATE);
                boolean musicState = sharedPref.getBoolean("islogin", false);
                Log.d("====>",""+musicState);
                if(musicState==true){
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    if(firebaseUser != null) {
                        Dexter.withActivity(SplashScreen.this)
                                .withPermissions(new String[]{
                                        Manifest.permission.READ_CALENDAR,
                                        Manifest.permission.WRITE_CALENDAR
                                }).withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                if (firebaseUser != null) {



                                    //Get Token
                                    FirebaseInstanceId.getInstance()
                                            .getInstanceId()
                                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Common.updateToken(getBaseContext(), task.getResult().getToken());

                                                        Log.d("Usama's Token", task.getResult().getToken());

                                                        Intent intent = new Intent(SplashScreen.this, UserHome.class);
                                                        intent.putExtra(Common.IS_LOGIN, true);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SplashScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(SplashScreen.this, UserHome.class);
                                            intent.putExtra(Common.IS_LOGIN, true);
                                            startActivity(intent);
                                            finish();

                                        }
                                    });

                                } else {
                                    setContentView(R.layout.activity_splash_screen);
                                    ButterKnife.bind(SplashScreen.this);
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                            }
                        }).check();


                    }
                    Intent intent = new Intent(SplashScreen.this, UserHome.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, SPLASH_SCREEN);
    }
}