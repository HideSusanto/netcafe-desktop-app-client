package dev.meoftbanana.module;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeHandle {
    public static String timeToMoney(String time, double pricePerHour) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        double decimalHours = hours + (double) minutes / 60;
        double price = pricePerHour * decimalHours;
        String priceString = String.format("%.1f", price);
        return priceString;
    }

    public static String moneyToTime(double money, double pricePerHour) {
        // Tính số giờ từ số tiền và giá mỗi giờ
        double hours = money / pricePerHour;

        // Chuyển đổi số giờ thành phần nguyên và phần thập phân
        int wholeHours = (int) hours;
        double fractionalHours = hours - wholeHours;

        // Tính số phút từ phần thập phân của số giờ
        int minutes = (int) (fractionalHours * 60);

        // Trả về chuỗi thời gian dưới dạng "HH:MM"
        return String.format("%02d:%02d", wholeHours, minutes);
    }

    public static int getHour(String remainTime) {
        // Phân tách chuỗi thành phần giờ và phút
        String[] parts = remainTime.split(":");

        // Lấy giờ từ phần đầu tiên của mảng
        return Integer.parseInt(parts[0]);
    }

    public static int getMinute(String remainTime) {
        // Phân tách chuỗi thành phần giờ và phút
        String[] parts = remainTime.split(":");

        // Lấy phút từ phần thứ hai của mảng
        return Integer.parseInt(parts[1]);
    }

    public static String convertTimeString(String inputTimeString) {
        int dotIndex = inputTimeString.indexOf('.');
        if (dotIndex != -1) {
            inputTimeString = inputTimeString.substring(0, dotIndex);
        }
        // Định dạng chuỗi thời gian đầu vào
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        // Định dạng đầu ra cho thời gian và ngày
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy");

        // Chuyển đổi từ chuỗi thành LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(inputTimeString, inputFormatter);
        // Chuyển đổi lại thành chuỗi theo định dạng mới
        String outputTimeString = dateTime.format(outputFormatter);

        return outputTimeString;
    }
}
