package com.lihao.words.entry;

import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Parcelable {

    /**记不住的单词*/
    public static final int WORD_STATUS_NOT_REMEMBER = 1;
    /**知道了解的单词*/
    public static final int WORD_STATUS_KNOW         = 2;

    /**单词的首字母 大写*/
    public String firstLetter;
    /**单词*/
    public String word;
    /**单词的含义*/
    public String meaning;
    /**单词的状态*/
    public int status;

    /**创建的时间*/
    public long creatTime;

    /**修改的时间*/
    public long modifyTime;


    public Word(String firstLetter, String word, String meaning, int status, long creatTime, long modifyTime) {
        this.firstLetter = firstLetter;
        this.word = word;
        this.meaning = meaning;
        this.status = status;
        this.creatTime = creatTime;
        this.modifyTime = modifyTime;
    }

    protected Word(Parcel in) {
        firstLetter = in.readString();
        word = in.readString();
        meaning = in.readString();
        status = in.readInt();
        creatTime = in.readLong();
        modifyTime = in.readLong();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    @Override
    public String toString() {
        return "Word{" +
                "firstLetter='" + firstLetter + '\'' +
                ", word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                ", status=" + status +
                ", creatTime=" + creatTime +
                ", modifyTime=" + modifyTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstLetter);
        dest.writeString(word);
        dest.writeString(meaning);
        dest.writeInt(status);
        dest.writeLong(creatTime);
        dest.writeLong(modifyTime);
    }
}
