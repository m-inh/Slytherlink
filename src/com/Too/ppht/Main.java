package com.Too.ppht;

import com.Too.ppht.IO.FileManager;
import com.Too.ppht.commons.CommonVLs;
import com.Too.ppht.models.Block;
import com.Too.ppht.models.Clause;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //get path to file input
        FileManager fileMgr = new FileManager();
        String inputPath = fileMgr.getPathFromName("/input/input.cnf");

        // init block[][]
        int block[][] = {{2, 3, 2}, {2, 0, 2}, {2, 1, 2}};
        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[0].length; j++) {
                System.out.print(block[i][j] + " ");
            }
            System.out.println();
        }

        // innit side (canh), apex (dinh)
        boolean side[] = new boolean[24];


        // encode 1st law
        ArrayList<Clause> clauseArr = new ArrayList<>();
        encode1stLaw(clauseArr, block);

        // encode 2nd law
        encode2ndLaw(clauseArr);

        // write to input.cnf
        fileMgr.writeToInputCNF("input.cnf", clauseArr);

        System.out.println("-------------------");

        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        // CNF filename is given on the command line
        try {
            IProblem problem = reader.parseInstance(inputPath);
            if (problem.isSatisfiable()) {
                System.out.println("Satisfiable !");
//                System.out.println(reader.decode(problem.model()));
                int result[] = problem.model();
                for (int i = 0; i < result.length; i++) {
                    System.out.print(result[i] + ",");
                }
            } else {
                System.out.println("Unsatisfiable !");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ContradictionException e) {
            e.printStackTrace();
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            e.printStackTrace();
            System.out.println("Timeout, sorry!");
        }

    }


    public static void encode1stLaw(ArrayList<Clause> clauseArr1stLaw, int[][] block) {
        int n = CommonVLs.COLUMNS_BOARD;
        int m = CommonVLs.ROWS_BOARD;

        for (int x = 0; x < block[0].length; x++) {
            for (int y = 0; y < block.length; y++) {
                int up = n * y + x;
                int down = up + n;
                int left = (m + 1) * n + y * (n + 1) + x;
                int right = left + 1;

//                if (x == 2 && y == 0) {
//                    System.out.println("Number: " + block[y][x]);
//                    System.out.println(left);
//                    System.out.println(right);
//                    System.out.println(up);
//                    System.out.println(down);
//                }

                if (block[y][x] == 0) {
                    System.out.println("Number: " + block[y][x]);
                    int tempClause1[] = new int[1];
                    int tempClause2[] = new int[1];
                    int tempClause3[] = new int[1];
                    int tempClause4[] = new int[1];
                    tempClause1[0] = (up + 1) * (-1);
                    tempClause2[0] = (down + 1) * (-1);
                    tempClause3[0] = (left + 1) * (-1);
                    tempClause4[0] = (right + 1) * (-1);

//                    System.out.println(tempClause1[0] + " ");
//                    System.out.println(tempClause2[0] + " ");
//                    System.out.println(tempClause3[0] + " ");
//                    System.out.println(tempClause4[0] + " ");
//                    System.out.println();

                    Clause clause01 = new Clause(tempClause1);
                    Clause clause02 = new Clause(tempClause2);
                    Clause clause03 = new Clause(tempClause3);
                    Clause clause04 = new Clause(tempClause4);
                    clauseArr1stLaw.add(clause01);
                    clauseArr1stLaw.add(clause02);
                    clauseArr1stLaw.add(clause03);
                    clauseArr1stLaw.add(clause04);

                } else if (block[y][x] == 1) {
//                    System.out.println("Number: " + block[y][x]);
                    int tempClause1[] = new int[4];
                    int tempClause2[] = new int[2];
                    int tempClause3[] = new int[2];
                    int tempClause4[] = new int[2];
                    int tempClause5[] = new int[2];
                    int tempClause6[] = new int[2];
                    int tempClause7[] = new int[2];

                    tempClause1[0] = up + 1;
                    tempClause1[1] = down + 1;
                    tempClause1[2] = left + 1;
                    tempClause1[3] = right + 1;

                    tempClause2[0] = up + 1;
                    tempClause2[0] *= (-1);
                    tempClause2[1] = down + 1;
                    tempClause2[1] *= (-1);

                    tempClause3[0] = up + 1;
                    tempClause3[0] *= (-1);
                    tempClause3[1] = left + 1;
                    tempClause3[1] *= (-1);

                    tempClause4[0] = up + 1;
                    tempClause4[0] *= (-1);
                    tempClause4[1] = right + 1;
                    tempClause4[1] *= (-1);

                    tempClause5[0] = down + 1;
                    tempClause5[0] *= (-1);
                    tempClause5[1] = left + 1;
                    tempClause5[1] *= (-1);

                    tempClause6[0] = down + 1;
                    tempClause6[0] *= (-1);
                    tempClause6[1] = right + 1;
                    tempClause6[1] *= (-1);

                    tempClause7[0] = left + 1;
                    tempClause7[0] *= (-1);
                    tempClause7[1] = right + 1;
                    tempClause7[1] *= (-1);

//                    System.out.println(tempClause1[0] + " " + tempClause1[1] + " " + tempClause1[2] + " " + tempClause1[3]);
//                    System.out.println(tempClause2[0] + " " + tempClause2[1]);
//                    System.out.println(tempClause3[0] + " " + tempClause3[1]);
//                    System.out.println(tempClause4[0] + " " + tempClause4[1]);
//                    System.out.println(tempClause5[0] + " " + tempClause5[1]);
//                    System.out.println(tempClause6[0] + " " + tempClause6[1]);
//                    System.out.println(tempClause7[0] + " " + tempClause7[1]);
//                    System.out.println();

                    Clause clause01 = new Clause(tempClause1);
                    Clause clause02 = new Clause(tempClause2);
                    Clause clause03 = new Clause(tempClause3);
                    Clause clause04 = new Clause(tempClause4);
                    Clause clause05 = new Clause(tempClause5);
                    Clause clause06 = new Clause(tempClause6);
                    Clause clause07 = new Clause(tempClause7);
                    clauseArr1stLaw.add(clause01);
                    clauseArr1stLaw.add(clause02);
                    clauseArr1stLaw.add(clause03);
                    clauseArr1stLaw.add(clause04);
                    clauseArr1stLaw.add(clause05);
                    clauseArr1stLaw.add(clause06);
                    clauseArr1stLaw.add(clause07);
                } else if (block[y][x] == 2) {
//                    System.out.println("Number: " + block[y][x]);
                    int tempClause1[] = new int[3];
                    int tempClause2[] = new int[3];
                    int tempClause3[] = new int[3];
                    int tempClause4[] = new int[3];
                    int tempClause5[] = new int[3];
                    int tempClause6[] = new int[3];
                    int tempClause7[] = new int[3];
                    int tempClause8[] = new int[3];
                    tempClause1[0] = (up + 1);
                    tempClause1[1] = (right + 1);
                    tempClause1[2] = (down + 1);

                    tempClause2[0] = (up + 1);
                    tempClause2[1] = (right + 1);
                    tempClause2[2] = (left + 1);

                    tempClause3[0] = (up + 1);
                    tempClause3[1] = (down + 1);
                    tempClause3[2] = (left + 1);

                    tempClause4[0] = (right + 1);
                    tempClause4[1] = (down + 1);
                    tempClause4[2] = (left + 1);

                    tempClause5[0] = tempClause1[0] * (-1);
                    tempClause5[1] = tempClause1[1] * (-1);
                    tempClause5[2] = tempClause1[2] * (-1);

                    tempClause6[0] = tempClause2[0] * (-1);
                    tempClause6[1] = tempClause2[1] * (-1);
                    tempClause6[2] = tempClause2[2] * (-1);

                    tempClause7[0] = tempClause3[0] * (-1);
                    tempClause7[1] = tempClause3[1] * (-1);
                    tempClause7[2] = tempClause3[2] * (-1);

                    tempClause8[0] = tempClause4[0] * (-1);
                    tempClause8[1] = tempClause4[1] * (-1);
                    tempClause8[2] = tempClause4[2] * (-1);


//                    System.out.println(tempClause1[0] + " " + tempClause1[1] + " " + tempClause1[2]);
//                    System.out.println(tempClause2[0] + " " + tempClause2[1] + " " + tempClause2[2]);
//                    System.out.println(tempClause3[0] + " " + tempClause3[1] + " " + tempClause3[2]);
//                    System.out.println(tempClause4[0] + " " + tempClause4[1] + " " + tempClause4[2]);
//                    System.out.println(tempClause5[0] + " " + tempClause5[1] + " " + tempClause5[2]);
//                    System.out.println(tempClause6[0] + " " + tempClause6[1] + " " + tempClause6[2]);
//                    System.out.println(tempClause7[0] + " " + tempClause7[1] + " " + tempClause7[2]);
//                    System.out.println(tempClause8[0] + " " + tempClause8[1] + " " + tempClause8[2]);
//                    System.out.println();

                    Clause clause01 = new Clause(tempClause1);
                    Clause clause02 = new Clause(tempClause2);
                    Clause clause03 = new Clause(tempClause3);
                    Clause clause04 = new Clause(tempClause4);
                    Clause clause05 = new Clause(tempClause5);
                    Clause clause06 = new Clause(tempClause6);
                    Clause clause07 = new Clause(tempClause7);
                    Clause clause08 = new Clause(tempClause8);
                    clauseArr1stLaw.add(clause01);
                    clauseArr1stLaw.add(clause02);
                    clauseArr1stLaw.add(clause03);
                    clauseArr1stLaw.add(clause04);
                    clauseArr1stLaw.add(clause05);
                    clauseArr1stLaw.add(clause06);
                    clauseArr1stLaw.add(clause07);
                    clauseArr1stLaw.add(clause08);
                } else if (block[y][x] == 3) {
//                    System.out.println("Number: " + block[y][x]);
                    int tempClause1[] = new int[4];
                    int tempClause2[] = new int[2];
                    int tempClause3[] = new int[2];
                    int tempClause4[] = new int[2];
                    int tempClause5[] = new int[2];
                    int tempClause6[] = new int[2];
                    int tempClause7[] = new int[2];

                    tempClause1[0] = up + 1;
                    tempClause1[0] *= (-1);
                    tempClause1[1] = down + 1;
                    tempClause1[1] *= (-1);
                    tempClause1[2] = left + 1;
                    tempClause1[2] *= (-1);
                    tempClause1[3] = right + 1;
                    tempClause1[3] *= (-1);

                    tempClause2[0] = up + 1;
                    tempClause2[1] = down + 1;

                    tempClause3[0] = up + 1;
                    tempClause3[1] = left + 1;

                    tempClause4[0] = up + 1;
                    tempClause4[1] = right + 1;

                    tempClause5[0] = down + 1;
                    tempClause5[1] = left + 1;

                    tempClause6[0] = down + 1;
                    tempClause6[1] = right + 1;

                    tempClause7[0] = left + 1;
                    tempClause7[1] = right + 1;

//                    System.out.println(tempClause1[0] + " " + tempClause1[1] + " " + tempClause1[2] + " " + tempClause1[3]);
//                    System.out.println(tempClause2[0] + " " + tempClause2[1]);
//                    System.out.println(tempClause3[0] + " " + tempClause3[1]);
//                    System.out.println(tempClause4[0] + " " + tempClause4[1]);
//                    System.out.println(tempClause5[0] + " " + tempClause5[1]);
//                    System.out.println(tempClause6[0] + " " + tempClause6[1]);
//                    System.out.println(tempClause7[0] + " " + tempClause7[1]);
                    System.out.println();

                    Clause clause01 = new Clause(tempClause1);
                    Clause clause02 = new Clause(tempClause2);
                    Clause clause03 = new Clause(tempClause3);
                    Clause clause04 = new Clause(tempClause4);
                    Clause clause05 = new Clause(tempClause5);
                    Clause clause06 = new Clause(tempClause6);
                    Clause clause07 = new Clause(tempClause7);
                    clauseArr1stLaw.add(clause01);
                    clauseArr1stLaw.add(clause02);
                    clauseArr1stLaw.add(clause03);
                    clauseArr1stLaw.add(clause04);
                    clauseArr1stLaw.add(clause05);
                    clauseArr1stLaw.add(clause06);
                    clauseArr1stLaw.add(clause07);
                } else if (block[y][x] == 4) {
//                    System.out.println("Number: " + block[y][x]);
                    int tempClause1[] = new int[1];
                    int tempClause2[] = new int[1];
                    int tempClause3[] = new int[1];
                    int tempClause4[] = new int[1];
                    tempClause1[0] = (up + 1);
                    tempClause2[0] = (down + 1);
                    tempClause3[0] = (left + 1);
                    tempClause4[0] = (right + 1);

//                    System.out.println(tempClause1[0] + " ");
//                    System.out.println(tempClause2[0] + " ");
//                    System.out.println(tempClause3[0] + " ");
//                    System.out.println(tempClause4[0] + " ");
//                    System.out.println();

                    Clause clause01 = new Clause(tempClause1);
                    Clause clause02 = new Clause(tempClause2);
                    Clause clause03 = new Clause(tempClause3);
                    Clause clause04 = new Clause(tempClause4);
                    clauseArr1stLaw.add(clause01);
                    clauseArr1stLaw.add(clause02);
                    clauseArr1stLaw.add(clause03);
                    clauseArr1stLaw.add(clause04);
                }
            }
        }

        System.out.println("number of clause: " + clauseArr1stLaw.size());
    }


    //////////////////////////// the 2nd law
    public static void encode2ndLaw(ArrayList<Clause> clauseArr2ndLaw) {
        int n = CommonVLs.COLUMNS_BOARD;
        int m = CommonVLs.ROWS_BOARD;

//        int numberOfApex = (n +1) * (m + 1);
        // apex[n+1][m+1]

        for (int y = 0; y < n + 1; y++) {
            for (int x = 0; x < m + 1; x++) {
                // apex(x,y)
                // topLeft
                // bottomRight
//                Block topLeft = Block.getBlockInfo(x - 1, y - 1);
//                Block bottomRight = Block.getBlockInfo(x, y);
//
//                Block topRight = Block.getBlockInfo(x, y - 1);
//                Block bottomLeft = Block.getBlockInfo(x - 1, y);

                Block topLeft = Block.getBlockInfo(y - 1, x - 1);
                Block bottomRight = Block.getBlockInfo(y, x);

                Block topRight = Block.getBlockInfo(y, x - 1);
                Block bottomLeft = Block.getBlockInfo(y - 1, x);

                int up = topLeft.getRight();
                int left = topLeft.getDown();
                int down = bottomRight.getLeft();
                int right = bottomRight.getUp();

                if (topRight.getLeft() > 0) {
                    up = topRight.getLeft();
                }
                if (topRight.getDown() > 0) {
                    right = topRight.getDown();
                }
                if (bottomLeft.getUp() > 0) {
                    left = bottomLeft.getUp();
                }
                if (bottomLeft.getRight() > 0) {
                    down = bottomLeft.getRight();
                }

                if (up == 0) {
                    up = Clause.MAX_VALUE;
                }
                if (down == 0) {
                    down = Clause.MAX_VALUE;
                }
                if (left == 0) {
                    left = Clause.MAX_VALUE;
                }
                if (right == 0) {
                    right = Clause.MAX_VALUE;
                }

//                System.out.println(x + " " + y);
//                System.out.println("up: " + up);
//                System.out.println("down: " + down);
//                System.out.println("left: " + left);
//                System.out.println("right: " + right);
//                System.out.println();

                int X1 = up;
                int X2 = right;
                int X3 = down;
                int X4 = left;


                // (¬X1∨¬X2∨¬X3)
                Clause clause1 = new Clause();
                clause1.addNewVar(X1 * -1);
                clause1.addNewVar(X2 * -1);
                clause1.addNewVar(X3 * -1);

                if (clause1.toString().length() > 0) {
                    clauseArr2ndLaw.add(clause1);
                }
                // (¬X1∨¬X2∨¬X4)
                Clause clause2 = new Clause();
                clause2.addNewVar(X1 * -1);
                clause2.addNewVar(X2 * -1);
                clause2.addNewVar(X4 * -1);
                if (clause2.toString().length() > 0) {
                    clauseArr2ndLaw.add(clause2);
                }
                // (¬X1∨¬X3∨¬X4)
                Clause clause3 = new Clause();
                clause3.addNewVar(X1 * -1);
                clause3.addNewVar(X3 * -1);
                clause3.addNewVar(X4 * -1);
                if (clause3.toString().length() > 0) {
                    clauseArr2ndLaw.add(clause3);
                }
                // (¬X2∨¬X3∨¬X4)
                Clause clause4 = new Clause();
                clause4.addNewVar(X2 * -1);
                clause4.addNewVar(X3 * -1);
                clause4.addNewVar(X4 * -1);
                if (clause4.toString().length() > 0) {
                    clauseArr2ndLaw.add(clause4);
                }
                // (¬X1∨X2∨X3∨X4)
                Clause clause5 = new Clause();
                clause5.addNewVar(X1 * -1);
                clause5.addNewVar(X2);
                clause5.addNewVar(X3);
                clause5.addNewVar(X4);
                if (clause5.toString().length() > 0) {
                    clauseArr2ndLaw.add(clause5);
                }
                // (X1∨¬X2∨X3∨X4)
                Clause clause6 = new Clause();
                clause6.addNewVar(X1);
                clause6.addNewVar(X2 * -1);
                clause6.addNewVar(X3);
                clause6.addNewVar(X4);
                if (clause6.toString().length() > 0) {
                    clauseArr2ndLaw.add(clause6);
                }
                // (X1∨X2∨¬X3∨X4)
                Clause clause7 = new Clause();
                clause7.addNewVar(X1);
                clause7.addNewVar(X2);
                clause7.addNewVar(X3 * -1);
                clause7.addNewVar(X4);
                if (clause7.toString().length() > 0) {
                    clauseArr2ndLaw.add(clause7);
                }
                // (X1∨X2∨X3∨¬X4)
                Clause clause8 = new Clause();
                clause8.addNewVar(X1);
                clause8.addNewVar(X2);
                clause8.addNewVar(X3);
                clause8.addNewVar(X4 * -1);
                if (clause8.toString().length() > 0) {
                    clauseArr2ndLaw.add(clause8);
                }

            }
        }
    }


}
