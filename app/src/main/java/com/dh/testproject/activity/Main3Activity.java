package com.dh.testproject.activity;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.security.crypto.EncryptedFile;
//import androidx.security.crypto.MasterKeys;

import android.os.Bundle;
import android.os.Environment;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.View;

import com.dh.testproject.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.GeneralSecurityException;

public class Main3Activity extends AppCompatActivity {
    private static final String TAG = "Main3Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    public void btEncrypt(View view) {
        /*try {
            KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            String masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

            String fileName = "test.txt";
            File file = new File(getExternalCacheDir(), fileName);
            if (file.exists()) {
                file.delete();
            }
            EncryptedFile encryptedFile = new EncryptedFile.Builder(
                    file,
                    this,
                    masterKeyAlias,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB)
                    .build();
            Log.e(TAG, file.getAbsolutePath());

            BufferedOutputStream bos = new BufferedOutputStream(encryptedFile.openFileOutput());
            bos.write("我正在写入数据".getBytes());
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(encryptedFile.openFileOutput()));
            //writer.write("我正在写入加密文件");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void btDecrypt(View view) {
        /*try {
            KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            String masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

            String fileName = "test.txt";
            File file = new File(getExternalCacheDir(), fileName);

            EncryptedFile encryptedFile = new EncryptedFile.Builder(
                    file,
                    this,
                    masterKeyAlias,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB)
                    .build();
            Log.e(TAG, file.getAbsolutePath());

            StringBuilder builder = new StringBuilder();
            //BufferedReader reader = new BufferedReader(new InputStreamReader(encryptedFile.openFileInput()));
            BufferedInputStream bis = new BufferedInputStream(encryptedFile.openFileInput());

            byte[] buf = new byte[1024];
            int count;
            while ((count = bis.read(buf)) != -1) {
                String string = new String(buf, 0, count);
                builder.append(string);
            }

            *//*String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }*//*
            Log.e(TAG, "输出内容: " + builder.toString());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
