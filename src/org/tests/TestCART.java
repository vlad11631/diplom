package org.tests;

import org.Builder;
import org.objects.SetObjects;
import org.tree.DecisionTree;

import java.io.IOException;
import java.util.Date;

public class TestCART {
    public static void main(String[] args) throws IOException{
        Date start = new Date();
        SetObjects set = new SetObjects("test.xls");
        Builder builder = new Builder();
        DecisionTree tree = builder.build(set, "CART");
        Date end = new Date();
        long time = end.getTime() - start.getTime();
        System.out.println("Время выполнения " + time);
    }

    //Время выполнения
    //237029с
}
