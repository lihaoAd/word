package com.lihao.words;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.lihao.words.db.WordDBHelper;
import com.lihao.words.db.WordSQL;
import com.lihao.words.utils.FileUtil;
import com.lihao.words.utils.SoftInputUtils;
import com.lihao.words.widgets.EditTextWithDel;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private WordsFragment fragment;
    private EditTextWithDel search;
    private ImageView btnToggleWord;
    private boolean hideWordMeaning = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new WordsFragment();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.maincontent,fragment);
        fragmentTransaction.commitAllowingStateLoss();

        search = findViewById(R.id.editSearch);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String searchText  = v.getText().toString().trim();
                    fragment.search(searchText);
                    SoftInputUtils.hideInputForce(MainActivity.this);
                    return true;
                }
                return false;
            }
        });
        search.setTextDeleteListener(new EditTextWithDel.TextDeleteListener() {
            @Override
            public void deleteText() {
                fragment.search(null);
                SoftInputUtils.hideInputForce(MainActivity.this);
                search.clearFocus();
            }
        });

        btnToggleWord = findViewById(R.id.btnToggleWord);
        btnToggleWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hideWordMeaning){
                    btnToggleWord.setImageResource(R.drawable.ic_eye);
                    hideWordMeaning = false;
                    fragment.showWordMeaning();
                }else {
                    btnToggleWord.setImageResource(R.drawable.ic_eye_strike);
                    hideWordMeaning = true;
                    fragment.hideWordMeanig();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_add) {
            Intent intent = new Intent(this,WordSaveActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_word) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"暂未开发", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (id == R.id.nav_backup) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                backUpDB();
            }else {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                }else {
                    backUpDB();
                }
            }
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"备份失败,缺少相应权限", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else {
                backUpDB();
            }
        }
    }

    private void backUpDB(){
        File dbFile = getDatabasePath(WordDBHelper.DATABASE_NAME);
        File dest = new File(Environment.getExternalStorageDirectory(),WordDBHelper.DATABASE_NAME);
        try {
            FileUtil.copy(dbFile,dest);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"备份成功，"+dest.getPath(), Snackbar.LENGTH_LONG);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"备份失败", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }




}
