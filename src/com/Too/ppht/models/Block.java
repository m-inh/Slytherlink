package com.Too.ppht.models;

import com.Too.ppht.commons.CommonVLs;

/**
 * Created by TooNies1810 on 4/29/16.
 */
public class Block {
    private int up;
    private int down;
    private int left;
    private int right;


    public Block(int up, int down, int left, int right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    //get info of block[x][y]
    public static Block getBlockInfo(int x, int y){
        int n = CommonVLs.COLUMNS_BOARD;
        int m = CommonVLs.ROWS_BOARD;

        if (x < 0 || y < 0 || x > n-1 || y > m-1){
            return new Block(0,0,0,0);
        }

        Block tempBlock;

        int up = n * y + x;
        int down = up + n;
        int left = (m + 1) * n + y * (n + 1) + x;
        int right = left + 1;

        tempBlock = new Block(up + 1,down + 1,left + 1,right + 1);

        return tempBlock;
    }

    public String toString(){
        String result = "";
        result += up + " " + down + " " + left + " "+right;
        return result;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }


}
