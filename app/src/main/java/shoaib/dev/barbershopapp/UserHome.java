package shoaib.dev.barbershopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import Fragments.HomeFragment;
import Fragments.ShoppingFragment;
import Model.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

import static Common.Common.IS_LOGIN;

public class UserHome extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BottomSheetDialog bottomSheetDialog;


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
