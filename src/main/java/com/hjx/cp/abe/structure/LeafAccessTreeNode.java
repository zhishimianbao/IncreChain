package com.hjx.cp.abe.structure;


import com.hjx.cp.abe.attribute.Attribute;
import com.hjx.cp.abe.parameter.PublicKey;
import it.unisa.dia.gas.jpbc.Field;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@ToString
@Data
public class LeafAccessTreeNode extends AccessTreeNode {

    /**
     * 属性
     */
    private Attribute attribute;

    public LeafAccessTreeNode(String message, Field GO){
        Attribute attribute = new Attribute(message,GO);
        this.attribute = attribute;
    }

    public LeafAccessTreeNode (String attribute, PublicKey publicKey, AccessTreeNode parent, int index){
        this(attribute, publicKey.getPairingParameter().getG0());
        super.setIndex(index);
        super.setParent(parent);
    }

    public LeafAccessTreeNode (String attribute, PublicKey publicKey , int index){
        this(attribute, publicKey.getPairingParameter().getG0());
        super.setIndex(index);
    }


    public LeafAccessTreeNode(Attribute attribute) {
        this.attribute = attribute;
    }

    /**
     * 判断节点的属性是否属于用户属性集合
     * @return
     */
    public boolean attr(List<Attribute> attributes){
        return attributes.contains(this.attribute);
    }

    @Override
   public byte getAccessTreeNodeType() {
        return AccessTreeNodeType.LEAF_NODE;
    }

    @Override
    public int getChildrenSize() {
        return 0;
    }

    @Override
    public String toString(){
        return ("type : Leaf Node, attribute : " + attribute + ",   children size : " + getChildrenSize() + ",  index : " + getIndex() + "  secretNumber :  " + getSecretNumber() +  "  parentId  " + this.getParentId());
    }


}
