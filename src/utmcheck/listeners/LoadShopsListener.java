package utmcheck.listeners;

import utmcheck.exceptions.NotCorrectFileException;
import utmcheck.view.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadShopsListener implements ActionListener {
    private View view;

    public LoadShopsListener(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //here we parsing file to list we'll work with
        try {
            view.loadModelData(view.getPathToLoad());
            //DarkGreen
            view.getLogText().append(new Color(0, 100, 0), "Данные загружены успешно, можно обрабатывать." + System.lineSeparator());
        } catch (NotCorrectFileException e1) {
            view.getLogText().append(Color.RED, "Некорректный синтаксис в файле! Строка: " + e1.getMessage() + System.lineSeparator());
        } catch (Exception e2) {
            view.getLogText().append(Color.RED, "Ошибка при загрузке файла!" + System.lineSeparator());
        }
    }
}
