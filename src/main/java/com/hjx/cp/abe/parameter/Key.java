package com.hjx.cp.abe.parameter;

import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Key {
    private PairingParameter pairingParameter;

    protected Key(PairingParameter pairingParameter) {
        this.pairingParameter = pairingParameter;
    }

    /**
     * 把元素映射到G0上
     * @param element
     * @return
     */
    public Element hash(Element element) {
        return pairingParameter.getG0().newElementFromBytes(element.toBytes()).getImmutable();
    }
}
