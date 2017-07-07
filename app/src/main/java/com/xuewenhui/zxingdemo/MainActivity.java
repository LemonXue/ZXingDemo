package com.xuewenhui.zxingdemo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

/**
 * 类描述：使用ZXing生成二维码（支持添加Logo图案）
 *        二维码生成工具类
 *        添加权限：
 *        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE">
          <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"></uses-permission></uses-permission>
 * 创建人：XueWenhui
 * 创建时间：17.7.7
 */
public class MainActivity extends AppCompatActivity {
    /**
     * ZXing是谷歌的一个开源库，可以用来生成二维码、扫描二维码
     * ZXing相关各种文件官方下载地址：https://github.com/zxing/zxing/releases
       或者在这里下载（只有本项目所用的jar包，版本号：3.2.0）：链接: http://pan.baidu.com/s/1hq3s5EW 密码: mvg7
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //去状态栏 加沉浸式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //内容
        final EditText contentET = (EditText) findViewById(R.id.create_qr_content);
        //显示二维码图片
        final ImageView imageView = (ImageView) findViewById(R.id.create_qr_iv);
        //是否添加Logo
        final CheckBox addLogoCB = (CheckBox) findViewById(R.id.create_qr_addLogo);
        Button createQrBtn = (Button) findViewById(R.id.create_qr_btn);
        createQrBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String filePath = getFileRoot(MainActivity.this) + File.separator
                        + "qr_" + System.currentTimeMillis() + ".jpg";

                //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean success = QRCodeUtil.createQRImage(contentET.getText().toString().trim(), 800, 800,
                                addLogoCB.isChecked() ? BitmapFactory.decodeResource(getResources(), R.drawable.hehe) : null,
                                filePath);

                        if (success) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                                }
                            });
                        }
                    }
                }).start();

            }
        });
    }

    //文件存储根目录
    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }


}
