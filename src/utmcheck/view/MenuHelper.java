package utmcheck.view;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MenuHelper {

    public static JMenuItem addMenuItem(JMenu parent, String text, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setText(text);
        menuItem.addActionListener(actionListener);
        parent.add(menuItem);
        return menuItem;
    }


    public static void initFileMenu(final View view, JMenuBar menuBar) {
        JMenu fileMenu = new JMenu("    Файл    ");
        menuBar.add(fileMenu);

        addMenuItem(fileMenu, "Выбрать файл загрузки", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //GUI for choosing file to load with default path
                JFileChooser fileChooser = new JFileChooser(view.getPathToLoad());
                //adding *.txt filter for file chooser
                fileChooser.setFileFilter(new TxtFileFilter());
                //setting dialog type
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                //setting dialog title
                fileChooser.setDialogTitle("Выбрать txt-файл со списком магазинов");
                //opening window for file selection
                int returnVal = fileChooser.showDialog((JFrame)view, "Выбрать файл");

                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File currentFile = fileChooser.getSelectedFile();
                    view.setPathToLoad(currentFile.getAbsolutePath());
                }
            }
        });
        addMenuItem(fileMenu, "Сохранить все обработанные данные", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //GUI for saving file to load with selected path
                JFileChooser fileChooser = new JFileChooser(view.getFolderToLoad());
                //setting dialog type
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                //setting only directoru selection mode
                //setting dialog title
                fileChooser.setDialogTitle("Выберите папку для сохранения обработанных данных");
                //opening window for folder selection
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileChooser.showDialog((JFrame)view, "Сохранить файлы");

                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File currentFile = fileChooser.getSelectedFile();
                    view.saveAllCache(currentFile);
                }
            }
        });
        addMenuItem(fileMenu, "Сохранить лог", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //GUI for saving file to load with selected path
                JFileChooser fileChooser = new JFileChooser(view.getPathToLoad());
                //adding *.txt filter for file chooser
                fileChooser.setFileFilter(new TxtFileFilter());
                //setting dialog type
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                //setting dialog title
                fileChooser.setDialogTitle("Сохранить лог в txt-файл");
                //opening window for file selection
                int returnVal = fileChooser.showDialog((JFrame)view, "Сохранить");

                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File currentFile = fileChooser.getSelectedFile();
                    view.saveViewLog(currentFile);
                }
            }
        });
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Выход", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JFrame)view).dispose();
            }
        });
    }

    public static void initHelpMenu(final View view, JMenuBar menuBar) {
        JMenu helpMenu = new JMenu("    Помощь     ");
        menuBar.add(helpMenu);

        addMenuItem(helpMenu, "О программе", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showAbout();
            }
        });
    }


}
