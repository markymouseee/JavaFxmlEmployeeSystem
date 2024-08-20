package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import databases.DBConnection;
import model.AdminLogin;
import model.Session;
import passwordhasher.PasswordHash;
import model.SignUp;

public class AdminLoginController implements Initializable{

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private double x = 0;
    private double y = 0;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @FXML
    private Pane login_form;

    @FXML
    private Text errorHandler;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField showpasswordField;

    @FXML
    private Button login;

    @FXML
    private AnchorPane showpass_form;

    @FXML
    private AnchorPane hidepass_form;

    @FXML
    private AnchorPane loginForm;

    @FXML
    private AnchorPane email_form;

    @FXML
    private AnchorPane signup_form;


    @FXML
    private void forgotPassword(){
        loginForm.setVisible(false);
        email_form.setVisible(true);
    }

    @FXML
    private void backtologin(){
        email_form.setVisible(false);
        loginForm.setVisible(true);
    }

    @FXML
    private void haveAnAccount(){
        signup_form.setVisible(false);
        loginForm.setVisible(true);
    }

    @FXML
    private void createAccount(){
        loginForm.setVisible(false);
        signup_form.setVisible(true);
    }

    @FXML
    private void btnLogin(){
        String storeUser = "SELECT * FROM storeuser WHERE (username = ? OR email = ?)";
        
        connect = DBConnection.connect();
        try {

            AdminLogin temp_user = null;

            if(showpass_form.isVisible()){
                temp_user = new AdminLogin(usernameField.getText(), passwordField.getText());
            }else if(hidepass_form.isVisible()){
                temp_user = new AdminLogin(usernameField.getText(), showpasswordField.getText());
            }
            prepare = connect.prepareStatement(storeUser);
            prepare.setString(1, temp_user.getUsername());
            prepare.setString(2, temp_user.getUsername());
            result = prepare.executeQuery();

            if(result.next()){
                boolean Verify = PasswordHash.password_verify(temp_user.getPassword(), result.getString("password"));
                if(Verify){
                    showAlert(AlertType.INFORMATION, "Your account is pending, please wait for admin approval.", "Message from admin");
                    return;
                }else{
                    errorHandler.setText("Wrong password, please try again");
                    errorHandler.setFill(Color.web("EE4266"));
                }
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql = "SELECT id, role, email, username, password FROM admin WHERE (username = ? OR email = ?)";

        
        try {
            
            AdminLogin user = null;
            if(showpass_form.isVisible()){
                user = new AdminLogin(usernameField.getText(), passwordField.getText());
            }else if(hidepass_form.isVisible()){
                user = new AdminLogin(usernameField.getText(), showpasswordField.getText());
            }

            if(user == null || user.getUsername().isEmpty() || user.getPassword().isEmpty()){
                errorHandler.setText("Username or password can't be empty");
                errorHandler.setFill(Color.web("EE4266"));
                return;
            }

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, user.getUsername());
                prepare.setString(2, user.getUsername());
                result = prepare.executeQuery();
                
                if(result.next()){
                    boolean verifyPassword = PasswordHash.password_verify(user.getPassword(), result.getString("password"));
                    if(verifyPassword){
                        Session.setUserID(result.getInt("id"));
                        Session.setRole(result.getString("role"));
                        Session.setUsername(result.getString("username"));
                        Session.setEmail(result.getString("email"));
                        errorHandler.setText("Successfully log in");
                        errorHandler.setFill(Color.web("198754"));
    
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Success message");
                        alert.setHeaderText(null);
                        alert.setContentText("Login successfully");
                        alert.showAndWait();
    
                        Stage stage = new Stage();
                        login.getScene().getWindow().hide();
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

                    }else{
                        errorHandler.setText("Wrong password, please try again");
                        errorHandler.setFill(Color.web("EE4266"));
                    }

                }else{
                    errorHandler.setText("Username or email not found");
                    errorHandler.setFill(Color.web("EE4266"));
                }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showpassword(){
        showpasswordField.setText(passwordField.getText());
        showpass_form.setVisible(false);
        hidepass_form.setVisible(true);
    }

    @FXML
    private void hidepassword(){
        passwordField.setText(showpasswordField.getText());
        hidepass_form.setVisible(false);
        showpass_form.setVisible(true);
    }

    @FXML
    private void btnClose(ActionEvent event){
        System.exit(0);
    }

    @FXML
    private void btnMinimize(ActionEvent event){
        Stage stage = (Stage)login_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resource){
    }

    /*SIGN UP CONTROLLER */

    @FXML
    private TextField new_fullname;

    @FXML
    private TextField new_username;

    @FXML
    private TextField new_email;

    @FXML
    private TextField new_password;

    @FXML
    private TextField new_retype_password;

    @FXML
    private void btnSignUp(){
        String sql = "INSERT INTO storeuser (name, username, email, password) VALUES (?, ?, ?, ?)";

        connect = DBConnection.connect();

        SignUp user = new SignUp(new_fullname.getText(), new_username.getText(), new_email.getText(), new_password.getText());
        try {
            if(user.getName().isEmpty() || user.getUsername().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()){
                showAlert(AlertType.ERROR, "Please fill the blanks", "Error message");
            }else{
                String checkusername = "SELECT username FROM admin WHERE username = ?";

                prepare = connect.prepareStatement(checkusername);
                prepare.setString(1, new_username.getText());
                result = prepare.executeQuery();

                if(result.next()){
                    showAlert(AlertType.ERROR, "Username: " + new_username.getText() + " already existed", "Error message");
                    return;
                }

                String checkusernameStore = "SELECT username FROM storeuser WHERE username = ?";
                prepare = connect.prepareStatement(checkusernameStore);
                prepare.setString(1, new_username.getText());
                result = prepare.executeQuery();

                if(result.next()){
                    showAlert(AlertType.ERROR, "Username: " + new_username.getText() + " already existed", "Error message");
                    return;
                }

                if(!EMAIL_PATTERN.matcher(new_email.getText()).matches()){
                    showAlert(AlertType.ERROR, "\t\t\t\tInvalid email\n\nPlease put an @ in your email (ex. name@example.com)", "Error message");
                    return;
                }

                String checkemail = "SELECT email FROM admin WHERE email = ?";

                prepare = connect.prepareStatement(checkemail);
                prepare.setString(1, new_email.getText());
                result = prepare.executeQuery();

                if(result.next()){
                    showAlert(AlertType.ERROR, "Email: " + new_email.getText() + " already registered", "Error message");
                    return;
                }

                String checkemailStore = "SELECT email FROM storeuser WHERE email = ?";
                prepare = connect.prepareStatement(checkemailStore);
                prepare.setString(1, new_email.getText());
                result = prepare.executeQuery();
                
                if(result.next()){
                    showAlert(AlertType.ERROR, "Email: " + new_email.getText() + " already registered", "Error message");
                    return;
                }

                if(!new_password.getText().equals(new_retype_password.getText())){
                    showAlert(AlertType.ERROR, "Password doesn't match", "Error message");
                    return;
                }

                if(new_password.getText().length() < 6){
                    showAlert(AlertType.ERROR, "Your password must be atleast 6 characters.", "Error message");
                    return;
                }

                showAlert(AlertType.INFORMATION, "Registration requested successfully, please wait for admin\n\t\t\t\tapproval.", "Message");

                String encryptPassword = PasswordHash.password_hash(user.getPassword());

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, user.getName());
                prepare.setString(2, user.getUsername());
                prepare.setString(3, user.getEmail());
                prepare.setString(4, encryptPassword);
                prepare.executeUpdate();
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String contentText, String title){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}