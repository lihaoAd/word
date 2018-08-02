package com.lihao.words;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lihao.words.adapter.TitleItemDecoration;
import com.lihao.words.adapter.WordAdapter;
import com.lihao.words.db.WordSQL;
import com.lihao.words.entry.Word;
import com.lihao.words.provider.WordContentProvider;
import com.xp.wavesidebar.WaveSideBar;

import java.util.ArrayList;
import java.util.List;

public class WordsFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{

    private ContentObserver contentObserver;
    private static final int LOADER_ID = 1;

    private RecyclerView recyclerView;
    private WaveSideBar waveSideBar;
    private LinearLayoutManager manager;
    private WordAdapter mAdapter;
    private String filter;
    private TitleItemDecoration mDecoration;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        waveSideBar = view.findViewById(R.id.sideBar);

        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        mAdapter = new WordAdapter(getContext(), null);
        recyclerView.setAdapter(mAdapter);
        mDecoration = new TitleItemDecoration(getContext(), mAdapter);
        recyclerView.addItemDecoration(mDecoration);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        waveSideBar.setOnTouchLetterChangeListener(new WaveSideBar.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                getActivity().getSupportLoaderManager().restartLoader(LOADER_ID,null,WordsFragment.this);
            }
        };
        LoaderManager manager = getActivity().getSupportLoaderManager();
        manager.initLoader(LOADER_ID,null,this);
        getActivity().getContentResolver().registerContentObserver(WordContentProvider.CONNTENT_URL,true,contentObserver);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getContentResolver().unregisterContentObserver(contentObserver);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if(id == LOADER_ID){
            String  selection ;
            if(!TextUtils.isEmpty(filter)){
                selection = WordSQL.COLUMN_WORD +" like '%"+filter+"%'";
                return new CursorLoader(getContext(),WordContentProvider.CONNTENT_URL,null,selection,null,WordSQL.WORD_SORTORDER);
            }else {
                return new CursorLoader(getContext(),WordContentProvider.CONNTENT_URL,null,null,null,WordSQL.WORD_SORTORDER);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null)return;
        if(loader.getId() == LOADER_ID){
            List<Word> words = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()){
                words.add(new Word(cursor.getString(cursor.getColumnIndex(WordSQL.COLUMN_FIRSTLETTER)),
                        cursor.getString(cursor.getColumnIndex(WordSQL.COLUMN_WORD)),
                        cursor.getString(cursor.getColumnIndex(WordSQL.COLUMN_MEANING)),
                        cursor.getInt(cursor.getColumnIndex(WordSQL.COLUMN_STATUS)),
                        cursor.getLong(cursor.getColumnIndex(WordSQL.COLUMN_CREATTIME)),
                        cursor.getLong(cursor.getColumnIndex(WordSQL.COLUMN_MODIFYTIME))));
            }
            mAdapter.updateList(words);
        }
        loader.reset();
        cursor.close();
    }


    public void search(String filter){
        this.filter = filter;
        contentObserver.onChange(true);
    }



    public void showWordMeaning(){
        mAdapter.setWordMeaningVisible(true);
    }


    public void hideWordMeanig(){
        mAdapter.setWordMeaningVisible(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.updateList(null);
    }
}
