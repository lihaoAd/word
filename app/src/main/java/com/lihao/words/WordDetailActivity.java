package com.lihao.words;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lihao.words.entry.IcibaPart;
import com.lihao.words.entry.Word;
import com.lihao.words.persent.IWordDetailView;
import com.lihao.words.persent.WordDetailPresent;

import java.util.List;
import java.util.Locale;

public class WordDetailActivity extends AppCompatActivity implements IWordDetailView {

    private Word word;
    private WordDetailPresent present;
    private RelativeLayout layoutPh;
    private ImageView imHornEn;
    private ImageView imHornAm;
    private TextView  textWord;
    private TextView  textWordMeaning;
    private TextView  textHornPhEn;
    private TextView  textHornPhAm;
    private LinearLayout wordIcibaMeaning;
    private TextView textIcibaWordMeaning;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        word = getIntent().getParcelableExtra("word");

        imHornEn = findViewById(R.id.imHornEn);
        imHornAm = findViewById(R.id.imHornAm);
        layoutPh = findViewById(R.id.layoutPh);
        textWord = findViewById(R.id.textWord);
        textWordMeaning = findViewById(R.id.textWordMeaning);
        textHornPhEn = findViewById(R.id.textHornPhEn);
        textHornPhAm = findViewById(R.id.textHornPhAm);
        wordIcibaMeaning = findViewById(R.id.wordIcibaMeaning);
        textIcibaWordMeaning = findViewById(R.id.textIcibaWordMeaning);
        textWord.setText(word.word);
        textWordMeaning.setText(word.meaning);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        present = new WordDetailPresent(word.word);
        present.bindView(this);
        imHornEn.setOnClickListener(v->present.palyPhAm());
        imHornAm.setOnClickListener(v->present.palyPhEn());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        present.destroy();
    }

    @Override
    public void showphEn(String phEn) {
        layoutPh.setVisibility(View.VISIBLE);
        textHornPhEn.setText(String.format(Locale.getDefault(),"英[%s]",phEn));
    }

    @Override
    public void showphAm(String phAm) {
        layoutPh.setVisibility(View.VISIBLE);
        textHornPhAm.setText(String.format(Locale.getDefault(),"美[%s]",phAm));
    }

    @Override
    public void showMeans(List<IcibaPart> parts) {
        if(parts == null || parts.size() == 0)return;
        wordIcibaMeaning.setVisibility(View.VISIBLE);
        textIcibaWordMeaning.setVisibility(View.VISIBLE);
        StringBuilder builder = new StringBuilder();
        for (IcibaPart part : parts){
            builder.append(part.part).append(part.means).append("\n");
        }
        textIcibaWordMeaning.setText(builder.toString());
    }

    @Override
    public void playPhError(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
