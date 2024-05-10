package dev.meoftbanana;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.io.File;

import dev.meoftbanana.model.Product;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class CardProductController implements Initializable {

    @FXML
    private Button addToCartButton;

    @FXML
    private Text price;

    @FXML
    private ImageView productImage;

    @FXML
    private Text productName;

    private Product product;

    
    @FXML
    public void buttonAddClick() {
        DashBoardController.addToCart(product);
        DashBoardController.needToLoad.set(true);
    }

    public void setData(String price, String imageLink, String productName) {
        this.price.setText(price);
        String path = "file:" + imageLink;
        Image image = new Image(path, 190, 94, false, true);
        this.productImage.setImage(image);
    }

    public void setData(Product product, boolean isOutOfStock) {
        this.product = product;
        if(isOutOfStock) {
            this.addToCartButton.setText("Out of Stock");
        }
        this.addToCartButton.setDisable(isOutOfStock);
        productName.setText(product.getName());
        price.setText(Double.toString(product.getPrice()) + "VND");
        try {
            Image img = new Image(getClass().getResourceAsStream("image/" + product.getProductImageLink()));
            this.productImage.setImage(img);
        }
        catch (Exception e) {
            Image img = new Image(getClass().getResourceAsStream("image/foods.jpg"));
            this.productImage.setImage(img);
        }

        
        //this.productImage = new ImageView(image);
    }

    public Product getData() {
        return this.product;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        File imageFile = new File("image/meo.jpg");
        String path = imageFile.toURI().toString(); 
        Image image = new Image(path);
        //this.productImage.setImage(image);
        price.setTextAlignment(TextAlignment.CENTER);
    }

}
