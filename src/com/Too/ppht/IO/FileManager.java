package com.Too.ppht.IO;

import com.Too.ppht.commons.CommonVLs;
import com.Too.ppht.models.Clause;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

    public FileManager() {
    }

    public String getPathFromName(String name) {
        String path = "";
        try {
            path = getClass().getResource(name).getPath();
            File file = new File(path);
            if (file.isFile()) {
//                System.out.println("file ton tai");
//                System.out.println(path);
//                System.out.println("--------------------------");
            } else if (file.isDirectory()){
//                System.out.println("is directory");
            } else{
//                System.out.println("file ko ton tai");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

    private BufferedWriter writer;

    private boolean openInputFile(String path) {

        try {
            FileWriter fileWriter = new FileWriter(path);
            writer = new BufferedWriter(fileWriter);
//            System.out.println("open ok");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void createNewInputFile(String path, String fileName){
        File file = new File(path + fileName);
        if (!file.exists()){
            try {
                file.createNewFile();
//                System.out.println("new file out: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
//            System.out.println("file out: " + file.getAbsolutePath());
        }
    }

    public void writeToInputCNF(String fileName, ArrayList<Clause> clauseArr) {
        createNewInputFile(getPathFromName("/input/"), fileName);
        if (openInputFile(getPathFromName("/input/" + fileName))){
            try {
                // write 1st line
                int varCount = (CommonVLs.COLUMNS_BOARD + 1)*CommonVLs.ROWS_BOARD + CommonVLs.COLUMNS_BOARD*(CommonVLs.ROWS_BOARD + 1);
                String firstLine = "p cnf " + varCount + " " + clauseArr.size();
                writer.write(firstLine);
                writer.newLine();

                for (int i = 0; i < clauseArr.size(); i++) {
                    writer.write(clauseArr.get(i).toString() + "0");
                    writer.newLine();
                }
//                System.out.println("write ok");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("fail to open");
        }
    }

    public void writeToNextInput(String fileName, Clause newClause, int numberOfVar, int numberOfClause){
        if (openInputFile(getPathFromName("/input/" + fileName))){
            RandomAccessFile rdf;

        }
    }


//    public static void main(String[] args) {
//        FileManager fileMgr = new FileManager();
////		System.out.println(fileMgr.getPathFromName("/input/input.cnf"));
//
////        fileMgr.openInputFile(fileMgr.getPathFromName("/input/input.cnf"));
////        System.out.println("ok");
//
//        fileMgr.writeToInputCNF("input.cnf");
//    }
}
