package com.Too.ppht;

import com.Too.ppht.IO.FileManager;
import com.Too.ppht.commons.CommonVLs;
import com.Too.ppht.models.Clause;

import javax.swing.*;
import javax.swing.text.html.CSS;
import java.awt.*;
import java.text.AttributedCharacterIterator;
import java.util.*;

/**
 * Created by tmq on 22/04/2016.
 */
public class Drawing extends JPanel {
    private static final int BOX_NONE   = 0;
    private static final int BOX_INNER  = 1;
    private static final int BOX_OUTER  = 2;
    private static final Color COLOR_PRIMARY        = new Color(49, 176, 213);
    private static final Color COLOR_BACKGROUND     = new Color(255, 249, 196);
    private static final Color COLOR_ERROR          = Color.RED;

    private int n_size = 0;     // So cot
    private int m_size = 0;     // So hang
    private int width, height;  // Chieu ngang, chieu cao
    private int sizeBlock = 0;
    private int fontSize = 70;
    private Color colorOutput = COLOR_PRIMARY;

    private int [] inputData;
    private int [] outputData;
    private boolean onlyCycle = false;
    private int [] stateBox;

    private boolean isSolve = false;

    private Graphics2D graphics2D;

    private MyMenu myMenu;


    public Drawing(int type, MyMenu menu){
        super();

        setMyMenu(menu);

        setTypeMatrix(type);

        setBounds(MyMenu.WIDTH, 0, width+CommonVL.JUMP*2, height+CommonVL.JUMP*2);
        setLayout(null);
        setBackground(new Color(255, 253, 231));
    }

    // --------------------------------------- Set ---------------------------------------------------------------------
    public void reset(){
        colorOutput = COLOR_PRIMARY;
        isSolve = false;
        onlyCycle = false;
        stateBox = null;
    }

    private ArrayList<Clause> clauseArr;

    public void setTypeMatrix(int type){
        reset();
        // init block[][]
        int block[][];

        Random ran = new Random();
        switch (type){
            case CommonVL.TYPE_3x3:
                inputData = convertStringToArray(CommonVL.inputType3x3[ran.nextInt(CommonVL.inputType3x3.length)]);
                n_size = m_size = 3;
                break;
            case CommonVL.TYPE_5x5:
                inputData = convertStringToArray(CommonVL.inputType5x5[ran.nextInt(CommonVL.inputType5x5.length)]);
                n_size = m_size = 5;
                break;
            case CommonVL.TYPE_7x7:
                inputData = convertStringToArray(CommonVL.inputType7x7[ran.nextInt(CommonVL.inputType7x7.length)]);
                n_size = m_size = 7;
                break;
            case CommonVL.TYPE_10x10:
                inputData = convertStringToArray(CommonVL.inputType10x10[ran.nextInt(CommonVL.inputType10x10.length)]);
                n_size = m_size = 10;
                break;
            case CommonVL.TYPE_15x15:
                inputData = convertStringToArray(CommonVL.inputType15x15[ran.nextInt(CommonVL.inputType15x15.length)]);
                n_size = m_size = 15;
                break;
            case CommonVL.TYPE_20x20:
                inputData = convertStringToArray(CommonVL.inputType20x20[ran.nextInt(CommonVL.inputType20x20.length)]);
                n_size = m_size = 20;
                break;
            case CommonVL.TYPE_25x30:
                inputData = convertStringToArray(CommonVL.inputType25x30[ran.nextInt(CommonVL.inputType25x30.length)]);
                n_size = 25;
                m_size = 30;
                break;
        }
        // Chia o trong panel
        int divN = GUI.SIZE/n_size;
        int divM = GUI.SIZE/m_size;
        if (divM<divN) sizeBlock = divM;
        else sizeBlock = divN;
        // Set chieu rong, cao
        width = n_size*sizeBlock;
        height = m_size*sizeBlock;
        // Set kich thuoc so
        fontSize = sizeBlock/2;

        // done convert: array1[] -> array2[][]
        block = new int[m_size][n_size];
        for (int i=0; i<inputData.length; i++){
            int x = i%n_size;
            int y = i/n_size;
//            System.out.print("block[" + y + "," + x + "] = " + inputData[i] + "\n");
            block[y][x] = inputData[i];
        }
        CommonVLs.COLUMNS_BOARD = n_size;
        CommonVLs.ROWS_BOARD = m_size;

        //get path to file input
        FileManager fileMgr = new FileManager();
        String inputPath = fileMgr.getPathFromName("/input/input.cnf");

        for (int i = 0; i < block.length; i++) {
            for (int j = 0; j < block[0].length; j++) {
                System.out.print(block[i][j] + " ");
            }
            System.out.println();
        }

        // innit side (canh), apex (dinh)
//        boolean side[] = new boolean[24];


        // encode 1st law
        clauseArr = new ArrayList<>();
        Main.encode1stLaw(clauseArr, block);

        // encode 2nd law
        Main.encode2ndLaw(clauseArr);

        // write to input.cnf
        fileMgr.writeToInputCNF("input.cnf", clauseArr);

        System.out.println("-------------------");


        // number of var
        int varCount = (CommonVLs.COLUMNS_BOARD + 1)*CommonVLs.ROWS_BOARD + CommonVLs.COLUMNS_BOARD*(CommonVLs.ROWS_BOARD + 1);

        myMenu.getLbVariables().setText("Variables:  " + varCount + " (var)");
        myMenu.getLbClauses().setText(  "Clauses:    " + clauseArr.size() + " (clause)");
    }

