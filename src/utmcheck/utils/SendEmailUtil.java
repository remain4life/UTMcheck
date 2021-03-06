package utmcheck.utils;

import utmcheck.enums.Region;
import utmcheck.enums.Status;
import utmcheck.model.Shop;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public final class SendEmailUtil {
    private SendEmailUtil() {
    }

    public static void sendEmail(String textToSend, Region region) throws MessagingException, IOException {

        InternetAddress to = getIAfromRegion(region);

        //creating and loading properties
        Properties props = new Properties();
        InputStream is = SendEmailUtil.class.getResourceAsStream("email.properties");
        props.load(is);

        //getting information from properties
        String from = props.getProperty("mail.smtp.username");
        final String userLogin = props.getProperty("mail.smtp.username");
        final String userPass = props.getProperty("mail.smtp.password");

        //authorization
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            //adding login/password from email we'll send message from
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userLogin, userPass);
            }
        });

        //creating message object from session
        Message msg = new MimeMessage(session);

        //message attributes
        msg.setFrom(new InternetAddress(from));
        InternetAddress[] address = {to};
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject("Уведомление о недоступности УТМ");
        msg.setSentDate(new Date());

        //message body
        msg.setText(textToSend);

        //sending email
        Transport.send(msg);
    }

    public static String resultMapToText(Map<Shop, Status> resultMap) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Shop, Status> shopEntry : resultMap.entrySet()) {
            sb.append(shopEntry.getKey().getName()).append(", ");
            sb.append(shopEntry.getKey().getIP()).append(", ");

            switch (shopEntry.getValue()) {
                case NO_HOST_CONNECT:
                    sb.append("нет связи с компьютером!");
                    break;
                case UTM_WRONG_STATUS:
                case NO_UTM_CONNECT:
                    sb.append(" связь с компьютером есть, нет связи с УТМ!");
                    break;
                default:
                    sb.append(" всё ОК!");
                    break;
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            sendEmail("Message3, test", Region.ALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static InternetAddress getIAfromRegion(Region region) throws AddressException, IOException {
        //creating and loading properties with emails according region
        Properties props = new Properties();
        InputStream is = SendEmailUtil.class.getResourceAsStream("mail_list.properties");
        props.load(is);

        switch (region) {
            case BELOGORSK:
            case BAKHCHISARAI:
            case SIMFEROPOL:
                return new InternetAddress(props.getProperty("SIMFEROPOL"));
            case SEVASTOPOL:
                return new InternetAddress(props.getProperty("SEVASTOPOL"));
            case SAKI:
            case YEVPATORIA:
                return new InternetAddress(props.getProperty("YEVPATORIA"));
            case KRASNOGVARDEYSK:
            case NIZHNEGORSK:
            case JANKOI:
                return new InternetAddress(props.getProperty("JANKOI"));
            case YALTA:
            case ALUSHTA:
                return new InternetAddress(props.getProperty("ALUSHTA"));
            case ARMYANSK:
            case KRASNOPEREKOPSK:
                return new InternetAddress(props.getProperty("KRASNOPEREKOPSK"));
            case SUDAK:
            case FEODOSIYA:
                return new InternetAddress(props.getProperty("FEODOSIYA"));
            case KERCH:
                return new InternetAddress(props.getProperty("KERCH"));
            default:
                return new InternetAddress(props.getProperty("OTHER"));
        }
    }
}
