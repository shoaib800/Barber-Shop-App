package shoaib.dev.barbershopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Common.Common;
import Model.User;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.btn_login)
//    Button btn_login;
//
//    @BindView(R.id.txt_skip)
//    TextView txt_skip;
//
//
//    @OnClick(R.id.btn_login)
//    void loginUser()
//    {
//        Intent intent1 =new Intent(MainActivity.this, SendOTPActivity.class);
//        startActivity(intent1);
//    }
//
//    @OnClick(R.id.txt_skip)
//    void SkipLoginJustGoHome()
//    {
//        Intent intent =new Intent(MainActivity.this, UserHome.class);
//        intent.putExtra(Common.IS_LOGIN, false);
//        startActivity(intent);
//    }



    private Button BtnLogin;
    private TextView txtSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnLogin = findViewById(R.id.btn_login);
        txtSkip= findViewById(R.id.txt_skip);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null) {
            Intent intent = new Intent(MainActivity.this, SetupProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            startActivity(intent);

        }

        txtSkip.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent =new Intent(MainActivity.this, UserHome.class);
                intent.putExtra(Common.IS_LOGIN, false);
                startActivity(intent);
            }
        });

        BtnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent1 =new Intent(MainActivity.this, SendOTPActivity.class);
                startActivity(intent1);
            }
        });
    }
}