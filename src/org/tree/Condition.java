package org.tree;

public class Condition {
    private String attributName;
    private SymbolCondition symbol;
    private Double value;

    public Condition(String attributName, SymbolCondition symbol, Double value){
        this.attributName=attributName;
        this.symbol=symbol;
        this.value=value;
    }

    public String getAttributName() {
        return attributName;
    }

    public SymbolCondition getCondition() {
        return symbol;
    }

    public Double getValue() {
        return value;
    }

    public  boolean isCheck(double value){
        if (symbol == SymbolCondition.More){
            return value > this.value;
        }else if (symbol == SymbolCondition.MoreOrEqual){
            return value >= this.value;
        }else if (symbol == SymbolCondition.Equal){
            return value == this.value;
        }else if (symbol == SymbolCondition.LessOrEqual){
            return value <= this.value;
        }else if (symbol == SymbolCondition.Less){
            return value < this.value;
        }
        return  false;
    }

    public String toString(){
        StringBuffer result = new StringBuffer();
        result.append(attributName);

        if (symbol == SymbolCondition.More){
            result.append(">");
        }else if (symbol == SymbolCondition.MoreOrEqual){
            result.append(">=");
        }else if (symbol == SymbolCondition.Equal){
            result.append("==");
        }else if (symbol == SymbolCondition.LessOrEqual){
            result.append("<=");
        }else if (symbol == SymbolCondition.Less){
            result.append("<");
        }

        result.append(value);
        return  result.toString();
    }
}
