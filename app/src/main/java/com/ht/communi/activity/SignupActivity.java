package com.ht.communi.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ht.communi.javabean.Student;
import com.ht.communi.javabean.StudentCommunity;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SignupActivity extends AppCompatActivity {

    private EditText ed_name;
    private EditText ed_school;
    private EditText ed_email;
    private EditText ed_mobile;
    private EditText ed_password;
    private EditText ed_reEnterPassword;
    private EditText ed_verify;
    private TextView tv_loginLink;
    private Button btn_signUp;
    private Button btn_verify;
    private MyCountDownTimer myCountDownTimer;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context = this;
        initView();
    }

    public void initView(){
        //new倒计时对象,总共的时间,每隔多少秒更新一次时间
        myCountDownTimer = new MyCountDownTimer(60000,1000);
        ed_name = findViewById(R.id.input_name);
        ed_school = findViewById(R.id.input_school);
        ed_email = findViewById(R.id.input_email);
        ed_mobile = findViewById(R.id.input_mobile);
        ed_password = findViewById(R.id.input_password);
        ed_reEnterPassword = findViewById(R.id.input_reEnterPassword);
        ed_verify = findViewById(R.id.input_verify);
        tv_loginLink = findViewById(R.id.link_login);
        tv_loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_signUp = findViewById(R.id.btn_signup);
        btn_verify = findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求短信
                BmobSMS.requestSMSCode(ed_mobile.getText().toString(), "PK的模板", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if(e==null){//验证码发送成功
                            myCountDownTimer.start();
                            Log.i("htht", "短信id："+integer);//用于查询本次短信发送详情
                        }else{
                            String errorMessage ;
                            switch (e.getErrorCode()){
                                case 9018:
                                    errorMessage = "手机号码为空，咋子发出去嘛！！！";
                                    break;
                                default:
                                    errorMessage = "验证码获取失败！！！";
                                    break;

                            }
                            Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                            Log.e("htht", "done: "+e.getMessage());
                            Log.e("htht", "done:  code" + e.getErrorCode());
                        }
                    }
                });
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    //验证码倒计时类
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            btn_verify.setClickable(false);
            btn_verify.setAlpha(0.8f);
            btn_verify.setText(l/1000+"s");

        }


        //计时完毕的方法
        @Override
        public void onFinish() {
            btn_verify.setAlpha(1.0f);
            //重新给Button设置文字
            btn_verify.setText("重新获取验证码");
            //设置可点击
            btn_verify.setClickable(true);
        }
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            Log.i("htht", "signup: 直接退出了");
            return;
        }

        btn_signUp.setEnabled(false);


        //验证码验证
        BmobSMS.verifySmsCode(ed_mobile.getText().toString(), ed_verify.getText().toString(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //短信验证码已验证成功
                    Log.i("htht", "验证通过");
                    final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                            R.style.Theme_AppCompat_DayNight_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("创建账号中...");
                    progressDialog.show();


                    String name = ed_name.getText().toString();
                    String school = ed_school.getText().toString();
                    String email = ed_email.getText().toString();
                    String mobile = ed_mobile.getText().toString();
                    String password = ed_password.getText().toString();

                    Student student = new Student();
                    student.setUsername(mobile);
                    student.setStuName(name);
                    student.setSchool(school);
                    student.setEmail(email);
                    student.setMobilePhoneNumber(mobile);
                    student.setPassword(password);
                    student.signUp(new SaveListener<Student>() {
                        @Override
                        public void done(Student student, BmobException e) {
                            if(e == null){
                                //注册成功，将新用户加入到StudentCommunity表中
                                StudentCommunity studentCommunity = new StudentCommunity();
                                studentCommunity.setStudent(student);
                                studentCommunity.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e == null){
                                            Log.i("htht", "新用户注册成功，并加入到studentCommunity表中: ");
                                        }else{
                                            Log.i("htht", "新用户注册成功，但加入到studentCommunity表中失败: "+e.getErrorCode());
                                        }
                                    }
                                });

                                onSignupSuccess();
                            }else{
                                String errorMessage ;
                                switch (e.getErrorCode()){
                                    case 203:
                                        errorMessage = "邮箱已经存在";
                                        break;
                                    case 202:
                                    case 209:
                                        errorMessage = "该手机号码已经存在";
                                        break;
                                    case 9016:
                                        errorMessage = "网都没有，你注册个啥！！！";
                                        break;
                                    default:
                                        errorMessage = "注册失败！";
                                        break;

                                }
                                Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_LONG).show();
                                Log.i("htht", "出错了 "+e.getErrorCode());
                                onSignupFailed();
                            }
                            progressDialog.dismiss();
                        }
                    });

                }else{
                    Log.i("htht", "验证失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage()+"线程名      "+Thread.currentThread().getName());
                    String errorMessage ;
                    switch (e.getErrorCode()){
                        case 207:
                            errorMessage = "验证码错误，请重新输入 :)";
                            break;
                        default:
                            errorMessage = "出错了！！！";
                            break;

                    }
                    Toast.makeText(getBaseContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    onSignupFailed();
                }
            }
        });
    }


    public void onSignupSuccess() {
        btn_signUp.setEnabled(true);
        Intent intent = new Intent();
        // 获取用户计算后的结果
        String mobile = ed_mobile.getText().toString();
        intent.putExtra("mobile", mobile);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onSignupFailed() {
        btn_signUp.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String name = ed_name.getText().toString();
        String address = ed_school.getText().toString();
        String email = ed_email.getText().toString();
        String mobile = ed_mobile.getText().toString();
        String password = ed_password.getText().toString();
        String reEnterPassword = ed_reEnterPassword.getText().toString();
        String verify = ed_verify.getText().toString();

        if (name.isEmpty()|| name.length() > 10) {
            ed_name.setError("请输入小于10位用户名");
            valid = false;
        } else {
            ed_name.setError(null);
        }

        if (address.isEmpty()) {
            ed_school.setError("请输入一个学校名");
            valid = false;
        } else {
            ed_school.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed_email.setError("请输入一个有效的邮箱");
            valid = false;
        } else {
            ed_email.setError(null);
        }

        String num = "[1][34578]\\d{9}";
        if (mobile.isEmpty() || !mobile.matches(num)) {
            ed_mobile.setError("请输入一个有效的手机号");
            valid = false;
        } else {
            ed_mobile.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            ed_password.setError("请输入4到10位密码");
            valid = false;
        } else {
            ed_password.setError(null);
        }

        if (reEnterPassword.isEmpty() || !(reEnterPassword.equals(password))) {
            ed_reEnterPassword.setError("两次输入密码不一致");
            valid = false;
        } else { 
            ed_reEnterPassword.setError(null);
        }

        if(verify.isEmpty() ){
            ed_verify.setError("请输入验证码");
            valid = false;
        }else{
            ed_verify.setError(null);
        }

        Log.i("htht", "返回之前：valid ="+valid+"线程名       "+Thread.currentThread().getName());
        return valid;
    }
}
