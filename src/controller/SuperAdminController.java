package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import databases.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Session;
import model.SignUp;
import model.UserInfo;
import passwordhasher.PasswordHash;

public class SuperAdminController implements Initializable{

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    private double x = 0;
    private double y = 0;
    
    @FXML
    private AnchorPane main_form;

    @FXML
    private Text sideRole;

    @FXML
    private Text sideEmail;

    @FXML
    private Text totalUser;

    @FXML
    private Text totalSuperAdmin;

    @FXML
    private Text totalAdmin;

    @FXML
    private Button logout;

    @FXML
    private Button backtodashboard;
    
    @FXML
    private TableColumn<UserInfo, String> dashboard_fullname;

    @FXML
    private TableColumn<UserInfo, String> dashboard_password;

    @FXML
    private TableColumn<UserInfo, String> dashboard_role;

    @FXML
    private TableColumn<UserInfo, String> dashboard_email;

    @FXML
    private TableView<UserInfo> dashboard_table;

    @FXML
    private TableColumn<UserInfo, String> dashboard_userid;

    @FXML
    private TableColumn<UserInfo, String> dashboard_username;

    @FXML
    private TextField dashboard_search;

    @FXML
    private ComboBox<String> chooseRole;

    @FXML
    private TextField uidTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    public ObservableList<UserInfo> addUserListData(){
        ObservableList<UserInfo> ListUser = FXCollections.observableArrayList();

        String sql = "SELECT * FROM admin";
        connect = DBConnection.connect();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            UserInfo user;

            while (result.next()) {
                user = new UserInfo(result.getInt("uid"), result.getString("name"), result.getString("username"), result.getString("email"), result.getString("password"), result.getString("role"));
                ListUser.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListUser;
    }

    private ObservableList<UserInfo> addUserList;
    private FilteredList<UserInfo> filter;

    public void adminDashboardShowListUser(){
        addUserList = addUserListData();
        filter = new FilteredList<>(addUserList, p -> true);

        dashboard_userid.setCellValueFactory(new PropertyValueFactory<>("userID"));
        dashboard_fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        dashboard_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        dashboard_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        dashboard_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        dashboard_role.setCellValueFactory(new PropertyValueFactory<>("role"));

        dashboard_table.setItems(filter);

        dashboard_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(user -> {
                if(newValue == null || newValue.isEmpty()) return true;

                String searchKey = newValue.toLowerCase();

                if(user.getUserID().toString().contains(searchKey)){
                    return true;
                }else if(user.getFullname().toLowerCase().contains(searchKey)){
                    return true;
                }else if(user.getUsername().toLowerCase().contains(searchKey)){
                    return true;
                }else if(user.getEmail().toLowerCase().contains(searchKey)){
                    return true;
                }else if(user.getPassword().toLowerCase().contains(searchKey)){
                    return true;
                }else if(user.getRole().toLowerCase().contains(searchKey)){
                    return true;
                }
                return false;
            });
        });
    }

    @FXML
    public void userSelectItem(){
        UserInfo user = dashboard_table.getSelectionModel().getSelectedItem();
        int num = dashboard_table.getSelectionModel().getSelectedIndex();

        if((num - 1) < -1) return;

        uidTextField.setText(String.valueOf(user.getUserID()));
        nameTextField.setText(String.valueOf(user.getFullname()));
        usernameTextField.setText(String.valueOf(user.getUsername()));
        emailTextField.setText(String.valueOf(user.getEmail()));
        passwordField.setText("");
        chooseRole.setValue(String.valueOf(user.getRole()));
    }

    @FXML
    private void btnMinimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void btnClose(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to exit?");
        Optional<ButtonType> option = alert.showAndWait();

        if(option.get().equals(ButtonType.OK)){
            System.exit(0);
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

    @FXML
    private void clearUser(){
        uidTextField.setText("");
        nameTextField.setText("");
        usernameTextField.setText("");
        emailTextField.setText("");
        passwordField.setText("");
        chooseRole.setStyle("-fx-border-color: #616161;");
    }

    @FXML
    private void btnBackDashboard() throws Exception{
        backtodashboard.getScene().getWindow().hide();

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
    }

    @FXML
    private void addUser(){
        String sql = "INSERT INTO admin (uid, name, username, email, password, role) VALUES (?, ?, ?, ?, ?, ?)";

        connect = DBConnection.connect();

        try {
            if(uidTextField.getText().isEmpty() || nameTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || passwordField.getText().isEmpty() || chooseRole.getSelectionModel().getSelectedItem() == null){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the blank fields");
                alert.showAndWait();
            }else{
                try {
                    Integer.parseInt(uidTextField.getText());
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("UID must be a number");
                    alert.showAndWait();
                    return;
                }

                if(!EMAIL_PATTERN.matcher(emailTextField.getText()).matches()){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("\t\t\t\tInvalid email\n\nPlease put and @ in your email (ex. name@example.com)");
                    alert.showAndWait();
                    return;
                }

                if(uidTextField.getText().equals("0")){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid ID");
                    alert.showAndWait();
                    return;
                }

                String checkErrorEmail = "SELECT email FROM admin WHERE email = '" + emailTextField.getText() + "'";
                statement = connect.createStatement();
                result = statement.executeQuery(checkErrorEmail);
                if(result.next()){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Email: " + emailTextField.getText() + " already existed");
                    alert.showAndWait();
                    return;
                }

                String checkUsername = "SELECT username FROM admin WHERE username = ?";
                prepare = connect.prepareStatement(checkUsername);
                prepare.setString(1, usernameTextField.getText());
                result = prepare.executeQuery();
                
                if(result.next()){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Username: " + usernameTextField.getText() + " already existed");
                    alert.showAndWait();
                    return;
                }


                String checkErrorID = "SELECT uid FROM admin WHERE uid = '" + uidTextField.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkErrorID);

                if(result.next()){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("UID: " + uidTextField.getText() + " already existed");
                    alert.showAndWait();
                }else{
                    String encryptPassword = PasswordHash.password_hash(passwordField.getText());

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, uidTextField.getText());
                    prepare.setString(2, nameTextField.getText());
                    prepare.setString(3, usernameTextField.getText());
                    prepare.setString(4, emailTextField.getText());
                    prepare.setString(5, encryptPassword);
                    prepare.setString(6, (String)chooseRole.getSelectionModel().getSelectedItem());
                    prepare.executeUpdate();

                    Alert alert = new Alert(AlertType.INFORMATION);

                    alert.setTitle("Success message");
                    alert.setHeaderText(null);
                    alert.setContentText("User added successfully");
                    alert.showAndWait();

                    adminDashboardShowListUser();
                    countOfAdmin();
                    countOfSuperAdmin();
                    countofUser();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateUser(){
        String sql;

        connect = DBConnection.connect();
        try{

            if(uidTextField.getText().isEmpty() || nameTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || chooseRole.getSelectionModel().getSelectedItem() == null){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the blank fields");
                alert.showAndWait();
            }else{
                try {
                    Integer.parseInt(uidTextField.getText());
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("UID must be a number");
                    alert.showAndWait();
                    return;
                }

                String checkID = "SELECT uid FROM admin WHERE uid = ?";
                
                prepare = connect.prepareStatement(checkID);
                prepare.setInt(1, Integer.parseInt(uidTextField.getText()));
                result = prepare.executeQuery();

            if(!result.next()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Employee ID: " + uidTextField.getText() + " doesn't exist");
                alert.showAndWait();
                return;
            }
    
                if(!EMAIL_PATTERN.matcher(emailTextField.getText()).matches()){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("\t\t\t\tInvalid email\n\nPlease put and @ in your email (ex. name@example.com)");
                    alert.showAndWait();
                    return;
                }
    
                if(uidTextField.getText().equals("0")){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid ID");
                    alert.showAndWait();
                    return;
                }

                Alert alert = new Alert(AlertType.CONFIRMATION);

                alert.setTitle("Confirmation message");
                alert.setHeaderText(null);
                alert.setContentText("Note: Password can't be update\n\nAre you sure want to update UID: " + uidTextField.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    if(uidTextField.getText().equals("1")){
                        sql = "UPDATE admin SET name = ?, username = ?, email = ? WHERE uid = ?";
                    }else{
                        sql = "UPDATE admin SET name = ?, username = ?, email = ?, role = ? WHERE uid = ?";
                    }


                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, nameTextField.getText());
                    prepare.setString(2, usernameTextField.getText());
                    prepare.setString(3, emailTextField.getText());

                    if(!uidTextField.getText().equals("1")){
                        prepare.setString(4, (String)chooseRole.getSelectionModel().getSelectedItem());
                        prepare.setString(5, uidTextField.getText());
                    }else{
                        prepare.setString(4, uidTextField.getText());
                    }

                    prepare.executeUpdate();

                    adminDashboardShowListUser();
                    countOfAdmin();
                    countOfSuperAdmin();
                    countofUser();

                    if(Session.getUserID() != 1){

                        Alert alert2 = new Alert(AlertType.WARNING);
                        alert2.setTitle("Session expired");
                        alert2.setHeaderText(null);
                        alert2.setContentText("Session Expired");
                        alert2.showAndWait();
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
                }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteUser(){
        String sql = "DELETE FROM admin WHERE uid = ?";

        connect = DBConnection.connect();

        try {
            if(uidTextField.getText().isEmpty() || nameTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || chooseRole.getSelectionModel().getSelectedItem() == null){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the blank fields");
                alert.showAndWait();
            }else{
                try {
                    Integer.parseInt(uidTextField.getText());
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("UID must be a number");
                    alert.showAndWait();
                    return;
                }
                
                if(uidTextField.getText().equals("1")){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("You can't delete this Super Admin");
                    alert.showAndWait();
                    return;
                }

                String checkID = "SELECT uid FROM admin WHERE uid = ?";
                
                prepare = connect.prepareStatement(checkID);
                prepare.setInt(1, Integer.parseInt(uidTextField.getText()));
                result = prepare.executeQuery();

            if(!result.next()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("UID: " + uidTextField.getText() + " doesn't exist");
                alert.showAndWait();
                return;
            }

            if(uidTextField.getText().equals("0")){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid ID");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure want to delete UID: " + uidTextField.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, uidTextField.getText());
                prepare.executeUpdate();
                adminDashboardShowListUser();
                countOfAdmin();
                countOfSuperAdmin();
                countofUser();
            }

            if(Session.getUserID() != 1){

                Alert alert2 = new Alert(AlertType.WARNING);
                alert2.setTitle("Session expired");
                alert2.setHeaderText(null);
                alert2.setContentText("Session Expired");
                alert2.showAndWait();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void countofUser(){
        String sql = "SELECT COUNT(role) FROM admin WHERE role = 'User'";

        connect = DBConnection.connect();
        int countData = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countData = result.getInt("COUNT(role)");
            }

            totalUser.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void countOfSuperAdmin(){
        String sql = "SELECT COUNT(role) FROM admin WHERE role = 'Super Admin'";

        connect = DBConnection.connect();
        int countData = 0;

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countData = result.getInt("COUNT(role)");
            }

            totalSuperAdmin.setText(String.valueOf(countData));
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private void countOfAdmin(){
        String sql = "SELECT COUNT(role) FROM admin WHERE role = 'Admin'";

        connect = DBConnection.connect();
        int countData = 0;

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countData = result.getInt("COUNT(role)");
            }

            totalAdmin.setText(String.valueOf(countData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resource){
        String sql = "SELECT * FROM admin WHERE id = ?";

        connect = DBConnection.connect();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Session.getUserID());
            result = prepare.executeQuery();

            while (result.next()) {
                sideRole.setText(result.getString("role"));
                sideEmail.setText(result.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        countOfPending();
        showPendingListData();
        countOfAdmin();
        countOfSuperAdmin();
        countofUser();
        adminDashboardShowListUser();
        adminDashboard();
        ObservableList<String> roleList = FXCollections.observableArrayList("Super Admin", "Admin", "User");
        chooseRole.setItems(roleList);
    }

    @FXML
    private TableView<SignUp> pending_table;

    @FXML
    private TableColumn<SignUp, String> pending_name;

    @FXML
    private TableColumn<SignUp, String> pending_username;

    @FXML
    private TableColumn<SignUp, String> pending_email;

    @FXML
    private TableColumn<SignUp, String> pending_password;

    @FXML
    private TextField pendingSearch;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private AnchorPane pending_form;

    @FXML
    private Button adminbtn;

    @FXML
    private Button pendingbtn;

    @FXML
    private Button pendingText;

    @FXML
    private TextField new_uid;

    @FXML
    private TextField new_name;

    @FXML
    private TextField new_username;

    @FXML
    private TextField new_email;
    
    @FXML
    private TextField new_password;

    @FXML
    private TextField new_role;

    public ObservableList<SignUp> addPendingUser(){
        ObservableList<SignUp> listPending = FXCollections.observableArrayList();

        String sql = "SELECT * FROM storeuser";

        connect = DBConnection.connect();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            SignUp user1;
            while (result.next()) {
                user1 = new SignUp(result.getString("name"), result.getString("username"), result.getString("email"), result.getString("password"));
                listPending.add(user1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listPending;
    }

    private ObservableList<SignUp> addPendingList;
    private FilteredList<SignUp> filter1;

    private void showPendingListData(){
        addPendingList = addPendingUser();
        filter1 = new FilteredList<>(addPendingList, p -> true);

        pending_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        pending_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        pending_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        pending_password.setCellValueFactory(new PropertyValueFactory<>("password"));

        pending_table.setItems(filter1);

        pendingSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filter1.setPredicate(user1 -> {
                if(newValue == null || newValue.isEmpty()) return true;

                String searchKey = newValue.toLowerCase();

                if(user1.getName().toLowerCase().contains(searchKey)){
                    return true;
                }else if(user1.getUsername().toLowerCase().contains(searchKey)){
                    return true;
                }else if(user1.getEmail().toLowerCase().contains(searchKey)){
                    return true;
                }else if(user1.getPassword().toLowerCase().contains(searchKey)){
                    return true;
                }
                return false;
            });
        });

    }

    @FXML
    private void adminDashboard(){
        adminDashboardShowListUser();
        countOfAdmin();
        countofUser();
        countOfSuperAdmin();
        countOfPending();
        dashboard_form.setVisible(true);
        pending_form.setVisible(false);
        adminbtn.setStyle("-fx-background-color: #03AED2; -fx-background-radius: 8px; -fx-text-fill: #ffff;");
        pendingbtn.setStyle("-fx-background-color: transparent;");
    }

    @FXML
    private void pendingDashboard(){
        countOfPending();
        showPendingListData();
        pending_form.setVisible(true);
        dashboard_form.setVisible(false);
        pendingbtn.setStyle("-fx-background-color: #03AED2; -fx-background-radius: 8px; -fx-text-fill: #ffff;");
        adminbtn.setStyle("-fx-background-color: transparent;");
    }

    private void countOfPending(){
        String sql = "SELECT COUNT(id) FROM storeuser";

        connect = DBConnection.connect();
        int countData = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countData = result.getInt("COUNT(id)");
            }
            pendingText.setText(String.valueOf(countData));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void selectPending(){
        SignUp user = pending_table.getSelectionModel().getSelectedItem();

        int num = pending_table.getSelectionModel().getSelectedIndex();

        if((num -1) < -1) return;

        new_name.setText(String.valueOf(user.getName()));
        new_username.setText(String.valueOf(user.getUsername()));
        new_email.setText(String.valueOf(user.getEmail()));
        new_password.setText(String.valueOf(user.getPassword()));
    }

    @FXML
    private void deleteRequest(){
        String deleteStoreUser = "DELETE FROM storeuser WHERE username = ?";

        connect = DBConnection.connect();
        try {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Confirmation message");
            alert.setContentText("Are you sure want to delete this pending request?");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){
                prepare = connect.prepareStatement(deleteStoreUser);
                prepare.setString(1, new_username.getText());
                prepare.executeUpdate();
                showPendingListData();
                countOfPending();
                new_uid.setText("");
                new_name.setText("");
                new_username.setText("");
                new_email.setText("");
                new_password.setText("");
                new_role.setText("User");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void acceptRequest(){
        String sql = "INSERT INTO admin (uid, name, username, email, password, role) VALUES (?, ?, ?, ?, ?, ?)";

        connect = DBConnection.connect();
        SignUp user = new SignUp(new_name.getText(), new_username.getText(), new_email.getText(), new_password.getText());
        try {
            if(new_uid.getText().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setContentText("Please put an UID");
                alert.setHeaderText(null);
                alert.showAndWait();
                return;
            }

            String checkUID = "SELECT uid FROM admin WHERE uid = ?";

            prepare = connect.prepareStatement(checkUID);
            prepare.setString(1, new_uid.getText());
            result = prepare.executeQuery();

            if(result.next()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setContentText("UID: " + new_uid.getText() + " already existed");
                alert.setHeaderText(null);
                alert.showAndWait();
                return;
            }

            try {
                Integer.parseInt(new_uid.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("UID must be a number");
                alert.showAndWait();
                return;
            }

            if(new_uid.getText().equals("0")){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid ID");
                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText("Accepted successfully");
            alert.showAndWait();

            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Integer.parseInt(new_uid.getText()));
            prepare.setString(2, user.getName());
            prepare.setString(3, user.getUsername());
            prepare.setString(4, user.getEmail());
            prepare.setString(5, user.getPassword());
            prepare.setString(6, new_role.getText());
            prepare.executeUpdate();

            String deleteStoreUser = "DELETE FROM storeuser WHERE username = ?";

            prepare = connect.prepareStatement(deleteStoreUser);
            prepare.setString(1, user.getUsername());
            prepare.executeUpdate();
            showPendingListData();
            countOfPending();
            new_uid.setText("");
            new_name.setText("");
            new_username.setText("");
            new_email.setText("");
            new_password.setText("");
            new_role.setText("User");
        } catch (Exception e) {
          e.printStackTrace();
        }

    }
}