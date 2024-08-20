package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Session;
import passwordhasher.PasswordHash;
import databases.DBConnection;

public class SettingsController implements Initializable{

    private double x = 0;
    private double y = 0;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    @FXML
    private Button closebtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Text errorHandler;

    @FXML
    private Button confirmbtn;

    @FXML
    private Button logout;

    @FXML
    private void btnMinimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void btnClose(){
        closebtn.getScene().getWindow().hide();

        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/views/dashboard_layout.fxml"));
            Scene scene = new Scene(root);

            root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });

            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);

                stage.setOpacity(.8);
            });

            root.setOnMouseReleased((MouseEvent event) -> {
                stage.setOpacity(1);
            });

            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnConfirm(){
        String sql = "SELECT * FROM admin WHERE id = ?";

        connect = DBConnection.connect();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Session.getUserID());
            result = prepare.executeQuery();
            result.next();
            
            boolean verifyPassword = PasswordHash.password_verify(confirmPassword.getText(), result.getString("password"));

            if(confirmPassword.getText().isEmpty()){
                errorHandler.setText("Password field is required");
                errorHandler.setFill(Color.web("EE4266"));

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Password field is required");
                alert.showAndWait();
            }else{
                if(verifyPassword){
                    errorHandler.setText("Success");
                    errorHandler.setFill(Color.web("198754"));
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success message");
                    alert.setHeaderText(null);
                    alert.setContentText("Success");
                    alert.showAndWait();

                    confirmbtn.getScene().getWindow().hide();

                    Stage stage = new Stage();

                    Parent root = FXMLLoader.load(getClass().getResource("/views/superadmin_layout.fxml"));
                    Scene scene = new Scene(root);

                    root.setOnMousePressed((MouseEvent event) -> {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent event) -> {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);

                        stage.setOpacity(.8);
                    });

                    root.setOnMouseReleased((MouseEvent event) -> {
                        stage.setOpacity(1);
                    });

                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();
                }else{
                    errorHandler.setText("Wrong password!");
                    errorHandler.setFill(Color.web("EE4266"));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void btnLogout(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to log out?");
        Optional<ButtonType> optional = alert.showAndWait();

        if(optional.get().equals(ButtonType.OK)){
            logout.getScene().getWindow().hide();
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/views/login_layout.fxml"));
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resource){

    }
    
}
