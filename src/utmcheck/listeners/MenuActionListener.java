package utmcheck.listeners;

import utmcheck.view.TxtFileFilter;
import utmcheck.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MenuActionListener implements ActionListener{
    private View view;

    public MenuActionListener(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        JFileChooser fileChooser;
        int returnVal;
        switch (command) {
            case "Выбрать файл загрузки":
                //GUI for choosing file to load with default path
                fileChooser = new JFileChooser(view.getPathToLoad());
                //adding *.txt filter for file chooser
                fileChooser.setFileFilter(new TxtFileFilter());
                //setting dialog type
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                //setting dialog title
                fileChooser.setDialogTitle("Выбрать txt-файл со списком магазинов");
                //opening window for file selection
                returnVal = fileChooser.showDialog((JFrame)view, "Выбрать файл");

                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File currentFile = fileChooser.getSelectedFile();
                    view.setPathToLoad(currentFile.getAbsolutePath());
                }
                break;

            case "Сохранить все обработанные данные":
                //GUI for saving file to load with selected path
                fileChooser = new JFileChooser(view.getFolderToLoad());
                //setting dialog type
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                //setting only directory selection mode
                //setting dialog title
                fileChooser.setDialogTitle("Выберите папку для сохранения обработанных данных");
                //opening window for folder selection
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                returnVal = fileChooser.showDialog((JFrame)view, "Сохранить файлы");

                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File currentFile = fileChooser.getSelectedFile();
                    view.saveAllCache(currentFile);
                }
                break;

            case "Сохранить лог":
                //GUI for saving file to load with selected path
                fileChooser = new JFileChooser(view.getPathToLoad());
                //adding *.txt filter for file chooser
                fileChooser.setFileFilter(new TxtFileFilter());
                //setting dialog type
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                //setting dialog title
                fileChooser.setDialogTitle("Сохранить лог в txt-файл");
                //opening window for file selection
                returnVal = fileChooser.showDialog((JFrame)view, "Сохранить");

                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File currentFile = fileChooser.getSelectedFile();
                    view.saveViewLog(currentFile);
                }
                break;

            case "О программе":
                view.showAbout();
                break;

            case "Выход":
                ((JFrame)view).dispose();
                break;
        }
    }
}