    public void setSolve(boolean solve, int[] result){
        isSolve = solve;
        setOutputData(result);
    }
    private void setOutputData(int[] result){
        outputData = result;
    }

    // --------------------------------------- Draw --------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphics2D = (Graphics2D) g;

        graphics2D.setColor(COLOR_BACKGROUND);
        graphics2D.fillRect(CommonVL.JUMP, CommonVL.JUMP, width, height);

        drawBoxInCycle();
        drawLineMatrix();
        drawInput();
        if (isSolve) drawOutput();
    }

    // Ve cac o vuong
    private void drawLineMatrix(){
        graphics2D.setColor(COLOR_PRIMARY);
        float dash1[] = {5f};
        BasicStroke dashed = new BasicStroke(1.0f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND,
                        10.0f, dash1, 0.0f);
        graphics2D.setStroke(dashed);
        for (int i=0; i<n_size+1; i++){
            drawLine(sizeBlock * i, 0, sizeBlock * i, height);
        }
        for (int i=0; i<m_size+1; i++){
            drawLine(0, sizeBlock * i, width, sizeBlock * i);
        }
    }

    // Ve gia tri cho cac o vuong
    private void drawInput() {
        Graphics2D g = graphics2D;
        Font myFont = new Font ("Courier New", Font.BOLD, fontSize);
        g.setFont(myFont);
        g.setColor(COLOR_PRIMARY);
        int size = n_size * m_size;
        for (int i = 0; i < size; i++) {
            drawStringBox(inputData[i], i, g);
        }
    }

    // Ve cach giai bai toan
    private void drawOutput(){
        graphics2D.setColor(colorOutput);
        graphics2D.setStroke(new BasicStroke((float)(25/Math.sqrt(m_size)),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));

        int size = outputData.length;                       // So luong cac canh ( ngang + doc)
        int sizeEdgeRow = n_size * (m_size+1);              // So luong cac canh canh cua hang ngang

        for (int value : outputData) {
            if (value < 0) continue;                        // Neu khong co duong ke thi tiep tuc vong for
            int absValue = Math.abs(value);

            if (absValue <= sizeEdgeRow) {                  // Canh do la ngang
                int x = (absValue - 1) % n_size;            // Lay hoanh do trong matrix
                int y = (absValue - 1) / n_size;            // Lay tung do trong matrix

                int xx = x * sizeBlock;                     // Lay hoanh do trong panel
                int yy = y * sizeBlock;                     // Lay tung do trong panel

                drawLine(xx, yy, xx + sizeBlock, yy);
            } else {                                        // Canh do la doc
                int x = (absValue - 1 - sizeEdgeRow) % (n_size + 1);
                int y = (absValue - 1 - sizeEdgeRow) / (n_size + 1);

                int xx = x * sizeBlock;
                int yy = y * sizeBlock;

                drawLine(xx, yy, xx, yy + sizeBlock);
            }
        }
    }

    // Kiem tra chu trinh duy nhat
    public boolean fillBox(){
        stateBox = new int[inputData.length];                        // Luu trang thai danh dau cua cac o trong matrix
        // Duyet cac o o bien
        int check;
        int idValueInner = -1;
        for (int i=0; i<inputData.length; i++) {
            check = BOX_NONE;
            if (i<n_size){                                                  // Kiem tra hang ngang ben tren
                if (!topEdge(i)) check = BOX_OUTER;
                else check = BOX_INNER;
            }else if (i % n_size == 0){                                     // Kiem tra cot doc ben trai
                if (!leftEdge(i)) check = BOX_OUTER;
                else check = BOX_INNER;
            }else if (i % n_size == n_size - 1){                            // Kiem tra cot doc ben phai
                if (!rightEdge(i)) check = BOX_OUTER;
                else check = BOX_INNER;
            }else if (i > inputData.length - n_size){                       // Kiem tra hang ngang ben duoi
                if (!bottomEdge(i)) check = BOX_OUTER;
                else check = BOX_INNER;
            }
            if (check!=BOX_NONE) {
                if (idValueInner==-1 && check==BOX_INNER) {
                    stateBox[i] = check;
                    idValueInner = i;
                }
                else if (check==BOX_OUTER) stateBox[i] = BOX_OUTER;
            }
        }
        System.out.print("\n");

        // Xac dinh cac o con lai trong chu trinh
        Queue<Integer> queue = new LinkedList<Integer>();                   // Tao Queue luu tru cac o nam trong chu trinh
        queue.add(idValueInner);
        int id = -1;
        while (true){
            // O ben tren
            id = queue.element() - n_size;
            if (!isOutOfBound(id) && stateBox[id]==BOX_NONE && !bottomEdge(id)){
                stateBox[id] = BOX_INNER;     queue.add(id);
            }
            // O ben duoi
            id = queue.element() + n_size;
            if (!isOutOfBound(id) && stateBox[id]==BOX_NONE && !topEdge(id)){
                stateBox[id] = BOX_INNER;     queue.add(id);
            }
            // O ben trai
            id = queue.element() - 1;
            if (!isOutOfBound(id) && stateBox[id]==BOX_NONE && !rightEdge(id)){
                stateBox[id] = BOX_INNER;     queue.add(id);
            }
            // O ben phai
            id = queue.element() + 1;
            if (!isOutOfBound(id) && stateBox[id]==BOX_NONE && !leftEdge(id)){
                stateBox[id] = BOX_INNER;     queue.add(id);
            }
            queue.poll();                                                   // Xoa trong queue
            if (queue.isEmpty()) break;                                     // Neu queue rong thi khong duyet nua
        }

        // Duyet cac o ngoai chu trinh con
        for (int i=0; i<stateBox.length; i++){
            if (stateBox[i]==BOX_NONE){
                // O ben tren
                id = i - n_size;
                if (!isOutOfBound(id) &&
                        ((stateBox[id]==BOX_OUTER && !bottomEdge(id)) || (stateBox[id]==BOX_INNER && bottomEdge(id)))){
                    stateBox[i] = BOX_OUTER;
                }
                // O ben duoi
                id = i + n_size;
                if (!isOutOfBound(id) &&
                        ((stateBox[id]==BOX_OUTER && !topEdge(id)) || (stateBox[id]==BOX_INNER && topEdge(id)))){
                    stateBox[i] = BOX_OUTER;
                }
                // O ben trai
                id = i - 1;
                if (!isOutOfBound(id) && !(i%n_size==0) &&  // (Khong nam o ria ben trai)
                        ((stateBox[id]==BOX_OUTER && !rightEdge(id)) || (stateBox[id]==BOX_INNER && rightEdge(id)))){
                    stateBox[i] = BOX_OUTER;
                }
                // O ben phai
                id = i + 1;
                if (!isOutOfBound(id) && !(i%n_size==n_size-1) &&   // (Khong nam o ria ben phai)
                        ((stateBox[id]==BOX_OUTER && !rightEdge(i)) || (stateBox[id]==BOX_INNER && rightEdge(i)))){
                    stateBox[i] = BOX_OUTER;
                }
            }
        }
        for (int i=stateBox.length-1; i>=0; i--){   // Duyet theo chieu nguoc lai
            if (stateBox[i]==BOX_NONE){
                // O ben tren
                id = i - n_size;
                if (!isOutOfBound(id) &&
                        ((stateBox[id]==BOX_OUTER && !bottomEdge(id)) || (stateBox[id]==BOX_INNER && bottomEdge(id)))){
                    stateBox[i] = BOX_OUTER;
                }
                // O ben duoi
                id = i + n_size;
                if (!isOutOfBound(id) &&
                        ((stateBox[id]==BOX_OUTER && !topEdge(id)) || (stateBox[id]==BOX_INNER && topEdge(id)))){
                    stateBox[i] = BOX_OUTER;
                }
                // O ben trai
                id = i - 1;
                if (!isOutOfBound(id) && !(i%n_size==0) &&  // (Khong nam o ria ben trai)
                        ((stateBox[id]==BOX_OUTER && !rightEdge(id)) || (stateBox[id]==BOX_INNER && rightEdge(id)))){
                    stateBox[i] = BOX_OUTER;
                }
                // O ben phai
                id = i + 1;
                if (!isOutOfBound(id) && !(i%n_size==n_size-1) &&   // (Khong nam o ria ben phai)
                        ((stateBox[id]==BOX_OUTER && !rightEdge(i)) || (stateBox[id]==BOX_INNER && rightEdge(i)))){
                    stateBox[i] = BOX_OUTER;
                }
            }
        }


        // Dem so luong o da duoc dien
        int countBox = 0;
        for (int aStateBox : stateBox) {
            if (aStateBox > 0) {
//                drawStringBox(stateBox[i]+"", i, g);
                countBox++;
            }
        }
        // Kiem tra
        if (countBox==stateBox.length) {
            onlyCycle = true;
            colorOutput = COLOR_PRIMARY;
            System.out.print("1 Chu trinh duy nhat");
            System.out.println();
            return true;
        }else {
            System.out.print("Co nhieu hon 1 chu trinh");
            System.out.println();

            onlyCycle = false;
            colorOutput = COLOR_ERROR;
            return false;
        }
    }

    private void drawBoxInCycle(){
        if (stateBox==null) return;

        graphics2D.setColor(Color.YELLOW);
        for (int i=0; i<stateBox.length; i++){
            if (stateBox[i]==BOX_INNER){
                int x = tran((i%n_size) * sizeBlock);
                int y = tran((i/n_size) * sizeBlock);
                graphics2D.fillRect(x, y, sizeBlock, sizeBlock);
            }
        }
    }


    private void drawStringBox(int valueBox, int index, Graphics2D g){
        int x = tran((index%n_size) * sizeBlock + sizeBlock/2);
        int y = tran((index/n_size) * sizeBlock) + sizeBlock/2;
        g.drawString(valueBox==-1?"":valueBox+"", x-fontSize/4, y+fontSize/4);
    }

    // Kiem tra cac canh xung quanh co duoc dien khong
    private boolean topEdge(int indexBox){
        return outputData[indexBox] > 0;
    }
    private boolean bottomEdge(int indexBox){
        int id = indexBox + n_size;
        return outputData[id] > 0;
    }
    private boolean leftEdge(int indexBox){
        int id = n_size * (m_size+1) + indexBox*(n_size+1)/n_size;
        return outputData[id] > 0;
    }
    private boolean rightEdge(int indexBox){
        int id = (indexBox/n_size)*(n_size+1) + indexBox%n_size +1 + n_size * (m_size+1);
        return outputData[id] > 0;
    }
    private boolean isOutOfBound(int index){
        return (!(index >= 0 && index < inputData.length));
    }

    private void drawLine(int x, int y, int xx, int yy){
        graphics2D.drawLine(tran(x), tran(y), tran(xx), tran(yy));
    }

    private int [] convertStringToArray(String string){
        String[] items = string.replaceAll("\\[", "").replaceAll("\\]", "").split(",");
        int[] results = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            try {
                results[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException ignored) {};
        }
        return results;
    }

    private int tran(int x){
        return x+CommonVL.JUMP;
    }

    public MyMenu getMyMenu() {
        return myMenu;
    }

    public void setMyMenu(MyMenu myMenu) {
        this.myMenu = myMenu;
    }
    public ArrayList<Clause> getClauseArr(){
        return clauseArr;
    }
}
