package controllers;



import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by abow on 16/1/28.
 */
public class sendMail {


    String host = "smtp.gmail.com";
    int port = 587;
    final String username = "fastlab.tw@gmail.com";
    final String password = "chibkhrbgjyzdqbc";//your password
    Session session;

    public void sendMail()
    {


    }

    public void sendResetPasswordRequest(String toEmail,String strToken) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.port", port);

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@fastlab.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Reset password");
            message.setContent("<a href='http://fastlab.cc/Fast-Lab/reset-password?email=" + toEmail + "&token=" + strToken + "'>Press the link to reset password</a>", "text/html; charset=utf-8");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, port, username, password);

            Transport.send(message);

            System.out.println("寄送email結束.");
        } catch (Exception e) {
            throw e;
        }
    }
}
