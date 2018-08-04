package com.lihao.words.persent;

import android.content.Context;

import com.lihao.words.entry.IcibaPart;

import java.util.List;

public interface IWordDetailView {

    void showphEn(String phEn);
    void showphAm(String phAm);
    void showMeans(List<IcibaPart> parts);
    void playPhError(String message);
    Context getContext();

}
