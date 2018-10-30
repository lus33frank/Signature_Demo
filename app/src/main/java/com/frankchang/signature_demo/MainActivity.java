package com.frankchang.signature_demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE = 999;
    private Signature sign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout view = findViewById(R.id.signature);
        sign = new Signature(this);
        view.addView(sign, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
    }

    // 詢問授權回覆處理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePNG();
            }
        }
    }

    // 上一步
    public void undoOnClick(View view) {
        sign.actionUndo();
    }

    // 下一步
    public void redoOnClick(View view) {
        sign.actionRedo();
    }

    // 變換筆色
    public void colorOnClick(View view) {
        sign.actioncolor();
    }

    // 清除簽名板
    public void clearOnClick(View view) {
        sign.actionClear();
    }

    // 儲存
    public void saveOnClick(View view) {
        // 檢查是否有權限
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            savePNG();

        } else {
            // 詢問開啟權限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE);
        }
    }

    private void savePNG() {
        String path = Environment.getExternalStorageDirectory().getPath();
        path += "/Pictures/Signature.png";
        sign.setDrawingCacheEnabled(true);

        try {
            FileOutputStream fos = new FileOutputStream(path);
            sign.getDrawingCache().compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            sign.setDrawingCacheEnabled(false);
            Toast.makeText(this, "Save OK!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
