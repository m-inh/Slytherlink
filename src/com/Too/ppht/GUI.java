package com.Too.ppht;

import javax.swing.*;
import java.awt.*;

/**
 * Created by tmq on 22/04/2016.
 */
public class GUI extends JFrame{
    public static final int SIZE = 700;
    public static final int WIDTH = 900;
    public static final int HEIGHT = 700;


    private Drawing drawing;
    private MyMenu menu;

    public GUI(){
        super("Slitherlink");

        setBounds(200, 50, WIDTH+CommonVL.JUMP*2, HEIGHT+CommonVL.JUMP*3);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        menu = new MyMenu();
        menu.setBounds(0, 0, MyMenu.WIDTH, GUI.HEIGHT+CommonVL.JUMP*3);
        drawing = new Drawing(CommonVL.TYPE_5x5,menu);

//        drawing.setMyMenu(menu);
        add(drawing);
        add(menu);
    }

    public Drawing getDrawing(){
        return drawing;
    }
}
