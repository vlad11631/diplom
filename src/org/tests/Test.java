package org.tests;

import org.metods.ID3;
import org.metods.Metod;
import org.metods.MetodFactory;
import org.objects.SetObjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws IOException{
        Date start = new Date();
        SetObjects set = new SetObjects("test.xls");
        Metod metod = MetodFactory.getMetod(MetodFactory.ID3);
        metod.separation(set);
        Date end = new Date();
        long time = end.getTime() - start.getTime();
        System.out.println("Время выполнения " + time);

    }
}
