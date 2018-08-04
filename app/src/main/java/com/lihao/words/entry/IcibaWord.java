package com.lihao.words.entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IcibaWord {

    /**单词*/
    @SerializedName("word_name") public String wordName;
    /**英式音标*/
    @SerializedName("ph_en") public String phEn;
    /**美式音标*/
    @SerializedName("ph_am") public String phAm;

    /**英式发音*/
    @SerializedName("ph_en_mp3") public String phEnMp3;
    /**美式发音*/
    @SerializedName("ph_am_mp3") public String phAmMp3;

    public List<IcibaPart> parts;
}
