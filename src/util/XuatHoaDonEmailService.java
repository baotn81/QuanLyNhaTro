package util;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import model.PhongTro;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class XuatHoaDonEmailService {

    // Dùng chung cấu hình email với EmailSender (tài khoản Gmail đã cấu hình App Password)
    private static final String HOST_EMAIL = "trinhngocbao1508@gmail.com";
    private static final String APP_PASSWORD = "qngs fsxg syus xulr";

    private static final String TEN_CHU_TRO = "TRỊNH NGỌC BẢO";
    private static final String SDT_CHU_TRO = "0335.122.471";
    private static final String CCCD_CHU_TRO = "040092001234";
    private static final String DIA_CHI_TRO = "123 Khu Phố Quản Lý, Hệ Thống Nhà Trọ";

    public static File createInvoicePDF(PhongTro phong, double soDienMoi, double soDienCu, double soNuocMoi, double soNuocCu, double tongTien) {
        try {
            DecimalFormat df = new DecimalFormat("#,###");
            String ngayLap = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            String thangNam = new SimpleDateFormat("MM/yyyy").format(new Date());

            double tieuThuDien = (soDienMoi >= soDienCu) ? (soDienMoi - soDienCu) : 0;
            double tieuThuNuoc = (soNuocMoi >= soNuocCu) ? (soNuocMoi - soNuocCu) : 0;

            double donGiaDien = 3500;
            double donGiaNuoc = 18000;

            double thanhTienDien = tieuThuDien * donGiaDien;
            double thanhTienNuoc = tieuThuNuoc * donGiaNuoc;
            double tienPhong = tongTien - thanhTienDien - thanhTienNuoc;

            File pdfFile = File.createTempFile("HoaDon_" + phong.getMaPhong() + "_", ".pdf");
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            String htmlContent = "<html>"
                    + "<head>"
                    + "<style>"
                    + "body { font-family: 'Times New Roman', Times, serif; color: #333; line-height: 1.4; }"
                    + ".header { text-align: center; margin-bottom: 20px; }"
                    + ".title { font-size: 22px; font-weight: bold; color: #2c3e50; text-transform: uppercase; }"
                    + ".subtitle { font-size: 13px; font-style: italic; color: #555; margin-top: 5px; }"
                    + ".section-title { font-size: 14px; font-weight: bold; color: #2c3e50; border-bottom: 1px solid #ddd; padding-bottom: 5px; margin-top: 15px; margin-bottom: 10px; text-transform: uppercase; }"
                    + "table { width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 13px; }"
                    + "table.info-table td { border: none; padding: 4px 0; vertical-align: top; }"
                    + "table.data-table th { background-color: #f2f2f2; border: 1px solid #ddd; padding: 8px; text-align: center; font-weight: bold; }"
                    + "table.data-table td { border: 1px solid #ddd; padding: 8px; }"
                    + ".text-right { text-align: right; }"
                    + ".text-center { text-align: center; }"
                    + ".total-row { font-weight: bold; background-color: #eaeded; font-size: 14px; }"
                    + ".footer-sign { margin-top: 30px; width: 100%; }"
                    + ".footer-sign td { text-align: center; font-size: 13px; width: 50%; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "  <div class='header'>"
                    + "    <div class='title'>HÓA ĐƠN THANH TOÁN TIỀN PHÒNG</div>"
                    + "    <div class='subtitle'>Kỳ thanh toán: Tháng " + thangNam + "</div>"
                    + "    <div class='subtitle'>Ngày lập hóa đơn: " + ngayLap + "</div>"
                    + "  </div>"
                    + "  <table class='info-table'>"
                    + "    <tr>"
                    + "      <td width='50%'>"
                    + "        <div class='section-title'>1. ĐƠN VỊ CHO THUÊ (CHỦ TRỌ)</div>"
                    + "        <b>Họ tên:</b> " + TEN_CHU_TRO + "<br/>"
                    + "        <b>Điện thoại:</b> " + SDT_CHU_TRO + "<br/>"
                    + "        <b>Số CCCD:</b> " + CCCD_CHU_TRO + "<br/>"
                    + "        <b>Địa chỉ khu trọ:</b> " + DIA_CHI_TRO
                    + "      </td>"
                    + "      <td width='50%' style='padding-left: 20px;'>"
                    + "        <div class='section-title'>2. KHÁCH HÀNG THUÊ PHÒNG</div>"
                    + "        <b>Họ tên khách:</b> " + phong.getTenKhachThue() + "<br/>"
                    + "        <b>Mã phòng:</b> " + phong.getTenPhong() + "<br/>"
                    + "        <b>Địa chỉ phòng:</b> " + phong.getDiaChi()
                    + "      </td>"
                    + "    </tr>"
                    + "  </table>"
                    + "  <div class='section-title'>3. CHI TIẾT CHỈ SỐ CÔNG TƠ & TIÊU THỤ</div>"
                    + "  <table class='data-table'>"
                    + "    <thead>"
                    + "      <tr>"
                    + "        <th>Loại dịch vụ</th>"
                    + "        <th>Chỉ số cũ</th>"
                    + "        <th>Chỉ số mới</th>"
                    + "        <th>Sản lượng tiêu thụ</th>"
                    + "      </tr>"
                    + "    </thead>"
                    + "    <tbody>"
                    + "      <tr>"
                    + "        <td>Điện sinh hoạt (kWh)</td>"
                    + "        <td class='text-center'>" + (int)soDienCu + "</td>"
                    + "        <td class='text-center'>" + (int)soDienMoi + "</td>"
                    + "        <td class='text-center'><b>" + (int)tieuThuDien + "</b> kWh</td>"
                    + "      </tr>"
                    + "      <tr>"
                    + "        <td>Nước sinh hoạt (m³)</td>"
                    + "        <td class='text-center'>" + (int)soNuocCu + "</td>"
                    + "        <td class='text-center'>" + (int)soNuocMoi + "</td>"
                    + "        <td class='text-center'><b>" + (int)tieuThuNuoc + "</b> m³</td>"
                    + "      </tr>"
                    + "    </tbody>"
                    + "  </table>"
                    + "  <div class='section-title'>4. CHI PHÍ THÀNH TIỀN CHI TIẾT</div>"
                    + "  <table class='data-table'>"
                    + "    <thead>"
                    + "      <tr>"
                    + "        <th width='5%'>STT</th>"
                    + "        <th width='45%'>Nội dung thanh toán</th>"
                    + "        <th width='25%'>Đơn giá</th>"
                    + "        <th width='25%'>Thành tiền (VND)</th>"
                    + "      </tr>"
                    + "    </thead>"
                    + "    <tbody>"
                    + "      <tr>"
                    + "        <td class='text-center'>1</td>"
                    + "        <td>Tiền thuê phòng trọ trọn gói</td>"
                    + "        <td class='text-right'>" + df.format(tienPhong) + " đ/tháng</td>"
                    + "        <td class='text-right'>" + df.format(tienPhong) + " đ</td>"
                    + "      </tr>"
                    + "      <tr>"
                    + "        <td class='text-center'>2</td>"
                    + "        <td>Tiền điện tiêu thụ (theo công tơ)</td>"
                    + "        <td class='text-right'>" + df.format(donGiaDien) + " đ/kWh</td>"
                    + "        <td class='text-right'>" + df.format(thanhTienDien) + " đ</td>"
                    + "      </tr>"
                    + "      <tr>"
                    + "        <td class='text-center'>3</td>"
                    + "        <td>Tiền nước tiêu thụ (theo công tơ)</td>"
                    + "        <td class='text-right'>" + df.format(donGiaNuoc) + " đ/m³</td>"
                    + "        <td class='text-right'>" + df.format(thanhTienNuoc) + " đ</td>"
                    + "      </tr>"
                    + "      <tr class='total-row'>"
                    + "        <td colspan='3' class='text-right'>TỔNG CỘNG TIỀN PHẢI TRẢ:</td>"
                    + "        <td class='text-right' style='color:#c0392b; font-size:15px;'>" + df.format(tongTien) + " đ</td>"
                    + "      </tr>"
                    + "    </tbody>"
                    + "  </table>"
                    + "  <table class='footer-sign'>"
                    + "    <tr>"
                    + "      <td>"
                    + "        <b>KHÁCH THUÊ PHÒNG</b><br/>"
                    + "        <i>(Ký và ghi rõ họ tên)</i>"
                    + "      </td>"
                    + "      <td>"
                    + "        <b>ĐẠI DIỆN CHỦ TRỌ</b><br/>"
                    + "        <i>(Ký và ghi rõ họ tên)</i>"
                    + "      </td>"
                    + "    </tr>"
                    + "  </table>"
                    + "</body>"
                    + "</html>";

            ByteArrayInputStream bias = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, bias, StandardCharsets.UTF_8);

            document.close();
            return pdfFile;
        } catch (Exception e) {
            System.out.println("Lỗi khi kết xuất PDF HTML hóa đơn: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean guiHoaDonQuaEmail(String emailNhan, String tenPhong, File fileDinhKem) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(HOST_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(HOST_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailNhan));
            message.setSubject("📋 THÔNG BÁO: HÓA ĐƠN TIỀN PHÒNG CHI TIẾT - " + tenPhong.toUpperCase());

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Xin chào khách thuê,\n\nHệ thống quản lý nhà trọ xin gửi tới bạn bảng sao kê chi tiết hóa đơn tiền phòng và điện nước tháng này.\n\nChi tiết cụ thể chỉ số tiêu thụ và thành tiền vui lòng xem và tải file đính kèm dạng định dạng .pdf bên dưới để quyết toán thanh toán tiền mặt/chuyển khoản.\n\nXin cảm ơn!");

            MimeBodyPart attachPart = new MimeBodyPart();
            attachPart.attachFile(fileDinhKem);
            attachPart.setFileName("HoaDon_ThanhToan_" + tenPhong.replace(" ", "_") + ".pdf");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachPart);

            message.setContent(multipart);
            Transport.send(message);
            return true;
        } catch (Exception e) {
            System.out.println("Lỗi gửi Email SMTP: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gửi email xác nhận đã thanh toán hóa đơn thành công đến khách thuê.
     */
    public static boolean guiEmailXacNhanThanhToan(
            String emailNhan,
            String tenKhach,
            String maHD,
            String tenPhong,
            int thang,
            int nam,
            double tongTien) {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(HOST_EMAIL, APP_PASSWORD);
            }
        });

        try {
            DecimalFormat df = new DecimalFormat("#,###");
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String thoiGianThanhToan = sdf.format(new java.util.Date());

            String htmlBody =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden;'>"
                + "  <div style='background: linear-gradient(135deg, #1565C0, #0288D1); padding: 30px; text-align: center;'>"
                + "    <h1 style='color: white; margin: 0; font-size: 22px;'>✅ XÁC NHẬN THANH TOÁN THÀNH CÔNG</h1>"
                + "    <p style='color: #e3f2fd; margin: 8px 0 0 0; font-size: 14px;'>Hệ Thống Quản Lý Nhà Trọ</p>"
                + "  </div>"
                + "  <div style='padding: 30px; background: #fff;'>"
                + "    <p style='font-size: 16px; color: #333;'>Xin chào <b>" + tenKhach + "</b>,</p>"
                + "    <p style='color: #555;'>Chúng tôi xác nhận bạn đã <b style='color: #27ae60;'>thanh toán thành công</b> hóa đơn tiền phòng tháng <b>" + thang + "/" + nam + "</b>.</p>"
                + "    <div style='background: #f5f5f5; border-radius: 8px; padding: 20px; margin: 20px 0;'>"
                + "      <h3 style='color: #1565C0; margin: 0 0 15px 0; border-bottom: 2px solid #1565C0; padding-bottom: 8px;'>📋 THÔNG TIN HÓA ĐƠN</h3>"
                + "      <table style='width: 100%; border-collapse: collapse;'>"
                + "        <tr><td style='padding: 6px 0; color: #777; width: 45%;'>Mã hóa đơn:</td>"
                + "            <td style='padding: 6px 0; font-weight: bold; color: #333;'>" + maHD + "</td></tr>"
                + "        <tr><td style='padding: 6px 0; color: #777;'>Phòng:</td>"
                + "            <td style='padding: 6px 0; font-weight: bold; color: #333;'>" + tenPhong + "</td></tr>"
                + "        <tr><td style='padding: 6px 0; color: #777;'>Kỳ thanh toán:</td>"
                + "            <td style='padding: 6px 0; font-weight: bold; color: #333;'>Tháng " + thang + "/" + nam + "</td></tr>"
                + "        <tr><td style='padding: 6px 0; color: #777;'>Thời gian:</td>"
                + "            <td style='padding: 6px 0; font-weight: bold; color: #333;'>" + thoiGianThanhToan + "</td></tr>"
                + "        <tr style='border-top: 1px solid #ddd;'>"
                + "            <td style='padding: 12px 0 6px 0; color: #777; font-size: 15px;'>Số tiền đã thanh toán:</td>"
                + "            <td style='padding: 12px 0 6px 0; font-weight: bold; font-size: 18px; color: #c0392b;'>"
                + df.format(tongTien) + " VNĐ</td></tr>"
                + "      </table>"
                + "    </div>"
                + "    <div style='background: #e8f5e9; border-left: 4px solid #27ae60; padding: 15px; border-radius: 4px; margin-bottom: 20px;'>"
                + "      <p style='margin: 0; color: #2e7d32; font-size: 14px;'>✔ Hóa đơn của bạn đã được ghi nhận trong hệ thống. Cảm ơn bạn đã thanh toán đúng hạn!</p>"
                + "    </div>"
                + "    <p style='color: #888; font-size: 13px;'>Nếu có thắc mắc, vui lòng liên hệ chủ nhà trọ: <b>" + SDT_CHU_TRO + "</b></p>"
                + "  </div>"
                + "  <div style='background: #f0f4f8; padding: 15px; text-align: center;'>"
                + "    <p style='color: #aaa; font-size: 12px; margin: 0;'>© Hệ thống Quản lý Nhà Trọ — " + TEN_CHU_TRO + "</p>"
                + "  </div>"
                + "</div>";

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(HOST_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailNhan));
            message.setSubject("✅ [XÁC NHẬN THANH TOÁN] Hóa đơn " + maHD + " - Phòng " + tenPhong + " - Tháng " + thang + "/" + nam);
            message.setContent(htmlBody, "text/html;charset=UTF-8");

            Transport.send(message);
            System.out.println("Đã gửi email xác nhận thanh toán tới: " + emailNhan);
            return true;

        } catch (Exception e) {
            System.out.println("Lỗi gửi email xác nhận thanh toán: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}