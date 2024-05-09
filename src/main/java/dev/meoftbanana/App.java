package dev.meoftbanana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;

import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        Parent root = loadFXML("client_login_page");
        Scene scene = new Scene(root);


        //Scene scene = new Scene(loadFXML("client_dashboard"), 1440, 1024);
        //Scene scene = new Scene(loadFXML("client_dashboard"));

        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("https://scontent.fsgn5-5.fna.fbcdn.net/v/t39.30808-6/234909417_1554557444883622_5333522347211506343_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeHpsuvmmhop_glDXe6tnmez50x7o3DgoeLnTHujcOCh4omto_Hg-qp3L6rIFNDybvkFQADSftql1M4ItZxgUx-g&_nc_ohc=9pyigiRxSt4Q7kNvgFdUuou&_nc_ht=scontent.fsgn5-5.fna&oh=00_AfDrwJo5ibLW_s7xfhH53ewOhzxGNvvcjP0lDQHHZ3Wjpw&oe=663FDB17"));
        stage.initStyle(StageStyle.DECORATED);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/dev/meoftbanana/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}