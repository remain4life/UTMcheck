package utmcheck.listeners;

import utmcheck.view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OtherButtonsListener implements ActionListener {
    private View view;

    public OtherButtonsListener(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Запустить проверку магазинов":
                //here controller starts new thread for real-time logging
                view.viewIPs();
                break;
            case "Прервать выполнение":
                view.workInterrupt();
                break;
            case "Вывести обработанные магазины":
                view.getController().onCachedShops();
                break;
            case "Вывести проблемные магазины":
                view.getController().onProblemShops();
                break;
            case "Очистить вывод":
                view.clearLogView();
                break;
            case "Очистить кэш обработки":
                view.getController().onClearModelData();
                view.getLogText().append(new Color(0, 100, 0), "База данных очищена." + System.lineSeparator());
                break;
            case "Выход":
                ((JFrame)view).dispose();
                break;
        }
    }
}
