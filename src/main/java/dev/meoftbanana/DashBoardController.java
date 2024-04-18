package dev.meoftbanana;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashBoardController {
    @FXML
    private Button FoodsNavigationButton;
    @FXML
    private Button BeveragesNavigationButton;
    @FXML
    private Button ChargingNavigationButton;
    @FXML
    private Button CartNavigationButton;
    @FXML
    private Button HistoryNavigationButton;
    @FXML
    private ScrollPane FoodScrollPane;
    @FXML
    private ScrollPane BeverageScrollPane;
    @FXML
    private ScrollPane ChargingScrollPane;

    @FXML
    private void clickShow(ActionEvent event) {

    }

    @FXML
    private void updateButtonState(Button selectedButton) {
        // Reset màu và văn bản của tất cả các nút
        FoodsNavigationButton
                .setStyle("-fx-background-color: #000000; -fx-text-fill: #FCFCFC;-fx-background-radius: 20 0 0 0");
        BeveragesNavigationButton.setStyle("-fx-background-color: #000000; -fx-text-fill: #FCFCFC;");
        ChargingNavigationButton.setStyle("-fx-background-color: #000000; -fx-text-fill: #FCFCFC; ");
        // Cập nhật màu và văn bản của nút được chọn
        selectedButton.setStyle("-fx-background-color: #FCFCFC; -fx-text-fill: #000000;");
    }

    private void showSelectedScrollPane(ScrollPane selectedScrollPane) {
        // Ẩn tất cả các ScrollPane
        FoodScrollPane.setVisible(false);
        BeverageScrollPane.setVisible(false);
        ChargingScrollPane.setVisible(false);

        // Hiển thị ScrollPane tương ứng với nút được nhấn
        selectedScrollPane.setVisible(true);
    }

    @FXML
    private void updateCartButtonState(Button selectedButton) {
        // Reset màu và văn bản của tất cả các nút
        CartNavigationButton
                .setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;-fx-background-radius: 20 0 0 0");
        HistoryNavigationButton
                .setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;-fx-background-radius: 0 20 0 0");
        // Cập nhật màu và văn bản của nút được chọn
        selectedButton.setStyle("-fx-background-color:  #FF5757; -fx-text-fill: #FFFFFF;");
    }

    @FXML
    private void handleFoodsNavigationButtonClick(ActionEvent event) {
        showSelectedScrollPane(FoodScrollPane);
        updateButtonState(FoodsNavigationButton);
        FoodsNavigationButton
                .setStyle("-fx-background-color: #FCFCFC; -fx-text-fill: #000000; -fx-background-radius: 20 0 0 0");
        // Thêm mã xử lý cho nút thức ăn ở đây
    }

    @FXML
    private void handleBeveragesNavigationButtonClick(ActionEvent event) {

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
    private void handleCartNavigationButtonClick(ActionEvent event) {
        updateCartButtonState(CartNavigationButton);
        CartNavigationButton
                .setStyle("-fx-background-color:  #FF5757; -fx-text-fill: #FFFFFF; -fx-background-radius: 20 0 0 0");

        // Thêm mã xử lý cho nút sạc ở đây
    }

    @FXML
    private void handleHistoryNavigationButtonClick(ActionEvent event) {
        updateCartButtonState(HistoryNavigationButton);
        HistoryNavigationButton
                .setStyle("-fx-background-color:  #FF5757; -fx-text-fill: #FFFFFF; -fx-background-radius: 0 20 0 0");
        // Thêm mã xử lý cho nút sạc ở đây
    }

}
