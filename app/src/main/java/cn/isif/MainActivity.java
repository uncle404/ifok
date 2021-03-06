package cn.isif;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import cn.isif.alibs.utils.ALibs;
import cn.isif.alibs.utils.ALog;
import cn.isif.ifok.CallBack;
import cn.isif.ifok.IfOk;
import cn.isif.ifok.OkConfig;
import cn.isif.ifok.Params;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    public String TAG = "MainActivity";
    public String url = "http://www.baidu.com";
    public Call call;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ALibs.init(this.getApplicationContext());
        textView = (TextView) findViewById(R.id.progress);
        checkDepPermission();

        OkConfig.Builder builder = new OkConfig.Builder()
                .setSSLSocketFactory(createSSLSocketFactory())
                .setHostnameVerifier(new TrustAllHostnameVerifier())
                .setTimeout(1000 * 60);

        IfOk.getInstance().init(builder.build());
        URLDecoder.decode()
    }


    private void checkDepPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    public void uploadFile(View view) {
        String currentApkPath = getApplicationContext().getPackageResourcePath();
        File apkFile = new File(currentApkPath);
        Params params = new Params.Builder().build();
        params.attach("test", apkFile);
        params.attach("test1", apkFile);
        params.attach("test2", apkFile);
        call = IfOk.getInstance().post(url, params, new CallBack() {
            @Override
            public void onStart(Request request) {

            }

            @Override
            public void onFail(Exception e, Response response) {
                Log.d(TAG, e.getMessage());
            }

            @Override
            public void onSuccess(Object object) {
                Toast.makeText(MainActivity.this, object.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void updateProgress(int progress, long networkSpeed, boolean done) {
                textView.setText("" + progress + "---" + networkSpeed);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void downloadFile(View view) {
//        final String path = getAppRootDirectory() + "weiboyi.apk";
//        ALog.d(path);
//        String file = "https://192.168.100.151/fred/bin/george/default_channel/weiboyi.apk";
//        final String fileDir = new File(path).getParentFile().getAbsolutePath();
//        ALog.d(fileDir);
//        IfOk.getInstance().download(file, fileDir, new CallBack() {
//            @Override
//            public void onStart(Request request) {
//
//            }
//
//            @Override
//            public void onFail(Exception e, Response response) {
//                ALog.d(e.getMessage());
//            }
//
//            @Override
//            public void onSuccess(Object object) {
//                try {
//                    OpenFileUtil.openFile(MainActivity.this, new File(path));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void updateProgress(int progress, long networkSpeed, boolean done) {
//                textView.setText("" + progress + "---" + networkSpeed);
//            }
//        });
        new Thread(){
            @Override
            public void run() {
                try {
                    Log.d("...","begain");
                    testUrl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    private String getAppRootDirectory() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "temp" + File.separator;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return path;
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void testUrl() throws Exception {
        String url = "http%3a%2f%2fimg.weiboyi.com%2fvol1%2f2%2f101%2f110%2fc%2fe%2fonr055po416p11r99p5o506o4op229o2%2f%e5%90%90%e6%a7%bd%e5%a4%a7%e4%bc%9a.docx";

        OkHttpClient client = new OkHttpClient();

        // Create request for remote resource.
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Execute the request and retrieve the response.
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()){
                Log.d("...","ok");
            }
        }
    }

}
