package com.ht.communi.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ht.communi.activity.ChangeMessageActivity;
import com.ht.communi.activity.ChangePasswordActivity;
import com.ht.communi.activity.LoginActivity;
import com.ht.communi.activity.R;
import com.ht.communi.alrtdialog.ActionSheetDialog;
import com.ht.communi.customView.CircleImageView;
import com.ht.communi.javabean.Student;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.ht.communi.activity.ChangeMessageActivity.CHANGE_TYPE_EMAIL;
import static com.ht.communi.activity.ChangeMessageActivity.CHANGE_TYPE_MOBILE;
import static com.ht.communi.activity.ChangeMessageActivity.CHANGE_TYPE_SCHOOL;
import static com.ht.communi.activity.ChangeMessageActivity.CHANGE_TYPE_STU_NAME;
import static com.ht.communi.activity.ChangeMessageActivity.RESULT_EMAIL;
import static com.ht.communi.activity.ChangeMessageActivity.RESULT_MOBILE;
import static com.ht.communi.activity.ChangeMessageActivity.RESULT_SCHOOL;
import static com.ht.communi.activity.ChangeMessageActivity.RESULT_USER_NAME;

public class PersonalFragment extends Fragment {
    private Context context;
    private CircleImageView cImageView;

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    private static final String IMAGE_FILE_CROP_NAME = "temp_crop_head_image.jpg";

    /* 请求识别码 */
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 剪切照片结果
    private static final int STUDENT_USER_MESSAGE = 4;// 修改用户名信息

