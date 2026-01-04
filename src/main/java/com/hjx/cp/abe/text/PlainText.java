package com.hjx.cp.abe.text;

import com.hjx.cp.abe.parameter.PublicKey;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import lombok.Data;

import java.nio.charset.StandardCharsets;

@Data
public class PlainText {
    private Element messageValue;
    private String messageStr;

    public PlainText(String messageStr, PublicKey publicKey) {
        this(messageStr, publicKey.getPairingParameter().getG1());
    }

    private PlainText(String messageStr, Field G1) {
        this.messageStr = messageStr;
        this.messageValue = G1.newElementFromBytes(messageStr.getBytes(StandardCharsets.UTF_8)).getImmutable();
    }

}
