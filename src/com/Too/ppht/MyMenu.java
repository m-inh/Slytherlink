package com.Too.ppht;

import com.Too.ppht.IO.FileManager;
import com.Too.ppht.commons.CommonVLs;
import com.Too.ppht.models.Clause;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by tmq on 26/04/2016.
 */
public class MyMenu extends JPanel implements ActionListener {
    public static final int WIDTH = 200;
    private static final String BTN_NEW = "btn_new";
    private static final String BTN_SOLVE = "btn_solve";
    private static final String BTN_STOP = "btn_stop";

    private JButton btnNew, btnSolve, btnStop;
    private JComboBox listMatrix;
    private JLabel lbVariables, lbClauses, lbTimes, lbIsRunning;


    private int currentTypeMatrix = CommonVL.TYPE_5x5;

    public MyMenu() {
        setBackground(Color.cyan);
        setLayout(null);

        initComponent();
    }

    private void initComponent() {
        // Nut choi moi
        btnNew = new JButton();
        btnNew.setText("New");
        btnNew.setBounds(20, 20, 160, 50);
        btnNew.setActionCommand(BTN_NEW);
        btnNew.addActionListener(this);

        // Nut giai bai toan
        btnSolve = new JButton();
        btnSolve.setText("Solve");
        btnSolve.setBounds(20, 350, 160, 50);
        btnSolve.setActionCommand(BTN_SOLVE);
        btnSolve.addActionListener(this);

        // Nut stop
        btnStop = new JButton();
        btnStop.setText("Stop");
        btnStop.setBounds(20, 420, 160, 50);
        btnStop.setActionCommand(BTN_STOP);
        btnStop.addActionListener(this);
        btnStop.setEnabled(false);

        // Label
        lbIsRunning = new JLabel();
        lbIsRunning.setText("");
        lbIsRunning.setForeground(Color.red);
        lbIsRunning.setBounds(20, 500, 160, 50);

        lbTimes = new JLabel();
        lbTimes.setText("Time:");
        lbTimes.setBounds(20, 550, 160, 50);

        lbClauses = new JLabel();
        lbClauses.setText("Clause:");
        lbClauses.setBounds(20, 600, 160, 50);

        lbVariables = new JLabel();
        lbVariables.setText("Variable:");
        lbVariables.setBounds(20, 650, 160, 50);

        // Lua chon man choi
        String[] choices = {"3x3", "5x5", "7x7", "10x10", "15x15", "20x20", "25x30"};
        listMatrix = new JComboBox(choices);
        listMatrix.setBounds(20, 200, 160, 30);
        listMatrix.setSelectedIndex(1);
        listMatrix.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                reset();
                if (e.getStateChange() == 2) return;
                String choice = (String) e.getItem();
                Drawing drawing = ((GUI) getRootPane().getParent()).getDrawing();
                switch (choice) {
                    case "3x3":
                        currentTypeMatrix = CommonVL.TYPE_3x3;
                        break;
                    case "5x5":
                        currentTypeMatrix = CommonVL.TYPE_5x5;
                        break;
                    case "7x7":
                        currentTypeMatrix = CommonVL.TYPE_7x7;
                        break;
                    case "10x10":
                        currentTypeMatrix = CommonVL.TYPE_10x10;
                        break;
                    case "15x15":
                        currentTypeMatrix = CommonVL.TYPE_15x15;
                        break;
                    case "20x20":
                        currentTypeMatrix = CommonVL.TYPE_20x20;
                        break;
                    case "25x30":
                        currentTypeMatrix = CommonVL.TYPE_25x30;
                        break;
                }
                drawing.setTypeMatrix(currentTypeMatrix);
                drawing.repaint();
            }
        });
//        listMatrix.

        // Add vao Panel
        add(btnNew);
        add(btnSolve);
        add(lbClauses);
        add(lbTimes);
        add(lbVariables);
        add(listMatrix);
        add(btnStop);
        add(lbIsRunning);
    }

    private void reset() {
        isRunning = true;
        isOnlyCycle = true;
        time = 0;
        lbIsRunning.setText("");
        lbTimes.setText("Time:");
        lbVariables.setText("Variable:");
        lbClauses.setText("Clause:");
    }

    Drawing drawing;
    boolean isOnlyCycle = true;
    private boolean isAllowedRunning = true;
    private boolean isRunning = true;
    private int time;

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        drawing = ((GUI) getRootPane().getParent()).getDrawing();
        switch (actionCommand) {
            case BTN_STOP:
                isAllowedRunning = false;
                break;
            case BTN_NEW:
                reset();
                drawing.setTypeMatrix(currentTypeMatrix);
                System.out.print("New");
                drawing.repaint();

                break;
            case BTN_SOLVE:
                lbIsRunning.setText("is running ...");
                lbIsRunning.setForeground(Color.red);
                listMatrix.setEnabled(false);
                btnNew.setEnabled(false);
                btnSolve.setEnabled(false);
                btnStop.setEnabled(true);
                solve();

                // thread for count time
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        time = 0;
                        while (isRunning) {
                            try {
                                Thread.sleep(1);
                                time++;
                                lbTimes.setText("Time:         " + time + " (ms)");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
//                        lbTimes.setText("Time:               " + time + " (ms)");
//                        System.out.println();
//                        System.out.println("time: " + time);
                        drawing.repaint();
                    }
                }).start();

                break;
        }
    }

    private void solve() {
        // thread for running
        new Thread(new Runnable() {
            @Override
            public void run() {
                //get path to file input
                FileManager fileMgr = new FileManager();
                String inputPath = fileMgr.getPathFromName("/input/input.cnf");

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
                        drawing.setSolve(true, result);
                        isOnlyCycle = drawing.fillBox();
                        //--------------------------------------------------
//                                lbVariables.setText("Variables:        " + result.length + " (var)");
                        if (isOnlyCycle) {
                            lbIsRunning.setText("Solved !!");
                            lbIsRunning.setForeground(new Color(27,94,32));

                            isRunning = false;
                            listMatrix.setEnabled(true);
                            btnSolve.setEnabled(true);
                            btnNew.setEnabled(true);
                            btnStop.setEnabled(false);
                            isAllowedRunning = true;
                        } else {
                            Clause.negativeClause(result);
                            drawing.getClauseArr().add(new Clause(result));

                            // write to input.cnf
                            fileMgr.writeToInputCNF("input.cnf", drawing.getClauseArr());

                            System.out.println("-------------------");


                            // number of var
//                            int varCount = (CommonVLs.COLUMNS_BOARD + 1)*CommonVLs.ROWS_BOARD + CommonVLs.COLUMNS_BOARD*(CommonVLs.ROWS_BOARD + 1);
//                            getLbVariables().setText("Variables: " + varCount + " (var)");
                            getLbClauses().setText(  "Clauses:    " + drawing.getClauseArr().size() + " (clause)");

                            isRunning = true;

                            if (isAllowedRunning){
                                solve();
                            } else {
                                isRunning = false;
                                listMatrix.setEnabled(true);
                                btnSolve.setEnabled(true);
                                btnNew.setEnabled(true);
                                btnStop.setEnabled(false);
                                isAllowedRunning = true;
                            }
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
        }).start();

        System.out.print("Solve");
        System.out.println();
    }

    public JLabel getLbVariables() {
        return lbVariables;
    }

    public JLabel getLbClauses() {
        return lbClauses;
    }

    public JLabel getLbTimes() {
        return lbTimes;
    }

}
