package utmcheck.utils;

import utmcheck.enums.Status;
import utmcheck.model.Shop;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public final class SendEmailUtil {
    private SendEmailUtil() {
    }

    public static void sendEmail(String textToSend) throws MessagingException{

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.rambler.ru");
        props.put("mail.smtp.socketFactory.port", 465);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");

        /*SSL

                («mail.smtp.host», «smtp.gmail.com»);
        («mail.smtp.socketFactory.port», «465»);
        («mail.smtp.socketFactory.class», «javax.net.ssl.SSLSocketFactory»);
        («mail.smtp.auth», «true»)
        («mail.smtp.port», «465»)

        TLS

                («mail.smtp.auth», «true»)
        («mail.smtp.starttls.enable», «true»)
        («mail.smtp.host», «smtp.gmail.com»)
        («mail.smtp.port», «587»)*/



        //логин и пароль gmail пользователя
        final String userLogin = "li-ionking@rambler.ru";
        final String userPassword = "Rem@in4life";
        Authenticator  auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userLogin, userPassword);
            }
        };

        Session session = Session.getDefaultInstance(props, auth);



        //our session message
        MimeMessage message = new MimeMessage(session);

        //message subject
        message.setSubject("Уведомление о недоступности УТМ!");

        //adding text to message
        message.setText(textToSend);

        //adding recipient and sending date
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("remain4life@gmail.com"));
        message.setSentDate(new Date());
        Transport.send(message);

    }

    public static String resultMapToText(Map<Shop, Status> resultMap) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Shop, Status> shopEntry: resultMap.entrySet()) {
            sb.append(shopEntry.getKey().getName()).append(", ");
            sb.append(shopEntry.getKey().getIP()).append(", ");

            switch (shopEntry.getValue()) {
                case NO_HOST_CONNECT:
                    sb.append(" нет связи с компьютером!");
                    break;
                case UTM_WRONG_STATUS:
                case NO_UTM_CONNECT:
                    sb.append(" связь с компьюетром есть, нет связи с УТМ!");
                    break;

            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}
