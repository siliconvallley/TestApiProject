package com.dh.testproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dh.testproject.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main8Activity extends AppCompatActivity implements View.OnClickListener {
    private static final Pattern PLAIN_TEXT_TO_ESCAPE = Pattern.compile("[<>&]| {2,}|\r?\n");
    private static final String TAG = "Main8Activity";

    private Button btText;
    private Button btBinary;
    private Button btMultiBinary;
    private Button btRichText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        initViews();
        initListener();
    }

    private void initListener() {
        btText.setOnClickListener(this);
        btBinary.setOnClickListener(this);
        btMultiBinary.setOnClickListener(this);
        btRichText.setOnClickListener(this);
    }

    private void initViews() {
        btText = findViewById(R.id.btText);
        btBinary = findViewById(R.id.btBinary);
        btMultiBinary = findViewById(R.id.btMultiBinary);
        btRichText = findViewById(R.id.btRichText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btText:
                shareText();
                break;
            case R.id.btBinary:
                shareBinary();
                break;
            case R.id.btMultiBinary:
                shareMultiBinary();
                break;
            case R.id.btRichText:
                //shareRichText();
                String toDisplay = escapeCharacterToDisplay(testText);
                Log.e(TAG, "转换后的数据: " + toDisplay);
                break;
        }
    }


    private String testText = "\n" +
            "半角\n" +
            "A B C\n" +
            "A  B  C\n" +
            "A   B   C\n" +
            "A    B    C\n" +
            "\n" +
            "\n" +
            "\n" +
            "全角\n" +
            "A　B　C　\n" +
            "A　　B　　C\n" +
            "A　　　B　　C\n" +
            "A　　　　B　　　　C";

    public static String escapeCharacterToDisplay(String text) {
        Matcher match = PLAIN_TEXT_TO_ESCAPE.matcher(text);

        if (match.find()) {
            StringBuilder out = new StringBuilder();
            int end = 0;
            do {
                int start = match.start();
                out.append(text.substring(end, start));
                end = match.end();
                int c = text.codePointAt(start);
                if (c == ' ') {
                    // Escape successive spaces into series of "&nbsp;".
                    for (int i = 1, n = end - start; i < n; ++i) {
                        out.append("&nbsp;");
                    }
                    out.append(' ');
                } else if (c == '\r' || c == '\n') {
                    out.append("<br>");
                } else if (c == '<') {
                    out.append("&lt;");
                } else if (c == '>') {
                    out.append("&gt;");
                } else if (c == '&') {
                    out.append("&amp;");
                }
            } while (match.find());
            out.append(text.substring(end));
            text = out.toString();
        }
        return text;
    }

    /**
     * 分享富文本
     */
    private void shareRichText() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "我是富文本");
        intent.putExtra(Intent.EXTRA_TITLE, "发送富文本");

        intent.setData(Uri.parse("http://img4.imgtn.bdimg.com/it/u=2853553659,1775735885&fm=26&gp=0.jpg"));
        intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "富文本"));
    }

    /**
     * 分享多个二进制文件
     */
    private void shareMultiBinary() {
        ArrayList<Uri> imageUris = new ArrayList<>();
        imageUris.add(Uri.parse("http://img4.imgtn.bdimg.com/it/u=2853553659,1775735885&fm=26&gp=0.jpg"));
        imageUris.add(Uri.parse("http://img4.imgtn.bdimg.com/it/u=2853553659,1775735885&fm=26&gp=0.jpg"));

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "分享多个图片"));
    }

    /**
     * MIME type
     * 1. text/plain, text/rtf, text/html, text/json, receivers should register for text/*
     * 2. image/jpg, image/png, image/gif, receivers should register for image/*
     * 3. video/mp4, video/3gp, receivers should register for video/*
     * 4. application/pdf, receivers should register for supported file extensions
     * 5. You can use a MIME type of "*\/*", 但是不建议使用
     *
     */

    /**
     * 分享二进制内容
     */
    private void shareBinary() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("http://img4.imgtn.bdimg.com/it/u=2853553659,1775735885&fm=26&gp=0.jpg"));
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "分享图片"));
    }

    /**
     * 分享文本
     */
    private void shareText() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "这是我想分享出去的文本");
        intent.putExtra(Intent.EXTRA_TITLE, "分享内容的标题");
        intent.putExtra(Intent.EXTRA_EMAIL, "www.google.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "电子邮件主题");
        intent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(intent, "分享的标题");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            shareIntent.putExtra(Intent.EXTRA_CHOOSER_TARGETS, "");
        }
        shareIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, "");
        startActivity(shareIntent);
    }
}
