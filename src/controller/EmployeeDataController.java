package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import databases.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.EmployeeData;
import model.Session;
import passwordhasher.PasswordHash;
import javafx.scene.control.Button;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;

public class EmployeeDataController implements Initializable{

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private double x = 0;
    private double y = 0;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @FXML
    private Button logout;

    @FXML
    private ComboBox<String> chooseSex;

    @FXML
    private ComboBox<String> choosePosition;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TableView<EmployeeData> employeeTable;

    @FXML
    private TableColumn<EmployeeData, String> date_column;

    @FXML
    private TableColumn<EmployeeData, String> email_column;

    @FXML
    private TableColumn<EmployeeData, String> employeeid_column;

    @FXML
    private TableColumn<EmployeeData, String> firstname_column;

    @FXML
    private TableColumn<EmployeeData, String> lastname_column;

    @FXML
    private TableColumn<EmployeeData, String> position_column;

    @FXML
    private TableColumn<EmployeeData, String> sex_column;

    @FXML
    private TextField employeeidTextField;

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField searchTextField;

    @FXML
    private AnchorPane employeetable_form;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Button dashboardbtn;

    @FXML
    private Button employeetablebtn;

    @FXML
    private Button settingsbtn;

    @FXML
    private Text totalEmployee;

    @FXML
    private Text detailsName;

    @FXML
    private Text detailsEmail;

    @FXML
    private Text detailsRole;

    @FXML
    private Text sideEmail;

    @FXML
    private Text sideRole;

    @FXML
    private Text totalAdmin;

    @FXML
    private Text totalUser;

    @FXML
    private Text totalSuperAdmin;

    @FXML
    private TableColumn<EmployeeData, String> dashboard_date;

    @FXML
    private TableColumn<EmployeeData, String> dashboard_email;

    @FXML
    private TableColumn<EmployeeData, String> dashboard_employeeid;

    @FXML
    private TableColumn<EmployeeData, String> dashboard_firstname;

    @FXML
    private TableColumn<EmployeeData, String> dashboard_lastname;

    @FXML
    private TableColumn<EmployeeData, String> dashboard_position;

    @FXML
    private TableColumn<EmployeeData, String> dashboard_sex;

    
    @FXML
    private TableView<EmployeeData> dashboard_table;

    @FXML
    private TextField dashboard_search;

    @FXML
    private Text femaleDisplay;

    @FXML
    private Text maleDisplay;

    @FXML
    private Button btnRefresh;

