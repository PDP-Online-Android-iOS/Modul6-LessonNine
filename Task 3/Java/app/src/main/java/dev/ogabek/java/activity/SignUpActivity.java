package dev.ogabek.java.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dev.ogabek.java.R;
import dev.ogabek.java.manager.AuthHandler;
import dev.ogabek.java.manager.AuthManager;

public class SignUpActivity extends BaseActivity {

    private static final String TAG = SignUpActivity.class.toString();
    EditText et_fullname;
    EditText et_password;
    EditText et_email;
    EditText et_cpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();

    }

    void initViews() {
        et_fullname = findViewById(R.id.et_fullname);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_cpassword = findViewById(R.id.et_cpassword);

        Button b_signup = findViewById(R.id.b_signup);
        b_signup.setOnClickListener(view -> firebaseSignUp(et_email.getText().toString(), et_password.getText().toString()));
        TextView tv_signin = findViewById(R.id.tv_signin);
        tv_signin.setOnClickListener(view -> finish());
    }

    private void firebaseSignUp(String email, String password) {
        showLoading(this);
        AuthManager.signUp(email, password, new AuthHandler() {
            @Override
            public void onSuccess() {
                dismissLoading();
                Toast.makeText(getApplicationContext(), "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                callMainActivity();
            }

            @Override
            public void onError(Exception exception) {
                dismissLoading();
                exception.printStackTrace();
                Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}