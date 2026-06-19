package util;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.DecimalFormat;
import java.util.Properties;

public class EmailSender {

    private static final String EMAIL_FROM =
            "trinhngocbao1508@gmail.com";

    private static final String APP_PASSWORD =
            "qngs fsxg syus xulr";


    public static boolean sendMail(String toEmail,
                                   String subject,
                                   String htmlContent) {

        try {

            Properties props = new Properties();

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            Session session = Session.getInstance(props,

                    new Authenticator() {

                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {

                            return new PasswordAuthentication(
                                    EMAIL_FROM,
                                    APP_PASSWORD
                            );

                        }

                    });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(EMAIL_FROM));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            message.setSubject(subject);

            message.setContent(
                    htmlContent,
                    "text/html;charset=UTF-8");

            Transport.send(message);

            System.out.println("================================");
            System.out.println("Đã gửi Email thành công");
            System.out.println("Đến : " + toEmail);
            System.out.println("================================");

            return true;

        } catch (Exception ex) {

            ex.printStackTrace();

            return false;

        }

    }

    public static boolean sendOTP(String email,
                                  String otp) {

        String html =

                "<div style='font-family:Arial'>"

                        + "<h2 style='color:#2196F3'>QUÊN MẬT KHẨU</h2>"

                        + "<p>Mã OTP của bạn là:</p>"

                        + "<h1 style='color:red'>"
                        + otp
                        + "</h1>"

                        + "<p>OTP có hiệu lực trong vài phút.</p>"

                        + "<hr>"

                        + "<b>Quản lý Nhà Trọ</b>"

                        + "</div>";

        return sendMail(

                email,

                "Mã OTP Quên Mật Khẩu",

                html

        );

    }

    public static boolean sendHoaDon(

            String email,

            String tenKhach,

            String phong,

            int thang,

            int nam,

            double tienPhong,

            double tienDien,

            double tienNuoc,

            double tongTien

    ) {

        DecimalFormat df = new DecimalFormat("#,###");

        String html =

                "<div style='font-family:Arial;'>"

                        + "<h2 style='color:#1565C0'>"

                        + "HÓA ĐƠN THANH TOÁN"

                        + "</h2>"

                        + "<hr>"

                        + "<p>Xin chào <b>"

                        + tenKhach

                        + "</b></p>"

                        + "<p>Thông tin hóa đơn tháng "

                        + thang

                        + "/"

                        + nam

                        + "</p>"

                        + "<table border='1' cellspacing='0' cellpadding='8'>"

                        + "<tr>"

                        + "<td>Phòng</td>"

                        + "<td>"

                        + phong

                        + "</td>"

                        + "</tr>"

                        + "<tr>"

                        + "<td>Tiền phòng</td>"

                        + "<td>"

                        + df.format(tienPhong)

                        + " VNĐ</td>"

                        + "</tr>"

                        + "<tr>"

                        + "<td>Tiền điện</td>"

                        + "<td>"

                        + df.format(tienDien)

                        + " VNĐ</td>"

                        + "</tr>"

                        + "<tr>"

                        + "<td>Tiền nước</td>"

                        + "<td>"

                        + df.format(tienNuoc)

                        + " VNĐ</td>"

                        + "</tr>"

                        + "<tr style='background:#FFF59D;'>"

                        + "<td><b>TỔNG TIỀN</b></td>"

                        + "<td><b style='color:red'>"

                        + df.format(tongTien)

                        + " VNĐ</b></td>"

                        + "</tr>"

                        + "</table>"

                        + "<br>"

                        + "<p>Vui lòng thanh toán đúng hạn.</p>"

                        + "<br>"

                        + "<b>Xin cảm ơn!</b>"

                        + "<br>"

                        + "<b>Hệ thống Quản lý Nhà Trọ</b>"

                        + "</div>";

        return sendMail(

                email,

                "HÓA ĐƠN THANH TOÁN TIỀN PHÒNG",

                html

        );

    }

    public static boolean sendThongBao(

            String email,

            String title,

            String noiDung

    ) {

        String html =

                "<div style='font-family:Arial'>"

                        + "<h2>"

                        + title

                        + "</h2>"

                        + "<hr>"

                        + "<p>"

                        + noiDung

                        + "</p>"

                        + "<br>"

                        + "<b>Quản lý Nhà Trọ</b>"

                        + "</div>";

        return sendMail(

                email,

                title,

                html

        );

    }

    public static void main(String[] args) {

        sendMail(

                "trinhngocbao1508@gmail.com",

                "TEST EMAIL",

                "<h2>Xin chào!</h2>"
                        + "<h3>Email gửi thành công.</h3>"
                        + "<b>Quản lý Nhà Trọ</b>"

        );

    }

}