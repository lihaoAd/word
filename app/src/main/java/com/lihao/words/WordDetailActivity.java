package com.lihao.words;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.lihao.words.entry.Word;

public class WordDetailActivity extends AppCompatActivity {

    private Word word;

    private TextView  textWord;
    private TextView  textWordMeaning;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        word = getIntent().getParcelableExtra("word");
        textWord = findViewById(R.id.textWord);
        textWordMeaning = findViewById(R.id.textWordMeaning);
        textWord.setText(word.word);
        textWordMeaning.setText(word.meaning);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
