package com.lihao.words;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import com.lihao.words.helper.WordHelper;
import com.lihao.words.utils.SoftInputUtils;

public class WordSaveActivity extends AppCompatActivity{

    private EditText editWord;
    private EditText editMeaning;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_word_save);

        editWord    = findViewById(R.id.editWord);
        editMeaning = findViewById(R.id.editMeaning);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.word_save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                SoftInputUtils.hideInputForce(this);
                String word = editWord.getText().toString().trim();
                String meaning = editMeaning.getText().toString().trim();
                if(!word.matches("[a-zA-Z\\- _]+?")){
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"请填写正确单词", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return false;
                }
                if(!TextUtils.isEmpty(word) && !TextUtils.isEmpty(meaning)){
                    WordHelper.saveWordToDB(this,word,meaning);
                    editWord.setText(null);
                    editMeaning.setText(null);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"保存成功", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                break;
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        SoftInputUtils.hideInputForce(this);
    }
}
