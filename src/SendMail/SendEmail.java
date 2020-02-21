/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SendMail;

import interfaces.CryptoKeys;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import validation.Crypto;

/**
 *
 * @author wests
 */
public class SendEmail {

    static boolean sent = false;

    public boolean sendFromGMail(String to, String subject, String body, String Attachement) {

        String from = CryptoKeys.SENDER_MAIL;
        String pass = Crypto.decrypt(CryptoKeys.SENDER_PASS, CryptoKeys.SECRET_KEY);

        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            try {
                message.setFrom(new InternetAddress(from));
            } catch (MessagingException ex) {
                Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE, null, ex);
            }

            Multipart emailContent = new MimeMultipart();

            //text body part
            MimeBodyPart texBodyPart = new MimeBodyPart();
            texBodyPart.setText(body);

            //attachment body part
            MimeBodyPart pdfAttachment = new MimeBodyPart();
            if (!Attachement.equals("")) {
                File pdf = new File(Attachement);
                if (pdf.exists()) {
                    try {
                        pdfAttachment.attachFile(pdf);
                    } catch (IOException ex) {
                        Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            //attach body parts
            emailContent.addBodyPart(texBodyPart);
            emailContent.addBodyPart(pdfAttachment);

            //attach multipart
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject(subject);
            message.setContent(emailContent);

            try (Transport transport = session.getTransport("smtp")) {
                transport.connect(host, from, pass);
                System.out.println("Sending mail to " + to);
                transport.sendMessage(message, message.getAllRecipients());
                return true;
            }

        } catch (AddressException ae) {
        } catch (MessagingException me) {
        }
        return false;
    }

    public boolean logError(String to, String subject, String body) {

        String from = CryptoKeys.SENDER_MAIL;
        String pass = Crypto.decrypt(CryptoKeys.SENDER_PASS, CryptoKeys.SECRET_KEY);

        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            try {
                message.setFrom(new InternetAddress(from));
            } catch (MessagingException ex) {
                Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE, null, ex);
            }

            Multipart emailContent = new MimeMultipart();

            //text body part
            MimeBodyPart texBodyPart = new MimeBodyPart();
            texBodyPart.setText(body);

            //attach body parts
            emailContent.addBodyPart(texBodyPart);

            //attach multipart
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject(subject);
            message.setContent(emailContent);

            try (Transport transport = session.getTransport("smtp")) {
                transport.connect(host, from, pass);
                System.out.println("Sending mail to " + to);
                transport.sendMessage(message, message.getAllRecipients());
                return true;
            }

        } catch (AddressException ae) {
        } catch (MessagingException me) {
        }
        return false;
    }
}
