package com.hjx.cp.abe.pojo;

import com.hjx.cp.abe.text.CipherText;

public class Pojo {
    private static CipherText cipherText;

    public static CipherText getCipherText() {
        return cipherText;
    }

    public static void setCipherText(CipherText cipherText) {
        Pojo.cipherText = cipherText;
    }
}