    @FXML
    private void refreshPage(){

        String checkUser = "SELECT * FROM admin WHERE id = ?";

        connect = DBConnection.connect();
        try {
            prepare = connect.prepareStatement(checkUser);
            prepare.setInt(1, Session.getUserID());
            result = prepare.executeQuery();

            Stage stage = new Stage();

            if(!result.next()){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning message"); 
                alert.setHeaderText(null);
                alert.setContentText("Session expired, please log in again!");
                alert.showAndWait();

                btnRefresh.getScene().getWindow().hide();

                Parent loginScene = FXMLLoader.load(getClass().getResource("/views/login_layout.fxml"));
                Scene scene2 = new Scene(loginScene);
                
                loginScene.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
        
                loginScene.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
        
                    stage.setOpacity(.8);
                });
        
                loginScene.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });
        
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene2);
                stage.show();
            }

            btnRefresh.getScene().getWindow().hide();

            String sql = "SELECT id, username, email, role FROM admin WHERE (username = ? OR email = ?)";

            connect = DBConnection.connect();

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Session.getUsername());
            prepare.setString(2, Session.getEmail());
            result = prepare.executeQuery();
            
            while (result.next()) {
                Session.setUserID(result.getInt("id"));
                Session.setRole(result.getString("role"));
                Session.setEmail(result.getString("email"));
                Session.setUsername(result.getString("username"));
            }
            
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
        }catch(Exception e){
            e.printStackTrace();
        }
    }


     public ObservableList<EmployeeData> addEmployeeListData(){

        ObservableList<EmployeeData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM employeedata";

        connect = DBConnection.connect();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            EmployeeData employeeData;

            while (result.next()) {
                employeeData = new EmployeeData(result.getInt("employeeid"), result.getString("firstname"), result.getString("lastname"), result.getString("sex"), result.getString("email"), result.getString("position"), result.getDate("date"));
                listData.add(employeeData);
            }

        } catch (Exception e) {
           e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<EmployeeData> addEmployeeList;

    private FilteredList<EmployeeData> filter;

    public void dashboardShowListData(){
        addEmployeeList = addEmployeeListData();
        filter = new FilteredList<>(addEmployeeList, p -> true);

        dashboard_employeeid.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        dashboard_firstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        dashboard_lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        dashboard_sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        dashboard_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        dashboard_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        dashboard_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        dashboard_table.setItems(filter);

        dashboard_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(employee -> {
                if(newValue == null || newValue.isEmpty()) return true;

                String searchKey = newValue.toLowerCase();

                if(employee.getEmployeeID().toString().contains(searchKey)){
                    return true;
                }else if(employee.getFirstname().toLowerCase().contains(searchKey)){
                    return true;
                }else if(employee.getLastname().toLowerCase().contains(searchKey)){
                    return true;
                }else if(employee.getSex().toLowerCase().contains(searchKey)){
                    return true;
                }else if(employee.getEmail().toLowerCase().contains(searchKey)){
                    return true;
                }else if(employee.getPosition().toLowerCase().contains(searchKey)){
                    return true;
                }else if(employee.getDate().toString().contains(searchKey)){
                    return true;
                }
                return false;
            });
        });
    }

    public void addEmployeeShowListData(){
        addEmployeeList = addEmployeeListData();
        filter = new FilteredList<>(addEmployeeList, p -> true);

        employeeid_column.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        firstname_column.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastname_column.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        sex_column.setCellValueFactory(new PropertyValueFactory<>("sex"));
        email_column.setCellValueFactory(new PropertyValueFactory<>("email"));
        position_column.setCellValueFactory(new PropertyValueFactory<>("position"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("date"));

        employeeTable.setItems(filter);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(employee -> {
                if(newValue == null || newValue.isEmpty()) return true;

                String searchKey = newValue.toLowerCase();

                if(employee.getEmployeeID().toString().contains(searchKey)){
                    return true;
                }else if(employee.getFirstname().toLowerCase().contains(searchKey)){
                    return true;
                }else if(employee.getLastname().toLowerCase().contains(searchKey)){
                    return true;
                }else if(employee.getSex().toLowerCase().contains(searchKey)){
                    return true;
                }else if(employee.getEmail().toLowerCase().contains(searchKey)){
                    return true;
                }else if(employee.getPosition().toLowerCase().contains(searchKey)){
                    return true;
                }
                return false;
            });
        });
    }

    @FXML
    private void employeeSelect(MouseEvent event){
        EmployeeData employeeData = employeeTable.getSelectionModel().getSelectedItem();
        int num = employeeTable.getSelectionModel().getSelectedIndex();

        if((num -1) < -1) return;

        employeeidTextField.setText(String.valueOf(employeeData.getEmployeeID()));
        firstnameTextField.setText(String.valueOf(employeeData.getFirstname()));
        lastnameTextField.setText(String.valueOf(employeeData.getLastname()));
        chooseSex.setValue(String.valueOf(employeeData.getSex()));
        emailTextField.setText(String.valueOf(employeeData.getEmail()));
        choosePosition.setValue(String.valueOf(employeeData.getPosition()));
    }

    @FXML
    private void clearEmployee(ActionEvent event){
        employeeidTextField.setText("");
        firstnameTextField.setText("");
        lastnameTextField.setText("");
        chooseSex.setStyle("-fx-border-color: #616161;");
        emailTextField.setText("");
        choosePosition.setStyle("-fx-border-color: #616161;");
    }

    @FXML
    private void addEmployee(ActionEvent event){
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO employeedata (employeeid, firstname, lastname, sex, email, position, date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        connect = DBConnection.connect();

        try {
            if(employeeidTextField.getText().isEmpty() || firstnameTextField.getText().isEmpty() || lastnameTextField.getText().isEmpty() || chooseSex.getSelectionModel().getSelectedItem() == null || emailTextField.getText().isEmpty() || choosePosition.getSelectionModel().getSelectedItem() == null){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the blank fields");
                alert.showAndWait();
            }else{
                try {
                    Integer.parseInt(employeeidTextField.getText());
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee ID must be a number");
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

                if(employeeidTextField.getText().equals("0")){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid ID");
                    alert.showAndWait();
                    return;
                }

                String checkErrorEmail = "SELECT email FROM employeedata WHERE email = '" + emailTextField.getText() + "'";
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

                String checkErrorID = "SELECT employeeid FROM employeedata WHERE employeeid = '" + employeeidTextField.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkErrorID);

                if(result.next()){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee ID: " + employeeidTextField.getText() + " already existed");
                    alert.showAndWait();
                }else{
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, employeeidTextField.getText());
                    prepare.setString(2, firstnameTextField.getText());
                    prepare.setString(3, lastnameTextField.getText());
                    prepare.setString(4, (String)chooseSex.getSelectionModel().getSelectedItem());
                    prepare.setString(5, emailTextField.getText());
                    prepare.setString(6, (String)choosePosition.getSelectionModel().getSelectedItem());
                    prepare.setString(7, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    Alert alert = new Alert(AlertType.INFORMATION);

                    alert.setTitle("Success message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee added successfully");
                    alert.showAndWait();
                    addEmployeeShowListData();
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    @FXML
    private void updateEmployee(ActionEvent event){
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "UPDATE employeedata SET firstname = ?, lastname = ?, sex = ?, email = ?, position = ?, date = ? WHERE employeeid = '" + employeeidTextField.getText() + "'";
        
        connect = DBConnection.connect();

        try {
            if(employeeidTextField.getText().isEmpty() || firstnameTextField.getText().isEmpty() || lastnameTextField.getText().isEmpty() || chooseSex.getSelectionModel().getSelectedItem() == null || emailTextField.getText().isEmpty() || choosePosition.getSelectionModel().getSelectedItem() == null){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the blank fields");
                alert.showAndWait();
            }else{
                try {
                    Integer.parseInt(employeeidTextField.getText());
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee ID must be a number");
                    alert.showAndWait();
                    return;
                }

                String checkID = "SELECT employeeid FROM employeedata WHERE employeeid = ?";
                
                    prepare = connect.prepareStatement(checkID);
                    prepare.setInt(1, Integer.parseInt(employeeidTextField.getText()));
                    result = prepare.executeQuery();

                if(!result.next()){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee ID: " + employeeidTextField.getText() + " doesn't exist");
                    alert.showAndWait();
                    return;
                }

                if(!EMAIL_PATTERN.matcher(emailTextField.getText()).matches()){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error message");
                    alert.setHeaderText(null);
                    alert.setContentText("\t\t\t\tInvalid email\n\nPlease put an @ in your email (ex. name@example.com)");
                    alert.showAndWait();
                    return;
                }

                if(employeeidTextField.getText().equals("0")){
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
                    alert.setContentText("Are you sure want to update Employee ID: " + employeeidTextField.getText() + "?");
                    Optional<ButtonType> option = alert.showAndWait();
                    if(option.get().equals(ButtonType.OK)){
                        prepare = connect.prepareStatement(sql);
                        prepare.setString(1, firstnameTextField.getText());
                        prepare.setString(2, lastnameTextField.getText());
                        prepare.setString(3, (String)chooseSex.getSelectionModel().getSelectedItem());
                        prepare.setString(4, emailTextField.getText());
                        prepare.setString(5, (String)choosePosition.getSelectionModel().getSelectedItem());
                        prepare.setString(6, String.valueOf(sqlDate));
                        prepare.executeUpdate();                        
                        addEmployeeShowListData();
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteEmployee(){
        String sql = "DELETE FROM employeedata WHERE employeeid = ?";

        connect = DBConnection.connect();
        try {
            if(employeeidTextField.getText().isEmpty() || firstnameTextField.getText().isEmpty() || lastnameTextField.getText().isEmpty() || chooseSex.getSelectionModel().getSelectedItem() == null || emailTextField.getText().isEmpty() || choosePosition.getSelectionModel().getSelectedItem() == null){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the blank fields");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(AlertType.CONFIRMATION);

                alert.setTitle("Confirmation message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure want to delete Employee ID: " + employeeidTextField.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, employeeidTextField.getText());
                    prepare.executeUpdate();
                    addEmployeeShowListData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnClose(ActionEvent event){
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
    private void btnMinimize(ActionEvent event){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void editProfile(){
        dashboard_form.setVisible(false);
        employeetable_form.setVisible(false);
        edit_form.setVisible(true);
        dashboardbtn.setStyle("-fx-background-color: transparent;");
        employeetablebtn.setStyle("-fx-background-color: transparent;");

        String sql = "SELECT * FROM admin WHERE id = ?";

        connect = DBConnection.connect();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Session.getUserID());
            result = prepare.executeQuery();

            while (result.next()) {
                display_edit_name.setText(result.getString("name"));
                display_edit_email.setText(result.getString("email"));
                edit_email.setText(result.getString("email"));
                edit_username.setText(result.getString("username"));
                edit_name.setText(result.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnSettiings(){

        String sql = "SELECT role FROM admin WHERE role = ?";

        connect = DBConnection.connect();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Session.getRole());
            result = prepare.executeQuery();
            
            if(result.next()){
                String getRole = result.getString("role");

                if(!"Super Admin".equals(getRole)){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Permission error");
                    alert.setHeaderText(null);
                    alert.setContentText("You don't have permission to proceed this feature");
                    alert.showAndWait();
                }else{
                    settingsbtn.getScene().getWindow().hide();
                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("/views/settings_layout.fxml"));
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

    private void countOfEmployee(){
        String sql = "SELECT COUNT(id) FROM employeedata";

        connect = DBConnection.connect();
        int countData = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while(result.next()){
                countData = result.getInt("COUNT(id)");
            }

            totalEmployee.setText(String.valueOf(countData));

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

    private void countOfUser(){
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

    private void dashbordDetails(){
        String sql = "SELECT * FROM admin WHERE id = ?";

        connect = DBConnection.connect();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, Session.getUserID());
            result = prepare.executeQuery();

           while (result.next()) {
                detailsName.setText(result.getString("name"));
                detailsEmail.setText(result.getString("email"));
                detailsRole.setText(result.getString("role"));
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void countOfMale(){
        String sql = "SELECT COUNT(sex) FROM employeedata WHERE sex = 'Male'";

        connect = DBConnection.connect();
        int countData = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countData = result.getInt("COUNT(sex)");
            }
            maleDisplay.setText(String.valueOf(countData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void countOfFemale(){
        String sql = "SELECT COUNT(sex) FROM employeedata WHERE sex = 'Female'";
        
        connect = DBConnection.connect();
        int countData = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                countData = result.getInt("COUNT(sex)");
            }

            femaleDisplay.setText(String.valueOf(countData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnDashboard(){
        countOfFemale();
        countOfMale();
        dashboardShowListData();
        countOfUser();
        countOfSuperAdmin();
        countOfAdmin();
        countOfEmployee();
        dashbordDetails();
        employeetable_form.setVisible(false);
        dashboard_form.setVisible(true);
        edit_form.setVisible(false);
        employeetablebtn.setStyle("-fx-background-color: transparent;");
        dashboardbtn.setStyle("-fx-background-color: #03AED2; -fx-background-radius: 8px; -fx-text-fill: #ffff;");
    }

    @FXML
    private void btnEmployeetable(){
        String sql = "SELECT role FROM admin WHERE role = ?";

        connect = DBConnection.connect();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Session.getRole());
            result = prepare.executeQuery();

            if(result.next()){
                String getRole = result.getString("role");

                if("Admin".equals(getRole) || "Super Admin".equals(getRole)){
                    dashboard_form.setVisible(false);
                    edit_form.setVisible(false);
                    employeetable_form.setVisible(true);
                    dashboardbtn.setStyle("-fx-background-color: transparent;");
                    employeetablebtn.setStyle("-fx-background-color: #03AED2; -fx-background-radius: 8px; -fx-text-fill: #ffff;");
                    addEmployeeShowListData();
                }else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Permission Error");
                    alert.setHeaderText(null);
                    alert.setContentText("You don't have permission to proceed this feature");
                    alert.showAndWait();
                }
            }

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
                sideEmail.setText(result.getString("email"));
                sideRole.setText(result.getString("role"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        countOfFemale();
        countOfMale();
        countOfUser();
        countOfAdmin();
        countOfSuperAdmin();
        dashbordDetails();
        countOfEmployee();
        addEmployeeShowListData();
        dashboardShowListData();
        ObservableList<String> sexList = FXCollections.observableArrayList("Male", "Female");
        chooseSex.setItems(sexList);

        ObservableList<String> positionList = FXCollections.observableArrayList("Cyber Security Engineer", "Cyber Security Analyst", "Cloud Security Analyst", "Software Engineer", "Network Engineer", "Full Stack Web Developer", "Data Analyst", "Janitor");
        choosePosition.setItems(positionList);
    }

   /*EDIT PROFILE CONTROLLER */

   @FXML
   private AnchorPane edit_form;

   @FXML
   private TextField edit_name;

   @FXML
   private TextField edit_username;

   @FXML
   private TextField edit_email;

   @FXML
   private PasswordField edit_password;

   @FXML
   private PasswordField edit_retype_password;

   @FXML
   private Text display_edit_name;

   @FXML
   private Text display_edit_email;

   @FXML
   private void btnUpdateProfile(){
        String sql = "UPDATE admin SET name = ?, username = ?, email = ?, password = ? WHERE id = ?";

        connect = DBConnection.connect();
        try {
            if(edit_name.getText().isEmpty() || edit_username.getText().isEmpty() || edit_password.getText().isEmpty() || edit_retype_password.getText().isEmpty()){
                showAlert(AlertType.ERROR, "Error message", "Please fill the blank fields");
                return;
            }

            String checkUsername = "SELECT username FROM admin WHERE username = ?";

            prepare = connect.prepareStatement(checkUsername);
            prepare.setString(1, edit_username.getText());
            result = prepare.executeQuery();

            if(result.next()){
               if(!edit_username.getText().equals(Session.getUsername())){
                    showAlert(AlertType.ERROR, "Error message", "Username: " + edit_username.getText() + " already taken.");
                    return;
               }
               
            }

            if(edit_password.getText().length() < 6){
                showAlert(AlertType.ERROR, "Error message", "Password must be atleast 6 characters.");
                return;
            }

            if(!edit_password.getText().equals(edit_retype_password.getText())){
                showAlert(AlertType.ERROR, "Error message", "Password doesn't match");
                return;
            }

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure want to update your profile?");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){
                String encryptPassword = PasswordHash.password_hash(edit_password.getText());
    
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, edit_name.getText());
                prepare.setString(2, edit_username.getText());
                prepare.setString(3, edit_email.getText());
                prepare.setString(4, encryptPassword);
                prepare.setInt(5, Session.getUserID());
                prepare.executeUpdate();
            }

            showAlert(AlertType.INFORMATION, "Success", "Profile updated successfully.");

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

        } catch (Exception e) {
            e.printStackTrace();
        }
   }

   private void showAlert(AlertType alertType, String title, String contentText){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
   }

    
}