    private File tempFile;
    private BmobFile bmobFile;
    private TextView tv_username;
    private TextView tv_school;
    private TextView tv_mobile;
    private TextView tv_email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }
    /**
     * 初始化
     */
    public void init() {
        context = getContext();

        //头像icon
        cImageView = getActivity().findViewById(R.id.circleimageview_personal_photo);
        cImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActionSheetDialog(context)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("打开相册", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {

                                        gallery();
                                    }
                                })
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {

                                    @Override
                                    public void onClick(int which) {

                                        camera();
                                    }
                                }).show();
            }
        });

        //个性昵称
        tv_username = getActivity().findViewById(R.id.tv_username);

        //学校
        tv_school = getActivity().findViewById(R.id.tv_school);

        //手机号
        tv_mobile = getActivity().findViewById(R.id.tv_mobile);

        //邮箱
        tv_email = getActivity().findViewById(R.id.tv_email);

        Student student = BmobUser.getCurrentUser(Student.class);
        if (student != null) {
            tv_username.setText(student.getStuName());
            tv_school.setText(student.getSchool());
            tv_mobile.setText(student.getMobilePhoneNumber());
            tv_email.setText(student.getEmail());

            Log.i("htht", "student.getUserIcon(): "+ student.getUserIcon());
            if(student.getUserIcon() != null) {
                Log.i("htht", "student.getUserIcon().getFileUrl(): "+ student.getUserIcon().getFileUrl());
                Glide.with(context)
                        .load(student.getUserIcon().getFileUrl())
                        .error(R.mipmap.ic_launcher)
                        .into(cImageView);
            }

        }

        //每一项的点击事件。。。没用做成listview，我深表遗憾
        LinearLayout ll_username = getActivity().findViewById(R.id.ll_username);//用户名
        ll_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeMessageActivity.class);
                intent.putExtra("changeType", CHANGE_TYPE_STU_NAME);
                startActivityForResult(intent, STUDENT_USER_MESSAGE);
            }
        });

        LinearLayout ll_school = getActivity().findViewById(R.id.ll_school);
        ll_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeMessageActivity.class);
                intent.putExtra("changeType", CHANGE_TYPE_SCHOOL);
                startActivityForResult(intent, STUDENT_USER_MESSAGE);
            }
        });

        LinearLayout ll_mobile = getActivity().findViewById(R.id.ll_mobile);
        ll_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeMessageActivity.class);
                intent.putExtra("changeType", CHANGE_TYPE_MOBILE);
                startActivityForResult(intent, STUDENT_USER_MESSAGE);
            }
        });

        LinearLayout ll_email = getActivity().findViewById(R.id.ll_email);
        ll_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangeMessageActivity.class);
                intent.putExtra("changeType", CHANGE_TYPE_EMAIL);
                startActivityForResult(intent, STUDENT_USER_MESSAGE);
            }
        });

        LinearLayout ll_password = getActivity().findViewById(R.id.ll_password);
        ll_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout ll_logout = getActivity().findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                BmobUser.logOut();   //清除缓存用户对象
                getActivity().finish();     //关闭当前个人中心页
            }
        });

    }


    /*
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 从相机获取
     */
    public void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            if (Build.VERSION.SDK_INT >= 24) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(context, "com.ht.communi.fileprovider", new File(Environment
                                .getExternalStorageDirectory(), IMAGE_FILE_NAME)) );
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            }else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
            }
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    crop(uri);
                } catch (Exception e) {
                }
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        IMAGE_FILE_NAME);

                if (Build.VERSION.SDK_INT >= 24) {
                    crop(FileProvider.getUriForFile(context, "com.ht.communi.fileprovider", tempFile));
                }else {
                    crop(Uri.fromFile(tempFile));
                }

            } else {
                Toast.makeText(context, "未找到存储卡，无法存储照片！",
                        Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                cImageView.setImageBitmap(bitmap);

                //将图片设置完之后，还要上传网络
                bmobFile = new BmobFile(saveBitmapFile(bitmap));
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //bmobFile.getFileUrl()--返回的上传文件的完整地址
                            Log.i("htht", "上传文件成功:" + bmobFile.getFileUrl());
                            Student student = new Student();
                            student.setUserIcon(bmobFile);
                            student.update(BmobUser.getCurrentUser(Student.class).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Log.i("htht", "成功");
                                    } else {
                                        Log.i("htht", "失败" + e.getErrorCode());
                                    }
                                }
                            });
                            deleteIcon(IMAGE_FILE_CROP_NAME);
                        } else {
                            String errorMessage;
                            switch (e.getErrorCode()) {
                                case 9016:
                                    errorMessage = "没有网，上传服务器失败！！！";
                                    break;
                                default:
                                    errorMessage = "发生了一个不为人知的错误！！！";
                                    break;

                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            Log.i("htht", "上传文件失败:错误码   " + e.getErrorCode());
                            deleteIcon(IMAGE_FILE_CROP_NAME);
                        }
                    }
                });

                //检查是否拍了照片，如果拍了则删除。
                deleteIcon(IMAGE_FILE_NAME);
            }
        } else if (requestCode == STUDENT_USER_MESSAGE) {
            if (resultCode == RESULT_USER_NAME) {
                Log.i("htht", "username: " + BmobUser.getCurrentUser(Student.class).getStuName());
                tv_username.setText(BmobUser.getCurrentUser(Student.class).getStuName());
            } else if (resultCode == RESULT_SCHOOL) {
                Log.i("htht", "school: " + BmobUser.getCurrentUser(Student.class).getSchool());
                tv_school.setText(BmobUser.getCurrentUser(Student.class).getSchool());
            } else if (resultCode == RESULT_MOBILE) {
                Log.i("htht", "mobile: " + BmobUser.getCurrentUser(Student.class).getMobilePhoneNumber());
                tv_mobile.setText(BmobUser.getCurrentUser(Student.class).getMobilePhoneNumber());
            } else if (resultCode == RESULT_EMAIL) {
                Log.i("htht", "email: " + BmobUser.getCurrentUser(Student.class).getEmail());
                tv_email.setText(BmobUser.getCurrentUser(Student.class).getEmail());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void deleteIcon(String path) {
        //检查剪裁图片删除没有，如果没有，则删除。
        tempFile = new File(Environment.getExternalStorageDirectory(),
                path);
        Log.i("htht", "文件存在吗======= " + tempFile);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    //将bitmap转换成file
    public File saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(),
                IMAGE_FILE_CROP_NAME);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
}
