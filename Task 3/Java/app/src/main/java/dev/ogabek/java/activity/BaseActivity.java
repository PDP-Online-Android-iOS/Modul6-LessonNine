package dev.ogabek.java.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import dev.ogabek.java.R;

public class BaseActivity extends AppCompatActivity {

    Context context;
    AppCompatDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }

    public void showLoading(Activity activity) {
        if (activity == null) return;

        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new AppCompatDialog(activity, R.style.CustomDialog);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.custom_progress_dialog);
            ImageView iv_progress = progressDialog.findViewById(R.id.iv_progress);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_progress.getDrawable();
            animationDrawable.start();
            if (!activity.isFinishing()) progressDialog.show();
        }
    }

    protected void dismissLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void callMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void callSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void callSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}
