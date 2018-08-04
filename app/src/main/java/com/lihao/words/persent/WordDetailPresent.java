package com.lihao.words.persent;

import android.media.MediaPlayer;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.lihao.words.R;
import com.lihao.words.entry.IcibaPart;
import com.lihao.words.entry.IcibaWord;
import com.lihao.words.helper.IcibaWordHelper;
import com.lihao.words.http.HttpClient;
import com.lihao.words.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WordDetailPresent {


    public static final int PH_AM = 1;
    public static final int PH_EN = 2;
    private IWordDetailView view;
    private MediaPlayer mediaPlayer;
    private  IcibaWord icibaWord;

    private String word;

    public WordDetailPresent(String word) {
        this.word = word;
    }

    public void bindView(IWordDetailView view) {
        this.view = view;
        icibaWord = IcibaWordHelper.queryIcibaWord(view.getContext(), word);
        if (icibaWord == null) {
            achieveData(word);
        } else {
            view.showphAm(icibaWord.phAm);
            view.showphEn(icibaWord.phEn);
            view.showMeans(icibaWord.parts);
        }
    }



    public void palyPhAm(){
        if(TextUtils.isEmpty(icibaWord.phAmMp3)){
            view.playPhError(view.getContext().getResources().getString(R.string.play_ph_error));
            return;
        }
        File phAmFile = new File(FileUtil.getMediaCachePath(view.getContext()),icibaWord.phAmMp3.hashCode()+"");
        if(phAmFile.exists()){
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(phAmFile.getPath());
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            } catch (IOException e) {
                e.printStackTrace();
                view.playPhError(view.getContext().getResources().getString(R.string.play_ph_error));
            }
        }else {
            downloadPh(PH_AM,icibaWord.phAmMp3);
        }
    }

    public void palyPhEn(){
        if(TextUtils.isEmpty(icibaWord.phEnMp3)){
            view.playPhError(view.getContext().getResources().getString(R.string.play_ph_error));
            return;
        }
        File phEnFile = new File(FileUtil.getMediaCachePath(view.getContext()),icibaWord.phEnMp3.hashCode()+"");
        if(phEnFile.exists()){
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(phEnFile.getPath());
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            } catch (IOException e) {
                e.printStackTrace();
                view.playPhError(view.getContext().getResources().getString(R.string.play_ph_error));
            }
        }else {
            downloadPh(PH_EN,icibaWord.phEnMp3);
        }
    }

    private void downloadPh(int ph,String phUrl){
        HttpUrl parsed = HttpUrl.parse(phUrl);
        if(parsed == null){
            view.playPhError(view.getContext().getResources().getString(R.string.play_ph_error));
            return;
        }
        OkHttpClient client = HttpClient.getClient();
        Request request = new Request.Builder()
                .url(parsed)
                .tag(this)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                FileOutputStream fileOutputStream = null;
                InputStream inputStream  = null;
                try {
                    File fileDest = new File(FileUtil.getMediaCachePath(view.getContext()),phUrl.hashCode()+"_temp");
                    inputStream = response.body().byteStream();
                    fileOutputStream = new FileOutputStream(fileDest);
                    int len;
                    byte[] buf = new byte[2048];
                    while ((len = inputStream.read(buf)) != -1) {
                        fileOutputStream.write(buf, 0, len);
                    }
                    fileOutputStream.flush();
                    fileDest.renameTo(new File(FileUtil.getMediaCachePath(view.getContext()),phUrl.hashCode()+""));
                    android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                    handler.post(()->{
                       if(PH_AM == ph){
                           palyPhAm();
                       } else if(PH_EN == ph){
                           palyPhEn();
                       }
                    });

                }catch (Exception e){

                }finally {
                    if(fileOutputStream!=null)    fileOutputStream.close();
                    if(inputStream!=null)    inputStream.close();
                }
            }
        });

    }



    private void achieveData(String word) {
        OkHttpClient client = HttpClient.getClient();
        Request request = new Request.Builder()
                .get()
                .url("http://dict-co.iciba.com/api/dictionary.php?w=" + word + "&key=B24ACE30C82C52271747CEB0578F7094&type=json")
                .tag(this)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        String result = null;
                        try {
                            result = response.body().string();
                            icibaWord = new IcibaWord();
                            Gson gson = new Gson();
                            JsonObject icibaWordJson = gson.fromJson(result, new TypeToken<JsonObject>() {
                            }.getType());
                            String word_name = icibaWordJson.get("word_name").getAsString();
                            icibaWord.wordName = word_name;
                            JsonArray jsonArray = icibaWordJson.getAsJsonArray("symbols");
                            if (jsonArray.size() != 0) {
                                JsonObject symbolsJson = jsonArray.get(0).getAsJsonObject();
                                String ph_en = symbolsJson.get("ph_en").getAsString();
                                String ph_am = symbolsJson.get("ph_am").getAsString();
                                String ph_en_mp3 = symbolsJson.get("ph_en_mp3").getAsString();
                                String ph_am_mp3 = symbolsJson.get("ph_am_mp3").getAsString();

                                JsonArray partsJsonArray = symbolsJson.getAsJsonArray("parts");

                                List<IcibaPart> parts = new ArrayList<>(partsJsonArray.size());
                                for (int i = 0; i < partsJsonArray.size(); i++) {
                                    JsonObject partItem = partsJsonArray.get(i).getAsJsonObject();
                                    String part = partItem.get("part").getAsString();
                                    JsonArray meansArray = partItem.get("means").getAsJsonArray();
                                    StringBuilder meansBuilder = new StringBuilder();
                                    for (int len = meansArray.size(), j = 0; j < len; j++) {
                                        JsonPrimitive meansItem = meansArray.get(i).getAsJsonPrimitive();
                                        if (j != len - 1) {
                                            meansBuilder.append(meansItem.getAsString()).append(";");
                                        } else {
                                            meansBuilder.append(meansItem.getAsString());
                                        }
                                    }
                                    parts.add(new IcibaPart(part, meansBuilder.toString()));
                                }
                                icibaWord.parts = parts;
                                icibaWord.phEn = ph_en;
                                icibaWord.phAm = ph_am;
                                icibaWord.phEnMp3 = ph_en_mp3;
                                icibaWord.phAmMp3 = ph_am_mp3;
                            }
                            IcibaWordHelper.saveIcibaWordToDB(view.getContext(), icibaWord);

                            view.showphAm(icibaWord.phAm);
                            view.showphEn(icibaWord.phEn);
                            view.showMeans(icibaWord.parts);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public void destroy() {
        OkHttpClient client = HttpClient.getClient();
        cancelTag(client, this);

        if(mediaPlayer!= null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

    }


    private void cancelTag(OkHttpClient client, Object tag) {
        if (tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
