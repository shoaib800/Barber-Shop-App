package com.shoaib.barbershopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoaib.barbershopapp.Common.Common;
import com.shoaib.barbershopapp.Fragments.HomeFragment;
import com.shoaib.barbershopapp.Fragments.ShoppingFragment;
import com.shoaib.barbershopapp.Model.User;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class UserHome extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BottomSheetDialog bottomSheetDialog;
    CollectionReference userRef;

    AlertDialog dialog;

    @Override
    protected void onResume() {
        super.onResume();

        //Check Rating Dialog
        checkRatingDialog();
    }

    private void checkRatingDialog() {
        Paper.init(this);
        String dataSerialized = Paper.book().read(Common.RATING_INFORMATION_KEY, "");
        if(!TextUtils.isEmpty(dataSerialized))
        {
            Map<String, String> dataReceived = new Gson()
                    .fromJson(dataSerialized,new TypeToken<Map<String,String>>(){}.getType());
            if(dataReceived != null)
            {
                Common.showRatingDialog(UserHome.this,
                        dataReceived.get(Common.RATING_STATE_KEY),
                        dataReceived.get(Common.RATING_SALON_ID),
                        dataReceived.get(Common.RATING_SALON_NAME),
                        dataReceived.get(Common.RATING_BARBER_ID));
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        ButterKnife.bind(UserHome.this);

//        Intent intent = new Intent(UserHome.this, UserHome.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
//        startActivity(intent);

        //Check intent, if Is login = true, enable full access
        //If is login  = false, let user just around shopping to view

        //Init

        userRef = FirebaseFirestore.getInstance().collection("User");
        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        if (getIntent() != null)
        {
            boolean  isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN, false);
            if(isLogin)
            {
                dialog.show();
                //Check if User Exists
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onCreate: " + user.getPhoneNumber());

                //Save UserPhone by Paper
                Paper.init(UserHome.this);
                Paper.book().write(Common.LOGGED_KEY, user.getPhoneNumber());

                DocumentReference currentUser = userRef.document(user.getPhoneNumber());
                currentUser.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    DocumentSnapshot userSnapShot = task.getResult();
                                    if(!userSnapShot.exists())
                                    {

                                    }
                                    else
                                    {
                                        Common.currentUser = userSnapShot.toObject(User.class);
                                        bottomNavigationView.setSelectedItemId(R.id.action_home);
                                    }



                                    if(dialog.isShowing())
                                        dialog.dismiss();


                                }
                            }
                        });
            }

        }



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_home)
                    fragment = new HomeFragment();
                else if(menuItem.getItemId() == R.id.action_Shopping)
                    fragment =new ShoppingFragment();

                return loadFragment(fragment);
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);

        }

    private boolean loadFragment(Fragment fragment) {

        if(fragment != null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return  true;
        }

        return false;

    }


}
