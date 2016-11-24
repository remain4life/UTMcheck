package utmcheck.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        addMenuItem(fileMenu, "Выбрать файл", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        addMenuItem(fileMenu, "Сохранить", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        addMenuItem(fileMenu, "Сохранить как...", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
