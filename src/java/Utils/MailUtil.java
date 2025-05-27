/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator; 
import javax.mail.PasswordAuthentication; 
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;


/**
 *
 * @author Admin
 */
public class MailUtil {

    static final String FROM = "lamnvhe181525@fpt.edu.vn";
    static final String PASSWORD = "eves uatd dhpq surd";

    public static void sendCode(String toEmail, String verificationCode) {
        Properties pros = new Properties();
        pros.put("mail.smtp.host", "smtp.gmail.com"); //using SMTP host of gmail
        pros.put("mail.smtp.port", "587"); //using TLS: port 587, if use SSL port: 465
        pros.put("mail.smtp.auth", "true"); // require authentication step
        pros.put("mail.smtp.starttls.enable", "true"); // encode with tls

        // method for logging in email
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD); 
            }
        };
        // create a java mail session
        Session session = Session.getInstance(pros, auth);
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setFrom(FROM);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            msg.setSubject("Register verification");
            msg.setSentDate(new Date());
            msg.setText("Your verification code is: " + verificationCode);
            Transport.send(msg); //send email with the message
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        sendCode("vietlam2k4@gmail.com", "203817");
    }

}
