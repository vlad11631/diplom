package org;

import org.metods.ID3;
import org.metods.Metod;
import org.metods.MetodFactory;
import org.objects.SetObjects;
import org.tree.Condition;
import org.tree.DecisionTree;
import org.tree.NodeTree;

import java.io.IOException;
import java.util.HashMap;


public class Builder {

    public DecisionTree build(SetObjects set, String metodName) throws IOException{
        System.out.println("Начало построения дерева");
        DecisionTree tree = new DecisionTree();
        Metod metod = MetodFactory.getMetod(metodName);
        buildChild(tree.getHead(), set, metod);
        System.out.println("Конец построения дерева");
        return tree;
    }

    private void buildChild(NodeTree node, SetObjects set, Metod metod) throws IOException{
        SetObjects[] childSets = metod.separation(set);
        for(SetObjects childSet: childSets){
            Condition condition = childSet.getLastCondition();
            NodeTree childNode =  node.addChild(condition);

            if (childSet.getCountObjects() == 0){
                childNode.setClazz("null");
                System.out.println("Построен узел " + childNode);
            }else {
                String name = getOneClazz(childSet);
                if (name != null) {
                    childNode.setClazz(name);
                    System.out.println("Построен узел " + childNode);
                }else{
                    System.out.println("Построен узел " + childNode);
                    buildChild(childNode, childSet, metod);
                }
            }

        }
    }

    private String getOneClazz(SetObjects set){
        String name = null;
        HashMap<String, Integer> clazzes = set.getClazzes();
        for(String key: clazzes.keySet()){
            if (clazzes.get(key) != 0){
                if (name == null){
                    name = key;
                }else{
                    return  null;
                }
            }
        }
        return  name;
    }
}
