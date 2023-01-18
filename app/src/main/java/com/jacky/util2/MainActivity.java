package com.jacky.util2;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jacky.log.Logger;
import com.jacky.util.AppUtil;
import com.jacky.util.EDA;
import com.jacky.util.StringUtil;
import com.jacky.util.ToastUtil;
import com.jacky.widget.LoopPagerAdapter;
import com.jacky.widget.LoopViewPager;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
//                    ToastUtil.showLongMsg(Thread.currentThread().getName());
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    ToastUtil.showMsg("testtet");
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.w(EDA.SHA256.sha256_mac("123456", "12", false));
        Logger.w(EDA.SHA256.sha256_mac("123456", "12", true));

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, new MainFragment());
        transaction.commit();

        TextView md5View = findViewById(R.id.md5);
        md5View.setText(EDA.MD5.digest("123445"));

        String s = EDA.AES.encrypt("123123213叫背个阁下", "1234566766文", true);
        TextView aesView = findViewById(R.id.aes);
        aesView.setText(s);

        TextView aesdView = findViewById(R.id.aes_d);
        aesdView.setText(EDA.AES.decrypt(s, "1234566766文", true));

        LoopPagerAdapter adapter =new LoopPagerAdapter<String>() {
            @Override
            public View getItemView(ViewGroup container, int position) {
                TextView view = new TextView(container.getContext());
                view.setText(String.valueOf(position));
                return view;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
        LoopViewPager pager = findViewById(R.id.loop);
        pager.setAdapter(adapter);
        pager.notifyDataSetChanged();

        Logger.d(StringUtil.toString(new String[]{"123", "3333", "yuiee"}));
        Logger.d(StringUtil.toString(new String[]{"123", "3333", "yuiee"}, "/*/*/"));
        Logger.d(StringUtil.formatDuration(156602));
        Logger.d(StringUtil.formatDurationWithoutHour(156602));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Logger.e(AppUtil.isPermissionOK(permissions, grantResults));
    }
}
