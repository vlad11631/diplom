package org.metods;

import org.objects.SetObjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class C45 implements Metod{

    public SetObjects[] separation(SetObjects set) throws IOException{
        SetObjects[] goodGlobalSets = null;
        Double maxGlobalI = null;
        String goodAttributName = null;

        Set<String> attributNames = set.getAttributNames();
        for(String attributName:attributNames) {

            SetObjects[] goodLocalSets = null;
            Double maxLocalI = null;
            double I = 0;
            ArrayList<Double> thresholdValues = set.getThresholdValues(attributName);
            for(Double thresholdValue:thresholdValues){
                SetObjects[] childSets = set.separation(attributName, thresholdValue);
                I = modifI(set, childSets);
                if (maxLocalI == null || I > maxLocalI){
                    maxLocalI = I;
                    goodLocalSets = childSets;
                }
            }
            System.out.println("Разбиение по атрибуту " + attributName + " построено. I=" + maxLocalI);

            if (maxGlobalI == null || maxLocalI > maxGlobalI){
                maxGlobalI = maxLocalI;
                goodGlobalSets = goodLocalSets;
                goodAttributName = attributName;
            }
        }
        System.out.println("Выбран атрибут " + goodAttributName + " с I=" + maxGlobalI);
        System.out.println("Исходное множество " + set.getClazzes());
        for (int i = 0; i<goodGlobalSets.length; i++){
            System.out.println(i + " потомок " + goodGlobalSets[i].getClazzes());
        }
        return goodGlobalSets;
    }

    private double I(SetObjects set, SetObjects[] childSets){
        return H(set) - Hu(set, childSets);
    }

    private double H(SetObjects set){
        double H = 0;
        int countObject = set.getCountObjects();
        HashMap<String, Integer> clazzes = set.getClazzes();

        for(String key: clazzes.keySet()){
            double pi = (double)clazzes.get(key)/countObject;
            H -= (pi == 0)? 0 : pi * Math.log(pi) ;
        }
        return H;
    }

    private double Hu(SetObjects set, SetObjects[] childSets){
        double Hu = 0;

        for(int i = 0; i<childSets.length; i++){
            double Ti = childSets[i].getCountObjects();
            double T = set.getCountObjects();
            double Hi = H(childSets[i]);

            double Hui = Ti / T * Hi;
            Hu += Hui;
        }
        return Hu;
    }

    private double modifHu(SetObjects set, SetObjects[] childSets){
        double Hu = 0;

        for(int i = 0; i<childSets.length; i++){
            double Ti = childSets[i].getCountObjects();
            double T = set.getCountObjects();
            double pi = Ti/T;
            Hu -= (pi == 0)? 0 : pi * Math.log(pi) ;
        }
        return Hu;
    }

    private double modifI(SetObjects set, SetObjects[] childSets){
        return I(set, childSets)/modifHu(set, childSets);
    }
}
