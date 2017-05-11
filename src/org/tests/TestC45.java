package org.tests;

import org.Builder;
import org.objects.SetObjects;
import org.tree.DecisionTree;

import java.io.IOException;
import java.util.Date;

public class TestC45 {
    public static void main(String[] args) throws IOException{
        Date start = new Date();
        SetObjects set = new SetObjects("test.xls");
        Builder builder = new Builder();
        DecisionTree tree = builder.build(set, "C45");
        Date end = new Date();
        long time = end.getTime() - start.getTime();
        System.out.println("Время выполнения " + time);
    }

    //Время выполнения
    // 229280с
}
