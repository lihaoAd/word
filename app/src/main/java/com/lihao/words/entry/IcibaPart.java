package com.lihao.words.entry;

import com.google.gson.annotations.SerializedName;

public class IcibaPart {
    @SerializedName("part") public String part;
    @SerializedName("means") public String means;

    public IcibaPart(String part, String means) {
        this.part = part;
        this.means = means;
    }
}
