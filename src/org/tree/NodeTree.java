package org.tree;

import java.util.ArrayList;

public class NodeTree{
    Condition condition;
    String clazz;
    ArrayList<NodeTree> children = new ArrayList<NodeTree>();

    public NodeTree(Condition condition){
        this.condition = condition;
    }

    public NodeTree addChild(Condition condition){
        NodeTree node =  new NodeTree(condition);
        children.add(node);
        return node;
    }

    public ArrayList<NodeTree> getChildren() {
        return children;
    }

    public Condition getCondition() {
        return condition;
    }
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getClazz() {
        return clazz;
    }
    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String toString(){
        StringBuffer result = new StringBuffer();
        result.append("Node{");
        result.append(" condition: "+ condition);
        result.append(" clazz: "+ clazz);
        return  result.toString();
    }


}
