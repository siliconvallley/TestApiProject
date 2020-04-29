package com.dh.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {

    private Uri uri;
    private static final String TAG = "Main2Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        uri = Uri.parse("content://com.dh.MyContentProvider/user");

        /*int[] colors = {Color.RED, Color.MAGENTA, Color.GREEN, Color.YELLOW};
        Bitmap bitmap = Bitmap.createBitmap(colors,2, 2, Bitmap.Config.ARGB_8888);
        ((ImageView)findViewById(R.id.iv)).setImageBitmap(bitmap);*/
    }

    public void btAdd(View view) {
        ContentValues values = new ContentValues();
        values.put("_id", "3");
        values.put("name", "添加名字");
        getContentResolver().insert(uri, values);
        Log.e(TAG, "添加成功");
    }

    public void btDel(View view) {
        getContentResolver().delete(uri, "_id=?", new String[]{"3"});
        Log.e(TAG, "删除成功");
    }

    public void btUpdate(View view) {
        ContentValues values = new ContentValues();
        values.put("name", "修改名字");
        getContentResolver().update(uri, values, "_id=?", new String[]{"3"});
        Log.e(TAG, "修改成功");
    }

    public void btQuery(View view) {
        Cursor cursor = getContentResolver().query(uri, new String[]{"_id", "name"}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.e(TAG, "id: " + cursor.getInt(cursor.getColumnIndex("_id")) +
                        " ++++> name: " + cursor.getString(cursor.getColumnIndex("name")));
            }
            cursor.close();
        }
    }

    public void btCall(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("call", "Call传递值");
        getContentResolver().call(uri, "call", "call", bundle);
    }
}
