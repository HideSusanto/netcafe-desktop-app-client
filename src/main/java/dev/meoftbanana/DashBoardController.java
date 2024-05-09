package dev.meoftbanana;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import dev.meoftbanana.logindata.Computer;
import dev.meoftbanana.logindata.User;
import dev.meoftbanana.model.Order;
import dev.meoftbanana.model.OrderItem;
import dev.meoftbanana.model.Product;
import dev.meoftbanana.model.ProductCategory;
import dev.meoftbanana.module.TimeHandle;
import io.github.palexdev.virtualizedfx.controls.behavior.actions.EventAction;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DashBoardController implements Initializable {
    private static final String API_URL = "http://localhost:8080/api/products/category";
    private static final String API_URL_ORDER = "http://localhost:8080/api/orders";
    private static final String API_URL_USERORDER = "http://localhost:8080/api/orders/user";
    private static final String API_URL_ORDERITEM = "http://localhost:8080/api/orderitems";
    private static final String API_URL_LOGOUT = "http://localhost:8080/api/auth/logout";

    private ObservableList<Product> foods = FXCollections.observableArrayList();
    private ObservableList<Product> beverages = FXCollections.observableArrayList();
    private ObservableList<Product> chargings = FXCollections.observableArrayList();
    private ObservableList<Product> others = FXCollections.observableArrayList();
    public static ObservableList<OrderItem> cart = FXCollections.observableArrayList();
    public static ObservableList<Order> orders = FXCollections.observableArrayList();
    static BooleanProperty needToLoad = new SimpleBooleanProperty(false);
    Timeline timeUsed = new Timeline(
            new KeyFrame(Duration.seconds(60), event -> updateTimeUsed()));

    Timeline timeUpdateHistory = new Timeline(
            new KeyFrame(Duration.seconds(20), event -> updateTimeHistory()));
    Timeline timeRemains=new Timeline(new KeyFrame(Duration.seconds(60),event->{try{updateTimeRemains();}catch(
    IOException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }}));

    Timeline timeNotiAppear = new Timeline(
            new KeyFrame(Duration.seconds(10), event -> updateTimeNotiAppear()));
    int hoursUsed = 0;
    int minutesUsed = 0;
    int hoursRemains;
    int minutesRemains;
    public static Stage stage;
    public static Scene scene;
    public static Parent root;

    @FXML
    private Pane rootPane;
    @FXML
    private Button FoodsNavigationButton;
    @FXML
    private Button BeveragesNavigationButton;
    @FXML
    private Button ChargingNavigationButton;
    @FXML
    private Button OthersNavigationButton;
    @FXML
    private Button CartNavigationButton;
    @FXML
    private Button HistoryNavigationButton;
    @FXML
    private Button ChatNavigationButton;
    @FXML
    private Button orderButton;
    @FXML
    private ScrollPane FoodScrollPane;
    @FXML
    private ScrollPane BeverageScrollPane;
    @FXML
    private ScrollPane ChargingScrollPane;
    @FXML
    private ScrollPane OthersScrollPane;
    @FXML
    private ScrollPane HistoryScrollPane;
    @FXML
    private GridPane FoodGridPane;
    @FXML
    private GridPane BeverageGridPane;
    @FXML
    private GridPane ChargingGridPane;
    @FXML
    private GridPane OthersGridPane;
    @FXML
    private GridPane CartGridPane;
    @FXML
    private GridPane HistoryGridPane;
    @FXML
    private Pane CartPane;
    @FXML
    private Pane HistoryPane;
    @FXML
    private Pane ChatPane;

    @FXML
    private Label discount;

    @FXML
    private Label finalTotalPriceText;

    @FXML
    private Label totalPriceText;

    @FXML
    private Label totalProductText;

    @FXML
    private Label usedText;

    @FXML
    private Label remainsText;

    @FXML
    private Label usedPriceText;

    @FXML
    private Label remainsPriceText;

    @FXML
    private Label checkinTime;

    @FXML
    private Text usernameText;

    @FXML
    private Text ordernotiText;

    @FXML
    private void clickShow(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // orderButton.setDisable(true);
        ordernotiText.setFill(Color.web("#FF5757"));
        usernameText.setText(User.userName);
        LocalTime currentTime = LocalTime.now();
        // Định dạng thời gian thành "hh:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = currentTime.format(formatter);
        checkinTime.setText(formattedTime);
        if (User.userName != "") {
            hoursRemains = TimeHandle.getHour(TimeHandle.moneyToTime(User.balance, Computer.pricePerHour));
            minutesRemains = TimeHandle.getMinute(TimeHandle.moneyToTime(User.balance, Computer.pricePerHour));
        }
        usedPriceText.setText(TimeHandle.timeToMoney(usedText.getText(), Computer.pricePerHour) + "VND");
        remainsText.setText(TimeHandle.moneyToTime(User.balance, Computer.pricePerHour));
        remainsPriceText.setText(TimeHandle.timeToMoney(remainsText.getText(), Computer.pricePerHour) + "VND");
        displayProducts(foods, 1, FoodGridPane);
        displayProducts(beverages, 2, BeverageGridPane);
        displayProducts(chargings, 3, ChargingGridPane);
        displayProducts(others, 4, OthersGridPane);
        displayOrders();
        // displayFoods();
        // displayBeverages();
        cart.addListener((ListChangeListener<OrderItem>) change -> {
            // Gọi displayOrderItems() mỗi khi cart thay đổi
            CartGridPane.getChildren().clear();
            displayOrderItems();
            loadTotals();

        });
        //orders.addListener((ListChangeListener<Order>) change -> {
            // Gọi displayOrderItems() mỗi khi cart thay đổi
            //HistoryGridPane.getChildren().clear();
            //displayOrders();

        //});
        needToLoad.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                CartGridPane.getChildren().clear();
                displayOrderItems();
                loadTotals();
            }
        });
        timeUsed.setCycleCount(Animation.INDEFINITE);
        timeUsed.play();
        timeRemains.setCycleCount(Animation.INDEFINITE);
        timeRemains.play();
        timeUpdateHistory.setCycleCount(Animation.INDEFINITE);
        timeUpdateHistory.play();

        // TODO Auto-generated method stub
        showSelectedPane(CartPane);
        showSelectedScrollPane(FoodScrollPane);

    }

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
    private void loadOutOfTimeModal() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("outoftime.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void logoutClick(ActionEvent event) throws IOException {
        logout();
        
        User.userName = "";
        User.balance = null;
        User.userId = null;
        Parent root = App.loadFXML("client_login_page");
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
        CartGridPane.getChildren().clear();
        cart.clear();
        //HistoryGridPane.getChildren().clear();
        //orders.clear();

        // Đặt vị trí của cửa sổ
        stage.setX(x);
        stage.setY(y);
        stage.show();
    }

    public void deleteOrderItems() {
        Iterator<OrderItem> iterator = cart.iterator();
        while (iterator.hasNext()) {
            OrderItem item = iterator.next();
            if (item.getQuantity() < 1) {
                iterator.remove(); // Loại bỏ OrderItem không hợp lệ
            }
        }
    }

    @FXML
    public void updateButtonState(Button selectedButton) {
        // Reset màu và văn bản của tất cả các nút
        FoodsNavigationButton
                .setStyle("-fx-background-color: #000000; -fx-text-fill: #FCFCFC;-fx-background-radius: 20 0 0 0");
        BeveragesNavigationButton.setStyle("-fx-background-color: #000000; -fx-text-fill: #FCFCFC;");
        ChargingNavigationButton.setStyle("-fx-background-color: #000000; -fx-text-fill: #FCFCFC; ");
        OthersNavigationButton.setStyle("-fx-background-color: #000000; -fx-text-fill: #FCFCFC; ");
        // Cập nhật màu và văn bản của nút được chọn
        selectedButton.setStyle("-fx-background-color: #FCFCFC; -fx-text-fill: #000000;");
    }

    private void showSelectedScrollPane(ScrollPane selectedScrollPane) {
        // Ẩn tất cả các ScrollPane
        FoodScrollPane.setVisible(false);
        BeverageScrollPane.setVisible(false);
        ChargingScrollPane.setVisible(false);
        OthersScrollPane.setVisible(false);

        // Hiển thị ScrollPane tương ứng với nút được nhấn
        selectedScrollPane.setVisible(true);
    }

    private void showSelectedPane(Pane selectedPane) {

        ChatPane.setVisible(false);
        CartPane.setVisible(false);
        HistoryPane.setVisible(false);
        selectedPane.setVisible(true);
    }

    @FXML
    private void updateCartButtonState(Button selectedButton) {
        // Reset màu và văn bản của tất cả các nút
        CartNavigationButton
                .setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;-fx-background-radius: 20 20 0 0");
        HistoryNavigationButton
                .setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;-fx-background-radius: 20 20 0 0");
        ChatNavigationButton
                .setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;-fx-background-radius: 20 20 0 0");
        // Cập nhật màu và văn bản của nút được chọn
        selectedButton.setStyle("-fx-background-color:  #FF5757; -fx-text-fill: #FFFFFF;");
    }

    @FXML
    private void handleFoodsNavigationButtonClick(ActionEvent event) {
        // displayFoods();

        // Đặt GridPane vào một ScrollPane
        showSelectedScrollPane(FoodScrollPane);
        updateButtonState(FoodsNavigationButton);
        FoodsNavigationButton
                .setStyle("-fx-background-color: #FCFCFC; -fx-text-fill: #000000; -fx-background-radius: 20 0 0 0");
        // Thêm mã xử lý cho nút thức ăn ở đây
    }

    @FXML
    private void handleBeveragesNavigationButtonClick(ActionEvent event) {
        // displayBeverages();
        updateButtonState(BeveragesNavigationButton);
        showSelectedScrollPane(BeverageScrollPane);
        // Thêm mã xử lý cho nút đồ uống ở đây
    }

    @FXML
    private void handleChargingNavigationButtonClick(ActionEvent event) {
        updateButtonState(ChargingNavigationButton);
        showSelectedScrollPane(ChargingScrollPane);
        // Thêm mã xử lý cho nút sạc ở đây
    }

    @FXML
    private void handleOthersNavigationButtonClick(ActionEvent event) {
        updateButtonState(OthersNavigationButton);
        showSelectedScrollPane(OthersScrollPane);
        // Thêm mã xử lý cho nút sạc ở đây
    }

    @FXML
    private void handleCartNavigationButtonClick(ActionEvent event) {
        // CartGridPane.getChildren().clear();
        // displayOrderItems();
        updateCartButtonState(CartNavigationButton);
        CartNavigationButton
                .setStyle("-fx-background-color:  #FF5757; -fx-text-fill: #FFFFFF; -fx-background-radius: 20 0 0 0");
        showSelectedPane(CartPane);
        // Thêm mã xử lý cho nút sạc ở đây
    }

    @FXML
    private void handleHistoryNavigationButtonClick(ActionEvent event) {
        updateCartButtonState(HistoryNavigationButton);
        HistoryNavigationButton
                .setStyle("-fx-background-color:  #FF5757; -fx-text-fill: #FFFFFF; -fx-background-radius: 0 0 0 0");
        showSelectedPane(HistoryPane);
        // Thêm mã xử lý cho nút sạc ở đây
    }

    @FXML
    private void handleChatNavigationButtonClick(ActionEvent event) {
        updateCartButtonState(ChatNavigationButton);
        ChatNavigationButton
                .setStyle("-fx-background-color:  #FF5757; -fx-text-fill: #FFFFFF; -fx-background-radius: 0 20 0 0");
        showSelectedPane(ChatPane);
        // Thêm mã xử lý cho nút sạc ở đây
    }

    private ObservableList getProductList(int productCategoryId) {
        ObservableList<Product> products = FXCollections.observableArrayList();

        try {
            URL url = new URL(API_URL + "/" + Integer.toString(productCategoryId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // JSONObject jsonResponse = new JSONObject(response.toString());
            // JSONArray productsArray = jsonResponse.getJSONArray("products");

            JSONArray productsArray = new JSONArray(response.toString());

            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject productObject = productsArray.getJSONObject(i);
                Long id = productObject.getLong("id");
                String name = productObject.getString("name");
                String description = productObject.getString("description");
                double price = productObject.getDouble("price");
                int remainQuantity = productObject.getInt("remainQuantity");
                String productImageLink = productObject.getString("productImageLink");

                JSONObject categoryObject = productObject.getJSONObject("productCategory");
                Long categoryId = categoryObject.getLong("id");
                String categoryName = categoryObject.getString("name");
                String categoryImageLink = categoryObject.getString("imageLink");

                Product product = new Product(id, name, description, price, remainQuantity, productImageLink,
                        new ProductCategory(categoryId, categoryName, categoryImageLink));
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    private ObservableList getOrderList() {
        ObservableList<Order> orders = FXCollections.observableArrayList();
        if(User.userId == null) {
            System.out.println("user is not logging in");
            return orders;
        }
        try {
            URL url = new URL(API_URL_USERORDER+ "/" + User.userId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // JSONObject jsonResponse = new JSONObject(response.toString());
            // JSONArray productsArray = jsonResponse.getJSONArray("products");
            // Chuyển đổi chuỗi JSON thành một đối tượng JSONObject
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Lấy mảng "Orders" từ đối tượng JSONObject
            JSONArray ordersArray = jsonResponse.getJSONArray("orders");

            for (int i = 0; i < ordersArray.length(); i++) {
                JSONObject orderObject = ordersArray.getJSONObject(i);
                
                // Lấy thông tin của mỗi đơn hàng từ object JSON và tạo đối tượng Order tương ứng
                int id = orderObject.getInt("id");
                String timeCreated = orderObject.getString("timeCreated");
                //System.out.println(timeCreated);
                String timeCreatedFormatted = TimeHandle.convertTimeString(timeCreated);
                String orderStatus = orderObject.getString("orderStatus");
                Double orderTotalPrice = getTotalPrice(id);
                // Tạo đối tượng Order từ dữ liệu JSON và thêm vào danh sách orders
                Order order = new Order(id, timeCreatedFormatted, orderStatus, orderTotalPrice);
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    private static double getTotalPrice(int orderId) {
        double totalPrice = 0;

        try {
            URL url = new URL(API_URL_ORDERITEM + "/" + orderId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray orderItemsArray = new JSONArray(response.toString());

            for (int i = 0; i < orderItemsArray.length(); i++) {
                JSONObject orderItemObject = orderItemsArray.getJSONObject(i);
                int quantity = orderItemObject.getInt("quantity");
                double singlePrice = orderItemObject.getDouble("singlePrice");
                totalPrice += quantity * singlePrice;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalPrice;
    }

    public void createOrder() {
        try {
            URL url = new URL(API_URL_ORDER + "/" + Long.toString(User.userId));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // Đặt tiêu đề cho yêu cầu POST
            connection.setRequestProperty("Content-Type", "application/json");

            // Gửi yêu cầu POST
            connection.setDoOutput(true);
            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            // Kiểm tra mã trạng thái phản hồi
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Order created successfully.");

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
                    Long orderId = jsonResponse.getLong("id");
                    System.out.println(orderId);
                    for (OrderItem orderItem : cart) {
                        CreateOrderItem(orderItem, orderId);
                    }
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to create order. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CreateOrderItem(OrderItem orderItem, Long orderId) {
        try {
            Long productId = orderItem.getProduct().getId();
            int quantity = orderItem.getQuantity();
            // Tạo URL và kết nối
            URL url = new URL(API_URL_ORDERITEM);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Cấu hình yêu cầu
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Tạo body request
            String requestBody = "{ \"productId\": " + productId + ", \"orderId\": " + orderId + ", \"quantity\": "
                    + quantity + " }";

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

            } else {

                // không thành công
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void displayFoods() {
        foods.clear();
        foods.addAll(getProductList(1));
        int row = 0;
        int col = 0;
        FoodGridPane.getRowConstraints().clear();
        FoodGridPane.getColumnConstraints().clear();

        for (int q = 0; q < foods.size(); q++) {
            try {
                FXMLLoader load = new FXMLLoader();
                // load.setLocation(getClass().getResource("product_card.fxml"));
                // AnchorPane pane = load.load();
                load.setLocation(getClass().getResource("product_card.fxml"));
                AnchorPane pane = load.load();
                CardProductController cardC = load.getController();

                if (foods.get(q).getRemainQuantity() == 0) {
                    cardC.setData(foods.get(q), true);
                } else {
                    cardC.setData(foods.get(q), false);
                }
                if (col == 4) {
                    col = 0;
                    row += 1;
                }
                GridPane.setMargin(pane, new Insets(13));
                FoodGridPane.add(pane, col++, row);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void displayProducts(ObservableList<Product> products, int categoryId, GridPane gridPane) {
        products.clear();
        products.addAll(getProductList(categoryId));
        int row = 0;
        int col = 0;
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        for (int q = 0; q < products.size(); q++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("product_card.fxml"));
                AnchorPane pane = load.load();
                CardProductController cardC = load.getController();
                if (products.get(q).getRemainQuantity() == 0) {
                    cardC.setData(products.get(q), true);
                } else {
                    cardC.setData(products.get(q), false);
                }
                if (col == 4) {
                    col = 0;
                    row += 1;
                }

                gridPane.add(pane, col++, row);
                GridPane.setMargin(pane, new Insets(13));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private void displayOrders() {
        orders.clear();
        orders.addAll(getOrderList());
        int row = 0;
        int col = 0;
        HistoryGridPane.getRowConstraints().clear();
        HistoryGridPane.getColumnConstraints().clear();
        for (int q = 0; q < orders.size(); q++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("history_card.fxml"));
                AnchorPane pane = load.load();
                HistoryCardController cardC = load.getController();
                cardC.setData(orders.get(q));
                if (col == 1) {
                    col = 0;
                    row += 1;
                }

                HistoryGridPane.add(pane, col++, row);
                HistoryGridPane.setMargin(pane, new Insets(9));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private void displayOrderItems() {

        int row = 0;
        int col = 0;
        CartGridPane.getRowConstraints().clear();
        CartGridPane.getColumnConstraints().clear();

        for (int q = 0; q < cart.size(); q++) {
            try {
                FXMLLoader load = new FXMLLoader();
                // load.setLocation(getClass().getResource("product_card.fxml"));
                // AnchorPane pane = load.load();
                load.setLocation(getClass().getResource("order_item_card.fxml"));
                AnchorPane pane = load.load();
                OrderItemCardController cardC = load.getController();
                if(cart.get(q).getQuantity() >=  cart.get(q).getProduct().getRemainQuantity()) {
                    cardC.setData(cart.get(q), true);
                }
                else{
                    cardC.setData(cart.get(q), false);
                }
                //cardC.setData(cart.get(q), false);
                if (col == 1) {
                    col = 0;
                    row += 1;
                }

                GridPane.setMargin(pane, new Insets(9));
                CartGridPane.add(pane, col++, row);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void addToCart(Product product) {
        // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
        Optional<OrderItem> existingItem = cart.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();

        // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên 1
        if (existingItem.isPresent()) {
            if(existingItem.get().getQuantity() < existingItem.get().getProduct().getRemainQuantity()) {
                existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
            }
            else {
                return;
            }
           
        } else {
            // Nếu sản phẩm chưa tồn tại trong giỏ hàng, tạo một đối tượng OrderItem mới và
            // thêm vào giỏ hàng
            OrderItem newItem = new OrderItem();
            newItem.setProduct(product);
            newItem.setQuantity(1);
            cart.add(newItem);
        }

        System.out.println("Product added to cart: " + product.getName());
    }

    public static void plusQuantity(OrderItem item) {
        // Lấy ra giá trị hiện tại của quantity
        int currentQuantity = item.getQuantity();
        // Tăng giá trị lên 1 và đặt lại vào orderItem
        item.setQuantity(currentQuantity + 1);
        needToLoad.set(false);
    }

    public static void minusQuantity(OrderItem item) {
        // Lấy ra giá trị hiện tại của quantity
        int currentQuantity = item.getQuantity();
        // Giảm giá trị xuống 1 và đặt lại vào orderItem
        item.setQuantity(currentQuantity - 1);

        needToLoad.set(false);
        // Nếu quantity xuống 0, xóa orderItem khỏi danh sách
    }

    public void loadTotals() {
        needToLoad.set(false);
        double totalPrice = 0;
        int totalProduct = 0;
        double discount = 0;
        double finalTotalPrice = 0;
        for (OrderItem orderItem : cart) {
            totalPrice = totalPrice + orderItem.getProduct().getPrice() * orderItem.getQuantity();
            totalProduct += orderItem.getQuantity();
        }
        finalTotalPrice = totalPrice - discount;
        totalPriceText.setText(Double.toString(totalPrice) + " VND");
        totalProductText.setText(Integer.toString(totalProduct));
        finalTotalPriceText.setText(Double.toString(finalTotalPrice) + " VND");
    }

    public void clearCartClick() {
        CartGridPane.getChildren().clear();
        cart.clear();
    }

    public void addOrderToOrders(Order order) {
        this.orders.add(order);
    }

    public void orderClick() {
        createOrder();
        cart.clear();
        CartGridPane.getChildren().clear();
        orders.clear();
        HistoryGridPane.getChildren().clear();
        displayOrders();
        ordernotiText.setFill(Color.web("#88ff4f"));
        timeNotiAppear.setCycleCount(Animation.INDEFINITE);
        timeNotiAppear.play();
        // createOrder();
    }

    private void updateTimeUsed() {

        // Tăng thời gian lên một giây
        minutesUsed++;
        if (minutesUsed == 60) {
            minutesUsed = 0;
            hoursUsed++;
        }
        // Cập nhật hiển thị
        String timeString = String.format("%02d:%02d", hoursUsed, minutesUsed);
        usedPriceText.setText(TimeHandle.timeToMoney(timeString, Computer.pricePerHour) + "VND");
        usedText.setText(timeString);
    }

    private void updateTimeRemains() throws IOException {

        // giảm thời gian đi một giây
        minutesRemains--;
        if (minutesRemains <= 0) {
            if(hoursRemains == 0) {
                loadOutOfTimeModal();
            }
        }

        if (minutesRemains == -1) {
            minutesRemains = 59;
            hoursRemains--;
        }
        // Cập nhật hiển thị
        String timeString = String.format("%02d:%02d", hoursRemains, minutesRemains);
        remainsPriceText.setText(TimeHandle.timeToMoney(timeString, Computer.pricePerHour) + "VND");
        remainsText.setText(timeString);
    }
    private void updateTimeNotiAppear(){
        ordernotiText.setFill(Color.web("#FF5757"));
        timeNotiAppear.stop();
    }
    private void updateTimeHistory() {
        orders.clear();
        HistoryGridPane.getChildren().clear();
        displayOrders();
    }
}
