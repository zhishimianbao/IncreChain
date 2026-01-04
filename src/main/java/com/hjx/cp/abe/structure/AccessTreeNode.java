package com.hjx.cp.abe.structure;

import it.unisa.dia.gas.jpbc.Element;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public abstract class AccessTreeNode {
    /**
     * 秘密数
     */
    private Element secretNumber;
    /**
     * 记录其父亲节点
     */
    private AccessTreeNode parent;
    /**
     * 节点的标号
     */
    private int index;
    /**
     * 孩子
     */
    private List<AccessTreeNode> children;
    //父亲节点ID，方便构建树
    private Integer parentId;


    protected AccessTreeNode(){
        children = new ArrayList<>();
    }

    public abstract byte getAccessTreeNodeType();

    public  int getChildrenSize(){
        return this.children.size();
    };





}
