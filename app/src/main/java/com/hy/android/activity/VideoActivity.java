package com.hy.android.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.hy.android.R;
import com.hy.android.base.BaseActivity;
import com.hy.android.bean.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class VideoActivity extends BaseActivity {

    @Override
    public int getContentLayout() {
        return R.layout.activity_video;
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Serializable");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initData() {
        writeObj();
        readObj();
    }

    //εΊεε
    private void writeObj() {
        User user = new User("jack", 20, "man");
        try {
            String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(sdCardDir + "/user.text");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(user);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //εεΊεε
    private void readObj() {
        try {
            String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            FileInputStream fis = new FileInputStream(sdCardDir + "/user.text");
            ObjectInputStream ois = new ObjectInputStream(fis);
            User bean = (User) ois.readObject();
            // Log.e("---", bean.toString());
            //ζε°εΌδΈΊ  E/---: User{name='jack', age=20, gender='man'}

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRetry() {

    }
}
