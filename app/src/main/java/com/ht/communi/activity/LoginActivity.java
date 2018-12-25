package com.ht.communi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ht.communi.javabean.Student;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    private EditText ed_account;
    private EditText ed_password;
    private Button btn_login;
    private TextView tv_signupLink;

    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        initView();
        //第一：默认初始化
        Bmob.initialize(this, "214523c8d5aeba03fe3da3bac366f14a");

        //自动登陆
        Student student = BmobUser.getCurrentUser(Student.class);
        if(student != null){
            Log.i("htht", "bmobUser: " + student.getUsername());
            onLoginSuccess();
        }
    }
    public void initView(){
        ed_account = findViewById(R.id.input_account);
        ed_password = findViewById(R.id.input_password);
        btn_login = findViewById(R.id.btn_login);
        tv_signupLink = findViewById(R.id.link_signup);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        tv_signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                String mobile = data.getStringExtra("mobile");
                Log.i("htht", "onActivityResult: " + mobile);
                if(mobile != null){
                    ed_account.setText(mobile);
                }
            }
        }
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        btn_login.setEnabled(false);

        //正在登陆提示框
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("登陆中...");
        progressDialog.show();

        String mobile = ed_account.getText().toString();
        String password = ed_password.getText().toString();

        Student student = new Student();
        student.setUsername(mobile);
        student.setPassword(password);
        student.login(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    onLoginSuccess();
                }else{
                    String errorMessage ;
                    switch (e.getErrorCode()) {
                        case 101:
                            errorMessage = "用户名或者密码不正确";
                            break;
                        case 9016:
                            errorMessage = "网都没有，你登陆个啥！！！";
                            break;
                        default:
                            errorMessage = "登陆失败！";
                            break;
                    }
                    Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                    onLoginFailed();
                    Log.i("htht", "登陆时出错了 "+e.getErrorCode());
                }
                progressDialog.dismiss();
            }
        });

    }

    //输入框验证
    public boolean validate() {
        boolean valid = true;

        String account = ed_account.getText().toString();
        String password = ed_password.getText().toString();

        //匹配手机号正则表达式
        String num = "[1][34578]\\d{9}";
        if (account.isEmpty() || !account.matches(num)) {
            ed_account.setError("请输入一个有效的手机号");
            valid = false;
        } else {
            ed_account.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            ed_password.setError("请输入4到10位密码");
            valid = false;
        } else {
            ed_password.setError(null);
        }
        return valid;
    }

    public void onLoginSuccess() {
        btn_login.setEnabled(true);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        btn_login.setEnabled(true);
    }
}
