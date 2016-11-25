package utmcheck.view;

import utmcheck.listeners.MenuActionListener;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuHelper {
    //action listener for menu operations
    ActionListener listener;

    public MenuHelper(View view) {
        listener = new MenuActionListener(view);
    }

    public JMenuItem addMenuItem(JMenu parent, String text, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setText(text);
        menuItem.addActionListener(actionListener);
        parent.add(menuItem);
        return menuItem;
    }


    public void initFileMenu(JMenuBar menuBar) {
        JMenu fileMenu = new JMenu("    Файл    ");
        menuBar.add(fileMenu);

        addMenuItem(fileMenu, "Выбрать файл загрузки", listener);
        addMenuItem(fileMenu, "Сохранить все обработанные данные", listener);
        addMenuItem(fileMenu, "Сохранить лог", listener);
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Выход", listener);
    }

    public void initHelpMenu(JMenuBar menuBar) {
        JMenu helpMenu = new JMenu("    Помощь     ");
        menuBar.add(helpMenu);

        addMenuItem(helpMenu, "О программе", listener);
    }


}
