package com.lihao.words.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lihao.words.R;
import com.lihao.words.WordDetailActivity;
import com.lihao.words.entry.Word;
import com.lihao.words.helper.WordHelper;

import java.util.HashSet;
import java.util.List;

public class WordAdapter  extends RecyclerView.Adapter<WordAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private List<Word> mData;
    private Context mContext;

    private HashSet<String> excludeWord;

    private boolean wordMeaningVisible;

    public void setWordMeaningVisible(boolean wordMeaningVisible) {
        this.wordMeaningVisible = wordMeaningVisible;
        if(wordMeaningVisible)excludeWord.clear();
        notifyDataSetChanged();
    }

    public WordAdapter(Context context, List<Word> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        this.mContext = context;
        excludeWord = new HashSet<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_word, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.textWord =  view.findViewById(R.id.textWord);
        viewHolder.textMeaning =  view.findViewById(R.id.textMeaning);
        viewHolder.imgWordVisiable =  view.findViewById(R.id.imgWordVisiable);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Word word = mData.get(position);
        holder.textWord.setText(word.word);
        holder.textMeaning.setText(word.meaning);
        holder.textMeaning.setText(word.meaning);
        holder.imgWordVisiable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!excludeWord.contains(word.word)){
                    excludeWord.add(word.word);
                    notifyItemChanged(position);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WordDetailActivity.class);
                intent.putExtra("word",word);
                mContext.startActivity(intent);
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupWindow(v,word);
                return true;
            }
        });

        if(wordMeaningVisible){
            holder.textMeaning.setVisibility(View.VISIBLE);
            holder.imgWordVisiable.setVisibility(View.GONE);
        }else {
            if(excludeWord.contains(word.word)){
                holder.textMeaning.setVisibility(View.VISIBLE);
                holder.imgWordVisiable.setVisibility(View.GONE);
            }else {
                holder.textMeaning.setVisibility(View.GONE);
                holder.imgWordVisiable.setVisibility(View.VISIBLE);
            }
        }
    }


    private void showPopupWindow(View anchor, final Word word){
        View view = LayoutInflater.from(mContext).inflate(R.layout.operate_word, null);
        final PopupWindow operateWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 只有绘制完后才能获取到正确的popupWindow的宽高
        int windowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,34,mContext.getResources().getDisplayMetrics());
        int windowWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,mContext.getResources().getDisplayMetrics());
        operateWindow.setHeight(windowHeight);
        operateWindow.setWidth(windowWidth);
        operateWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        operateWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.border_shadow));
        operateWindow.setTouchable(true);
        operateWindow.setOutsideTouchable(true);
        TextView tvDelete = view.findViewById(R.id.textDelete);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordHelper.deleteWord(mContext,word.word);
                operateWindow.dismiss();
            }
        });
        operateWindow.showAsDropDown(anchor,anchor.getWidth()/2-windowWidth/2,  -anchor.getHeight()/2, Gravity.CENTER);
    }

    @Override
    public int getItemCount() {
        if(mData == null)return 0;
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textWord;
        TextView textMeaning;
        ImageView imgWordVisiable;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }



    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mData.get(i).firstLetter;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    public void updateList(List<Word> list){
        this.mData = list;
        notifyDataSetChanged();
    }


    public Word getItemData(int position){
        return mData.get(position);
    }
}
