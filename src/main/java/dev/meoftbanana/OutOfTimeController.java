package dev.meoftbanana;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import dev.meoftbanana.logindata.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class OutOfTimeController {
    @FXML
    private Button buttonLogout;

    private static final String API_URL_LOGOUT = "http://localhost:8080/api/auth/logout";

    public void logout() throws IOException {
        // Tạo URL object từ URL string
        URL apiUrl = new URL(API_URL_LOGOUT + "/" + User.userId);

        // Mở kết nối HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

        // Đặt phương thức request là DELETE
        connection.setRequestMethod("DELETE");

        // Đọc response từ server
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // In ra response từ server
        System.out.println("Response: " + response.toString());
        // Đóng kết nối
        connection.disconnect();
    }
    @FXML
    private void logoutClick(ActionEvent event) throws IOException {
        logout();
        
        User.userName = "";
        User.balance = null;
        User.userId = null;
        Parent root = App.loadFXML("client_login_page");
        DashBoardController.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DashBoardController.scene = new Scene(root);
        DashBoardController.stage.setScene(DashBoardController.scene);
        // Lấy kích thước của màn hình
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Tính toán vị trí để đặt cửa sổ ở giữa màn hình
        double windowWidth = DashBoardController.stage.getWidth();
        double windowHeight = DashBoardController.stage.getHeight();
        double x = (screenWidth - windowWidth) / 2;
        double y = (screenHeight - windowHeight) / 2;
        //HistoryGridPane.getChildren().clear();
        //orders.clear();

        // Đặt vị trí của cửa sổ
        DashBoardController.stage.setX(x);
        DashBoardController.stage.setY(y);
        DashBoardController.stage.show();
    }
}
