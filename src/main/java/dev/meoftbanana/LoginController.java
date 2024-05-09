package dev.meoftbanana;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import dev.meoftbanana.logindata.Computer;
import dev.meoftbanana.logindata.User;
import dev.meoftbanana.module.TimeHandle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordTextInput;

    @FXML
    private TextField userNameTextInput;

    @FXML
    private Text notificationText;

    @FXML
    private void switchToDashBoard(ActionEvent event) throws IOException {
        if(login()){
            if(User.balance > 0) {
                
                notificationText.setText("Đăng nhập thành công");
                root = App.loadFXML("client_dashboard");
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                // Lấy kích thước của màn hình
                Rectangle2D screenBounds = Screen.getPrimary().getBounds();
                double screenWidth = screenBounds.getWidth();
                double screenHeight = screenBounds.getHeight();

                // Tính toán vị trí để đặt cửa sổ ở giữa màn hình
                double windowWidth = stage.getWidth();
                double windowHeight = stage.getHeight();
                double x = (screenWidth - windowWidth) / 2;
                double y = (screenHeight - windowHeight) / 2;

                // Đặt vị trí của cửa sổ
                stage.setX(x);
                stage.setY(y);
                stage.show();
            } else {
                System.out.println(User.balance);
                notificationText.setText("Tài khoản quý khách không đủ");
            }
        }
        else {
            notificationText.setText("Tài khoản hoặc mật khẩu sai!");
        }
        
    }

    public boolean login() {
        try {
            String username = userNameTextInput.getText();
            String password = passwordTextInput.getText();

            // Tạo URL và kết nối
            URL url = new URL("http://localhost:8080/api/auth/loginwithuserinfo/1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Cấu hình yêu cầu
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Tạo body request
            String requestBody = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

            // Gửi yêu cầu
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(requestBody);
            out.flush();
            out.close();
            
            // Đọc phản hồi
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Phản hồi 200: Đăng nhập thành công
                // Đọc dữ liệu JSON từ phản hồi
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Parse JSON
                JSONObject jsonResponse;
                try {
                    jsonResponse = new JSONObject(response.toString());
                    // Lấy các trường từ JSON
                    Long userId = jsonResponse.getLong("userId");
                    double balance = jsonResponse.getDouble("balance");
                    String userName = jsonResponse.getString("userName");
                    double pricePerHour = jsonResponse.getDouble("pricePerHour");
                    User.userId=userId;
                    User.balance=balance;
                    User.userName=userName;
                    Computer.pricePerHour=pricePerHour;

                    
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
                
            } else {
                
                // Đăng nhập không thành công
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
