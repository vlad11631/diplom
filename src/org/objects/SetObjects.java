package org.objects;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.tree.Condition;
import org.tree.SymbolCondition;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SetObjects {

    private String fileName;
    private HashMap<String, Integer> attributs;
    private ArrayList<Condition> conditions;
    private HashMap<String, Integer> clazzes;
    private HSSFWorkbook workbook;


    public SetObjects(String fileName) throws IOException{
        this(fileName, null, null, null);
    }

    public SetObjects(String fileName, HashMap<String, Integer> attributs, ArrayList<Condition> conditions, HashMap<String, Integer> clazzes) throws IOException{
        try {
            //Открытие потока в файл
            workbook = new HSSFWorkbook(new FileInputStream(fileName));

            this.fileName = fileName;

            //заполнение списка атрибутов
            if (attributs == null) {
                this.attributs = new HashMap<>();
                Sheet firstSheet = workbook.getSheetAt(0);
                for (int i = 0; i < firstSheet.getLastRowNum(); i++) {
                    String name = firstSheet.getRow(i).getCell(0).getStringCellValue();
                    this.attributs.put(name, i);
                }
            }else{
                this.attributs = new HashMap<>(attributs);
            }

            //заполнение списка условий этого множества
            if (conditions == null){
                this.conditions = new ArrayList<Condition>();
            }else{
                this.conditions = new ArrayList<Condition>(conditions);
            }

            //заполнение списка кдассов
            if (clazzes == null){
                this.clazzes = new HashMap<>();
                for (int i = 1; i< workbook.getNumberOfSheets(); i++) {//проход по вкладкам
                    Sheet sheet = workbook.getSheetAt(i);
                    String name = sheet.getSheetName();
                    int count = sheet.getRow(0).getLastCellNum();
                    this.clazzes.put(name, count);
                }
            }else{
                this.clazzes = new HashMap<>(clazzes);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public HashMap<String, Integer> getAttributs() {
        return new HashMap<>(attributs);
    }

    public Set<String> getAttributNames() {
        return attributs.keySet();
    }

    public ArrayList<Condition> getConditions() {
        return new ArrayList<>(conditions);
    }

    public Condition getLastCondition(){
        return  conditions.get(conditions.size()-1);
    }

    public HashMap<String, Integer> getClazzes() {
        return new HashMap<>(clazzes);
    }

    public int getCountObjects() {
        int sum = 0;
        for(String key: clazzes.keySet()){
            sum+=clazzes.get(key);
        }
        return sum;
    }

    public ArrayList<Double> getThresholdValues(String attributName){
        ArrayList<Double> thresholdValues = new ArrayList<Double>(); //массив пороговых значений
        Double lastThresholdValue = null; //последнее пороговое значение
        boolean exit = false; //флаг выхода
        while (!exit) {
            //два самых минимальных числа
            Double min1 = null;
            Double min2 = null;

            for (int indexSheet = 1; indexSheet< workbook.getNumberOfSheets(); indexSheet++){//проход по вкладкам
                Sheet sheet = workbook.getSheetAt(indexSheet);

                int indexRow = attributs.get(attributName);
                Row row = sheet.getRow(indexRow);
                for (int indexCell = 0; indexCell< row.getLastCellNum(); indexCell++) { //проход по ячейкам строки
                    //проверка: удовлетворяет ли значение условиям данного множества
                    boolean check = true;
                    for (Condition condition: conditions){
                        int ri = attributs.get(condition.getAttributName());
                        double v = sheet.getRow(ri).getCell(indexCell).getNumericCellValue();
                        if (!condition.isCheck(v)){
                            check=false;
                            break;
                        }
                    }
                    if (!check) continue;

                    double value = row.getCell(indexCell).getNumericCellValue();
                     //нужно учитывать значения выше последнего порогового, так как значения меньше были учитаны ранее
                    if (lastThresholdValue == null || value > lastThresholdValue) {
                        //нахождение двух минимальных чисел
                        if (min1 == null || value < min1) {
                            min2 = min1;
                            min1 = value;
                        } else if (value != min1 && (min2 == null || value < min2)) {
                            min2 = value;
                        }
                    }
                }
            }

            //если одно из значений осталось пустым, то пороговых значений больше нет. выходим из цыкла
            if (min2 == null || min1 == null){
                exit = true;
            }else {
                //считаем пороговое значение
                lastThresholdValue = (min1 + min2) / 2;
                thresholdValues.add(lastThresholdValue);
            }
        }
        return  thresholdValues;
    }

    public SetObjects[] separation(String attributName, double thresholdValue) throws IOException{
        HashMap<String, Integer> clazzes1 = new HashMap<>();
        HashMap<String, Integer> clazzes2 = new HashMap<>();

        for (int indexSheet = 1; indexSheet < workbook.getNumberOfSheets(); indexSheet++) {
            Sheet sheet = workbook.getSheetAt(indexSheet);
            String sheetName = sheet.getSheetName();

            if (clazzes.get(sheetName) == 0) continue;;

            int count1 = 0;
            int count2 = 0;
            int indexRow = attributs.get(attributName);
            Row row = sheet.getRow(indexRow);
            for (int indexCell = 0; indexCell< row.getLastCellNum(); indexCell++){ //проход по ячейкам строки

                //проверка: удовлетворяет ли значение условиям данного множества
                boolean check = true;
                for (Condition condition: conditions){
                    int ri = attributs.get(condition.getAttributName());
                    double v = sheet.getRow(ri).getCell(indexCell).getNumericCellValue();
                    if (!condition.isCheck(v)){
                        check=false;
                        break;
                    }
                }
                if (!check) continue;

                double value = row.getCell(indexCell).getNumericCellValue();

                if (value < thresholdValue){
                    count1++;
                }else{
                    count2++;
                }
            }
            clazzes1.put(sheetName, count1);
            clazzes2.put(sheetName, count2);
        }

        //создание множества меньше порогового
        ArrayList<Condition> conditions1 = new ArrayList<>(this.conditions);
        conditions1.add(new Condition(attributName, SymbolCondition.Less, thresholdValue));
        SetObjects set1 = new SetObjects(fileName, this.attributs, conditions1, clazzes1);

        //создание множества больше или равно пороговому
        ArrayList<Condition> conditions2 = new ArrayList<>(this.conditions);
        conditions2.add(new Condition(attributName, SymbolCondition.MoreOrEqual, thresholdValue));
        SetObjects set2 = new SetObjects(fileName, this.attributs, conditions2, clazzes2);
        SetObjects[] result = {set1, set2};
        return result;
    }

    public String toString(){
        StringBuffer result = new StringBuffer();
        result.append("Set{");
        result.append(" Condition: "+conditions);
        result.append(" Clazzes: "+ clazzes);
        result.append(" All= "+ getCountObjects());
        result.append(" }");
        return  result.toString();
    }

}
