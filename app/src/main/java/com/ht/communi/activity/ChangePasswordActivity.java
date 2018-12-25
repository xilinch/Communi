package com.ht.communi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText ed_old_password;
    private EditText ed_new_password;
    private EditText ed_new_repassword;
    private Button btn_changePassword;
    private ImageView iv_password_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();

        //左上角返回
        iv_password_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //修改密码
        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = ed_old_password.getText().toString();
                String newPassword = ed_new_password.getText().toString();
                String newRePassword = ed_new_repassword.getText().toString();
                if(oldPassword.isEmpty()){
                    ed_old_password.setError("原密码不能为空！！");
                    return;
                }
                if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 10) {
                    ed_new_password.setError("请输入4到10位密码");
                    return;
                }
                if(!newPassword.equals(newRePassword)){
                    ed_new_repassword.setError("两次输入密码不相等！！");
                    return;
                }

                BmobUser.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.i("htht", "done: 密码修改成功，可以用新密码进行登录啦");
                            Toast.makeText(getApplicationContext(),"密码修改成功，可以用新密码进行登录啦！！",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            String errMessage;
                            switch (e.getErrorCode()){
                                case 210:
                                    errMessage = "旧密码不正确！！";
                                    break;
                                default:
                                    errMessage = "密码更改失败！！";
                                    break;
                            }
                            Toast.makeText(getApplicationContext(),errMessage,Toast.LENGTH_SHORT).show();
                            Log.i("htht", "done: 密码修改失败！！" + e.getErrorCode());
                        }
                    }

                });
            }
        });

    }
    private void initView(){
        iv_password_back = findViewById(R.id.iv_password_back);
        btn_changePassword = findViewById(R.id.btn_changePassword);
        ed_old_password = findViewById(R.id.ed_old_password);
        ed_new_password = findViewById(R.id.ed_new_password);
        ed_new_repassword = findViewById(R.id.ed_new_repassword);
    }
}
