package org.metods;

import org.objects.SetObjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CART implements Metod{

    public SetObjects[] separation(SetObjects set) throws IOException{
        SetObjects[] goodGlobalSets = null;
        Double maxGlobalGini = null;
        String goodAttributName = null;

        Set<String> attributNames = set.getAttributNames();
        for(String attributName:attributNames) {

            SetObjects[] goodLocalSets = null;
            Double maxLocalGini = null;
            double gini = 0;
            ArrayList<Double> thresholdValues = set.getThresholdValues(attributName);
            for(Double thresholdValue:thresholdValues){
                SetObjects[] childSets = set.separation(attributName, thresholdValue);
                gini = gini(childSets);
                if (maxLocalGini == null || gini > maxLocalGini){
                    maxLocalGini = gini;
                    goodLocalSets = childSets;
                }
            }
            System.out.println("Разбиение по атрибуту " + attributName + " построено. gini=" + maxLocalGini);

            if (maxGlobalGini == null || maxLocalGini > maxGlobalGini){
                maxGlobalGini = maxLocalGini;
                goodGlobalSets = goodLocalSets;
                goodAttributName = attributName;
            }
        }
        System.out.println("Выбран атрибут " + goodAttributName + " с gini=" + maxGlobalGini);
        //System.out.println("Исходное множество " + set.getClazzes());
        System.out.println("Исходное множество " + set);
        for (int i = 0; i<goodGlobalSets.length; i++){
            //System.out.println(i + " потомок " + goodGlobalSets[i].getClazzes());
            System.out.println(i + " потомок " + goodGlobalSets[i]);
        }
        return goodGlobalSets;
    }

    private double gini(SetObjects[] childSets){
        SetObjects leftSet = childSets[0];
        HashMap<String, Integer> leftSetClazzes = leftSet.getClazzes();
        double Sl = 0;
        for(String clazzName: leftSetClazzes.keySet()){
            double li = leftSetClazzes.get(clazzName);
            Sl += li * li;
        }

        SetObjects rightSet = childSets[1];
        HashMap<String, Integer> rightClazzes = rightSet.getClazzes();
        double Sr = 0;
        for(String clazzName: rightClazzes.keySet()){
            double ri = rightClazzes.get(clazzName);
            Sr += ri + ri;
        }

        return  (Sl/leftSet.getCountObjects())+(Sr/rightSet.getCountObjects());
    }
}
