package dev.meoftbanana;

import dev.meoftbanana.model.OrderItem;
import dev.meoftbanana.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class OrderItemCardController{
    @FXML
    private Button minusButton;

    @FXML
    private Button plusButton;

    @FXML
    private ImageView productImage;

    @FXML
    private Text productNameText;

    @FXML
    private Text quantityText;

    @FXML
    private Text totalPriceText;

    private OrderItem orderItem;

    public void setData(OrderItem orderItem, boolean isLimit) {
        this.orderItem = orderItem;
        this.plusButton.setDisable(isLimit);
        productNameText.setText(orderItem.getProduct().getName());
        quantityText.setText(Integer.toString(orderItem.getQuantity()));
        totalPriceText.setText(Double.toString(orderItem.getProduct().getPrice()*orderItem.getQuantity()) + "VND");
        Image img = new Image(getClass().getResourceAsStream("image/" + orderItem.getProduct().getProductImageLink()));
        this.productImage.setImage(img);
        
    }
    @FXML
    public void plusButtonClick() {
        DashBoardController.plusQuantity(orderItem);
        if(this.orderItem.getQuantity() == this.orderItem.getProduct().getRemainQuantity()) {
            this.plusButton.setDisable(true);
        }
        DashBoardController.needToLoad.set(true);
    }
    @FXML
    public void minusButtonClick() {
        DashBoardController.minusQuantity(orderItem);
        if(this.orderItem.getQuantity() < 1){
            //this.minusButton.setDisable(true);
            DashBoardController.cart.remove(orderItem);
        }
        DashBoardController.needToLoad.set(true);
    }

    public OrderItem getOrderItem() {
        return this.orderItem;
    }
    
}
