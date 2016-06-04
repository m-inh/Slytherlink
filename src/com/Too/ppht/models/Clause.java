package com.Too.ppht.models;

/**
 * Created by TooNies1810 on 4/26/16.
 */
public class Clause {
//    public static int ALWAYS_TRUE = -1;
//    public static int ALWAYS_FALSE = -2;
    public static int MAX_VALUE = 999999;

    private int variable[];

    public Clause(){
    }

    public Clause(int[] variable) {
        this.variable = variable;
    }

    public int[] getVariable() {
        return variable;
    }

    public void setVariable(int[] variable) {
        this.variable = variable;
    }

    public void addNewVar(int newVar){
        if (variable == null){
            variable = new int[1];
            variable[0] = newVar;
            return;
        }
        int tempVar[] = new int[variable.length + 1];
        for (int i = 0; i < variable.length; i++) {
            tempVar[i] = variable[i];
        }
        tempVar[tempVar.length - 1] = newVar;
        this.variable = tempVar;
    }

    public String toString(){
        for (int i = 0; i < variable.length; i++) {
            // neu bien do thuoc dang luon dung -> loai bo menh de nay
            if (variable[i] == MAX_VALUE){
                variable[i] = 0;
            }
            // neu bien do thuoc dang luon sai -> loai bo bien ra khoi menh de
            if (variable[i] == (MAX_VALUE * -1)){
                return "";
            }
        }

        String result = "";
        for (int i = 0; i < variable.length; i++) {
            if (variable[i] != 0){
                result += variable[i] + " ";
            }
        }
        return result;
    }

    public static void negativeClause(int[] clause){
        for (int i = 0; i < clause.length; i++) {
            clause[i] = clause[i] * (-1);
        }
    }

    public static void main(String arg[]){
//        Clause mClause = new Clause();
//        mClause.addNewVar(1);
////        mClause.addNewVar(MAX_VALUE* -1);
//        mClause.addNewVar(2);
////        mClause.addNewVar(MAX_VALUE* -1);
//        mClause.addNewVar(-4);
////        mClause.addNewVar(MAX_VALUE* -1);
////        mClause.addNewVar(MAX_VALUE);
//        if (mClause.toString().length() > 0){
//            System.out.println("add clause");
//        }
//        System.out.println(mClause.toString());

        int reslut[] = {1, 2, 4, -2, -5};
        Clause.negativeClause(reslut);
        Clause temp = new Clause(reslut);
        System.out.println(temp.toString());

    }
}
