package utmcheck.listeners;

import utmcheck.view.View;

import javax.mail.MessagingException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SendLettersListener implements ActionListener {
    private View view;

    public SendLettersListener(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //flag for successful sending
            boolean goodSend = view.getController().sendProblemShops();
            if (goodSend)
                view.getLogText().append(new Color(0, 100, 0), "Письма успешно отправлены." + System.lineSeparator());
            else
                view.getLogText().append(Color.RED, "Нет данных для отправки!" + System.lineSeparator());
        } catch (MessagingException | IOException | CloneNotSupportedException e1) {
            view.getLogText().append(Color.RED, "Проблема при отправке!" + System.lineSeparator());
            e1.printStackTrace();
        }
    }
}
