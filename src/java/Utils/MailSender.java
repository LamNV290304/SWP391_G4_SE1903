/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.util.Date;
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
 *
 * @author duckh
 */
public class MailSender {

    private static final String FROM = "he186395thanduckhoi@gmail.com";
    private static final String PASSWORD = "heaw neck apgn dack";

    public void sendInvoiceMail(String recipientEmail, String subject, String content) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // tao 1 Authenicator
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD);
            }
        };
        Session session = Session.getInstance(props, auth);

        //tao 1 tin nhan
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.addHeader("Content-type", "text/html; charset=UTF-8");
            msg.setFrom(new InternetAddress(FROM));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());

            msg.setContent(content, "text/html; charset=UTF-8");

            Transport.send(msg);
            System.out.println("Email sent successfully to: " + recipientEmail);

        } catch (MessagingException ex) {
            System.err.println("Error sending email: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    public static void main(String[] args) {
        MailSender sender = new MailSender();
        String testRecipient = "khoi123a@gmail.com"; // THAY BẰNG EMAIL CỦA BẠN ĐỂ NHẬN THỬ
        String testSubject = "";
        String testHtmlContent = "<html>"
                + "<body>"
                + "<h1>Kinh gui Quy khach,</h1>"
                + "<p>Day la hoa don thu nghiem tu he thong cua ban.</p>"
                + "<table border='1' style='width:100%; border-collapse: collapse;'>"
                + "<thead><tr><th>San Pham</th><th>So Luong</th><th>Don Gia</th></tr></thead>"
                + "<tbody>"
                + "<tr><td>Ao Thun</td><td>2</td><td>150,000 VNĐ</td></tr>"
                + "<tr><td>Quan Jean</td><td>1</td><td>400,000 VNĐ</td></tr>"
                + "</tbody>"
                + "<tfoot><tr><td colspan='2' style='text-align:right;'>Tong Cong:</td><td>700,000 VNĐ</td></tr></tfoot>"
                + "</table>"
                + "<p>Xin cam on!</p>"
                + "</body>"
                + "</html>";

        try {
            sender.sendInvoiceMail(testRecipient, testSubject, testHtmlContent);
            System.out.println("Test email sent successfully!");
        } catch (MessagingException e) {
            System.err.println("Failed to send test email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
