package util;

import java.util.Random;

public class OTPService {

    public String generateOTP() {

        Random random = new Random();

        int otp = 100000 + random.nextInt(900000);

        return String.valueOf(otp);

    }

    public boolean sendOTP(String email, String otp) {

        String subject = "🔐 MÃ OTP KHÔI PHỤC MẬT KHẨU";

        String html =

                "<div style='font-family:Arial;'>"

                        + "<h2 style='color:#2980b9;'>HỆ THỐNG QUẢN LÝ NHÀ TRỌ</h2>"

                        + "<hr>"

                        + "<p>Xin chào!</p>"

                        + "<p>Bạn vừa yêu cầu đặt lại mật khẩu.</p>"

                        + "<p>Mã OTP của bạn là:</p>"

                        + "<div style='font-size:40px;"
                        + "font-weight:bold;"
                        + "color:red;"
                        + "letter-spacing:5px;'>"

                        + otp

                        + "</div>"

                        + "<br>"

                        + "<p><b>OTP chỉ có hiệu lực trong 5 phút.</b></p>"

                        + "<p>Không chia sẻ mã này với bất kỳ ai.</p>"

                        + "<br>"

                        + "<p>Trân trọng!</p>"

                        + "<b>Quản lý nhà trọ</b>"

                        + "</div>";

        return EmailSender.sendMail(

                email,

                subject,

                html

        );

    }

    public boolean verifyOTP(String inputOTP,
                             String correctOTP) {

        if (inputOTP == null || correctOTP == null)

            return false;

        return inputOTP.trim().equals(correctOTP.trim());

    }

}