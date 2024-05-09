package dev.meoftbanana;

import dev.meoftbanana.model.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class HistoryCardController {
    @FXML
    private Label orderId;

    @FXML
    private Text orderStatus;

    @FXML
    private Label timeCreated;

    @FXML
    private Text totalPriceText;

    private Order order;

    public void setData(Long orderId, String orderStatus, String timeCreated, String totalPrice) {
        this.orderId.setText("#" + orderId);
        this.orderStatus.setText(orderStatus);
        this.timeCreated.setText(timeCreated);
        this.totalPriceText.setText(totalPrice);
    }

    public void setData(Order order) {
        this.order = order;
        this.orderId.setText("#" + order.getOrderId());
        this.orderStatus.setText(order.getOrderStatus());
        //orderStatus.setTextFill(Color.web("#189149"));
        if(order.getOrderStatus().equals("PAID")) {
            this.orderStatus.setFill(Color.web("#189149"));
        }
        this.timeCreated.setText(order.getTimeCreated());
        this.totalPriceText.setText(order.getTotal().toString() + "VND");

    }


}
