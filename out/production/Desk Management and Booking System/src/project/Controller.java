package project;

import animatefx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable
{

    @FXML
    private AnchorPane addDeskDialog, removeDeskDialog, addStaffDialog, removeStaffDialog, checkDeskDialog, makeBiddingDialogue;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private AnchorPane createBookingPane, showBookingPane;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private AnchorPane adminPane, deskPane, staffPane, dashboardPane, bookingPane;

    @FXML
    private AnchorPane userPane, bidPane, userDashboardPane;

    @FXML
    private AnchorPane checkStaffDialog;

    private Button currentButtonPane;

    @FXML
    private Label staffInfoUsername, staffInfoUsertype, leaveStatusLabel, bookedDesk;

    @FXML
    private Label checkStaffUsername, checkStaffUsertype, checkStaffLeaveStatus, checkStaffBookings;

    @FXML
    private Label usernameLabel, usernameLabel1;

    @FXML
    private Label hourTrendLabel, dayTrendLabel, weekTrendLabel, userHourTrendLabel, userDayTrendLabel, userWeekTrendLabel;

    @FXML
    private Label totalUsersLabel, totalDesksLabel;

    @FXML
    private Label deskNumber, deskReservedStatus, deskBookingStatus, bookingDurationLabel, showBookingDeskId;

    @FXML
    private Label adminEmailLabel, adminNameLabel, userEmailLabel, userNameLabel;

    @FXML
    private Button confirmLogin, downloadReceipt;

    @FXML
    private ToggleButton hourBookingBtn, dayBookingBtn, weekBookingBtn;

    @FXML
    private ToggleButton hourViewingBtn, dayViewingBtn, weekViewingBtn;

    @FXML
    private ToggleButton userHourViewingBtn, userDayViewingBtn, userWeekViewingBtn;

    @FXML
    private ToggleButton adminFreeViewingBtn, adminBookedViewingBtn, userFreeViewingBtn, userBookedViewingBtn;

    @FXML
    private Button makeBookingBtn, cancelBookingBtn;

    @FXML
    private Button dashboardBtn, staffBtn, deskBtn, bookingsBtn, logOutBtn;

    @FXML
    private Button userBookingBtn, userDashboardBtn, userLogOutBtn;

    @FXML
    private Button cancelAddDesk, confirmAddDesk, cancelNewStaff, addNewStaff;

    @FXML
    private Button confirmRemoveDesk, cancelRemoveDesk, confirmRemoveStaff, cancelRemoveStaff;

    @FXML
    private Button searchStaffBtn, checkStaffBackBtn, checkStaffBtn, checkDeskBackBtn;

    @FXML
    private Button reserveDeskBtn, deskCancelBookingBtn, searchDeskBtn;

    @FXML
    private ImageView rightArrowBtn, leftArrowBtn, splashImage;

    @FXML
    private TextField bookingDurationField, searchDeskId;

    @FXML
    private TextField usernameLogin;

    @FXML
    private TextField addDeskField, usernameField, removeDeskField, removeStaffName, checkUserSearchField, emailField;

    @FXML
    private PasswordField userPasswordField, confirmUserPassword, passwordLogin;

    @FXML
    private TableView<Booking> bookingTable;

    @FXML
    private TableView<Desk> dashboardBookingTable, userDashboardBookingTable;

    @FXML
    private TableColumn<Booking, String> staffBookedColumn, deskNumberColumn, startDateColumn, bookingDurationColumn;

    @FXML
    private TableColumn<Desk, String> adminDeskIdColumn, userDeskIdColumn, adminDurationColumn, userDurationColumn;

    @FXML
    private ComboBox<UserType> usertypeComboBox;

    @FXML
    private ComboBox<Desk> availableDesksCombo;

    @FXML
    private CheckBox showPasswordCheckBox;


    private static AnchorPane main;
    private static ImageView splash;

    private double xOffset;
    private double yOffset;

    private User currentUser;

    private Tooltip passwordTooltip;
    private Tooltip adminDashboardTooltip, adminDeskTooltip, adminStaffTooltip, adminBookingTooltip, adminLogoutTooltip;
    private Tooltip userDashboardTooltip, userLogoutTooltip, userBookingTooltip;
    private Tooltip adminNameTooltip, adminEmailTooltip, userNameTooltip, userEmailTooltip;
    private SimpleBooleanProperty showPassword;
    private ToggleGroup toggleGroup, toggleViewGroup, userToggleViewGroup;

    public Controller()
    {
        this.xOffset = 0;
        this.yOffset = 0;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        boolean validated = Loader.validateHomeFolder();
        boolean loaded = Loader.loadDataFile();
        if(!validated || !loaded)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Desk Management and Booking System");
            alert.setHeaderText("Database Error");
            alert.setContentText(Loader.getErrorMessage());
            alert.show();
            quit(null);
        }

        createTooltips();
        setLoginDetails();
        addAdminButtonAction();

        this.rootPane.setOnMousePressed(mouseEvent ->
        {
            this.xOffset = Main.mainStage.getX() - mouseEvent.getScreenX();
            this.yOffset = Main.mainStage.getY() - mouseEvent.getScreenY();
        });

        this.rootPane.setOnMouseDragged(mouseEvent ->
        {
            Main.mainStage.setX(mouseEvent.getScreenX() + this.xOffset);
            Main.mainStage.setY(mouseEvent.getScreenY() + this.yOffset);
        });

        main = this.loginPane;
        splash = this.splashImage;

        setToggleGroups();
        addAdminDialogs();
        addUserDialogs();
        addAdminButtonAction();

        playAnimation();
    }

    private void setLoginDetails()
    {
        this.showPassword = new SimpleBooleanProperty();
        this.showPassword.addListener((observableValue, oldValue, newValue) ->
        {
            if(newValue)
                showTooltip(this.passwordTooltip, this.passwordLogin.getText(), this.passwordLogin);
            else
                hideTooltip(this.passwordTooltip);
        });

        showPassword.bind(this.showPasswordCheckBox.selectedProperty());

        this.passwordLogin.setOnKeyTyped(keyEvent ->
        {
            if(showPassword.get())
                showTooltip(this.passwordTooltip, this.passwordLogin.getText(), this.passwordLogin);
        });

        this.confirmLogin.setOnMouseClicked(event ->
        {
            String email = this.usernameLogin.getText().trim();
            String password = this.passwordLogin.getText().trim();
            this.currentUser = User.getCurrentUser(email, password);
            if(this.currentUser != null)
            {
                this.usernameLogin.clear();
                this.passwordLogin.clear();
                this.showPasswordCheckBox.setSelected(false);

                if(this.currentUser.getUserType() == UserType.Administrator)
                {
                    this.usernameLabel.setText("Hello " + this.currentUser.getUsername() + ",");
                    this.adminPane.toFront();
                    this.adminPane.requestFocus();
                    this.adminNameLabel.setText(this.currentUser.getUsername());
                    this.adminEmailLabel.setText(this.currentUser.getEmailAddress());
                    completeArrowFunction();
                    currentButtonPane = this.dashboardBtn;
                }
                else
                {
                    this.usernameLabel1.setText("Hello " + this.currentUser.getUsername() + ",");
                    this.userPane.toFront();
                    this.userPane.requestFocus();
                    this.userNameLabel.setText(this.currentUser.getUsername());
                    this.userEmailLabel.setText(this.currentUser.getEmailAddress());
                    validateBookingInfo(this.currentUser);
                    currentButtonPane = this.userDashboardBtn;
                }
                populateBookingsTable();
                refreshDashboards();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("User Login");
                alert.setContentText("Invalid login details");
                alert.show();
            }
        });
    }

    private void createTooltips()
    {
        this.passwordTooltip = new Tooltip();
        this.passwordTooltip.setShowDelay(Duration.ZERO);
        this.passwordTooltip.setAutoHide(false);
        this.passwordTooltip.setMinWidth(50);

        this.adminDashboardTooltip = new Tooltip();
        this.adminDashboardTooltip.setShowDelay(Duration.ZERO);
        this.adminDashboardTooltip.setAutoHide(true);
        this.adminDashboardTooltip.setMinWidth(50);

        this.adminDeskTooltip = new Tooltip();
        this.adminDeskTooltip.setShowDelay(Duration.ZERO);
        this.adminDeskTooltip.setAutoHide(true);
        this.adminDeskTooltip.setMinWidth(50);

        this.adminStaffTooltip = new Tooltip();
        this.adminStaffTooltip.setShowDelay(Duration.ZERO);
        this.adminStaffTooltip.setAutoHide(true);
        this.adminStaffTooltip.setMinWidth(50);

        this.adminBookingTooltip = new Tooltip();
        this.adminBookingTooltip.setShowDelay(Duration.ZERO);
        this.adminBookingTooltip.setAutoHide(true);
        this.adminBookingTooltip.setMinWidth(50);

        this.adminLogoutTooltip = new Tooltip();
        this.adminLogoutTooltip.setShowDelay(Duration.ZERO);
        this.adminLogoutTooltip.setAutoHide(true);
        this.adminLogoutTooltip.setMinWidth(50);

        this.userDashboardTooltip = new Tooltip();
        this.userDashboardTooltip.setShowDelay(Duration.ZERO);
        this.userDashboardTooltip.setAutoHide(true);
        this.userDashboardTooltip.setMinWidth(50);

        this.userBookingTooltip = new Tooltip();
        this.userBookingTooltip.setShowDelay(Duration.ZERO);
        this.userBookingTooltip.setAutoHide(true);
        this.userBookingTooltip.setMinWidth(50);

        this.userLogoutTooltip = new Tooltip();
        this.userLogoutTooltip.setShowDelay(Duration.ZERO);
        this.userLogoutTooltip.setAutoHide(true);
        this.userLogoutTooltip.setMinWidth(50);

        this.adminNameTooltip = new Tooltip();
        this.adminNameTooltip.setShowDelay(Duration.ZERO);
        this.adminNameTooltip.setAutoHide(true);
        this.adminNameTooltip.setMinWidth(50);

        this.adminEmailTooltip = new Tooltip();
        this.adminEmailTooltip.setShowDelay(Duration.ZERO);
        this.adminEmailTooltip.setAutoHide(true);
        this.adminEmailTooltip.setMinWidth(50);

        this.userNameTooltip = new Tooltip();
        this.userNameTooltip.setShowDelay(Duration.ZERO);
        this.userNameTooltip.setAutoHide(true);
        this.userNameTooltip.setMinWidth(50);

        this.userEmailTooltip = new Tooltip();
        this.userEmailTooltip.setShowDelay(Duration.ZERO);
        this.userEmailTooltip.setAutoHide(true);
        this.userEmailTooltip.setMinWidth(50);
    }

    private void hideTooltip(Tooltip tooltip)
    {
        tooltip.setText("");
        tooltip.hide();
    }

    private void showTooltip(Tooltip tooltip, String message, Node node)
    {
        Point2D point2D = node.localToScene(node.getBoundsInLocal().getMaxX(),
                node.getBoundsInLocal().getMaxY());
        tooltip.setText(message);
        tooltip.show(this.passwordLogin,
                point2D.getX() + Main.mainStage.getScene().getX() + Main.mainStage.getX(),
                point2D.getY() + Main.mainStage.getScene().getY() + Main.mainStage.getY());
    }

    private void repopulateTables()
    {
        List<Desk> desks = Desk.getBookings(Desk.HOUR_VIEW, true);
        this.userDashboardBookingTable.getItems().setAll(desks);
        this.dashboardBookingTable.getItems().setAll(desks);

        this.userDashboardBookingTable.refresh();
        this.dashboardBookingTable.refresh();
    }

    private void populateBookingsTable()
    {
        this.userDeskIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        this.userDurationColumn.setCellValueFactory(new PropertyValueFactory<>("booking"));
        this.adminDeskIdColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        this.adminDurationColumn.setCellValueFactory(new PropertyValueFactory<>("booking"));

        this.staffBookedColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        this.deskNumberColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        this.bookingDurationColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        List<User> users = User.getAllUsers();
        for(User user : users)
        {
            if(user.getUserType() == UserType.User)
            {
                Booking booking = user.getBooking();
                if(!booking.isEmpty())
                    bookings.add(booking);
            }
        }
        this.bookingTable.getItems().setAll(bookings);

        repopulateTables();

        this.bookingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.dashboardBookingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.userDashboardBookingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableView.TableViewSelectionModel<Booking> selectionModel = this.bookingTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
    }

    private void validateBookingInfo(User currentUser)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Desk Management and Booking System");
        alert.setHeaderText("ACME Staff Manager");

        if(currentUser.alertOnLoad())
        {
            alert.setContentText("Dear " + currentUser.getUsername() +
                    ", your booking has expired or has been canceled by the administrator!");
            alert.show();
            currentUser.setAlertOnLoad(false);
            this.createBookingPane.setVisible(true);
            this.showBookingPane.setVisible(false);
            this.createBookingPane.toFront();
            this.userDashboardPane.toFront();
            return;
        }

        Booking booking = currentUser.getBooking();
        if(booking.isEmpty())
        {
            this.createBookingPane.setVisible(true);
            this.showBookingPane.setVisible(false);
            this.createBookingPane.toFront();
            this.userDashboardPane.toFront();
            return;
        }

        long currentMillis = System.currentTimeMillis();
        long seconds = DateUtility.getSecondsLeft(currentMillis, booking);
        long timeDifference = DateUtility.getTimeDifference(currentMillis, booking.getStartDate().getTime());

        if(!currentUser.isAttending())
        {
            if(timeDifference > (2 * DateUtility.SECONDS_IN_HOUR))
            {
                alert.setContentText("Dear " + currentUser.getUsername() +
                        ", your booking has expired because you did not sign in within 2 hours!");
                Desk.cancelBooking(booking);
                currentUser.isAttending(false);
                this.currentUser.setBooking(new Booking());
                this.bookingTable.getItems().removeAll(booking);
                this.bookingTable.refresh();
                this.showBookingPane.setVisible(false);
                this.showBookingDeskId.setText("");
                this.bookingDurationLabel.setText("");
                this.createBookingPane.setVisible(true);
                this.createBookingPane.toFront();
            }
            else
            {
                currentUser.isAttending(true);
                alert.setContentText("Dear " + currentUser.getUsername() + ", you have " +
                        DateUtility.formatDate(seconds) + "left! Your attendance has been noted!");
                this.createBookingPane.setVisible(false);
                this.showBookingPane.setVisible(true);
                this.showBookingDeskId.setText("Desk " + booking.getId() + " of " + Desk.getNumberOfDesks());
                this.bookingDurationLabel.setText(booking.getStartTime());
                this.showBookingPane.toFront();
            }
        }
        else
        {
            if(seconds <= 0 )
            {
                Desk.cancelBooking(booking);
                currentUser.isAttending(false);
                this.currentUser.setBooking(new Booking());
                this.bookingTable.getItems().removeAll(booking);
                this.bookingTable.refresh();
                alert.setContentText("Dear " + currentUser.getUsername() +
                        ", your booking has expired or has been canceled by the administrator!");
                this.showBookingPane.setVisible(false);
                this.showBookingDeskId.setText("");
                this.bookingDurationLabel.setText("");
                this.createBookingPane.setVisible(true);
                this.createBookingPane.toFront();
            }
            else
            {
                alert.setContentText("Dear " + currentUser.getUsername() + ", you have " +
                        DateUtility.formatDate(seconds) + "left!");
                currentUser.isAttending(true);
                this.createBookingPane.setVisible(false);
                this.showBookingPane.setVisible(true);
                this.showBookingDeskId.setText("Desk " + booking.getId() + " of " + Desk.getNumberOfDesks());
                this.bookingDurationLabel.setText(booking.getStartTime());
                this.showBookingPane.toFront();
            }
        }
        alert.show();
        Loader.saveDataFile();
    }

    private void addAdminButtonAction()
    {
        this.logOutBtn.setOnMouseClicked(event ->
        {
            Loader.saveDataFile();
            this.currentUser = null;
            this.adminPane.toBack();
            this.loginPane.toFront();
            this.usernameLogin.toFront();
            this.passwordLogin.toFront();
            this.confirmLogin.toFront();
            event.consume();
        });

        this.dashboardBtn.setOnMouseClicked(event ->
        {
            switchButtonAndPane(this.dashboardBtn);
            event.consume();
        });

        this.deskBtn.setOnMouseClicked(event ->
        {
            switchButtonAndPane(this.deskBtn);
            event.consume();
        });

        this.staffBtn.setOnMouseClicked(event ->
        {
            switchButtonAndPane(this.staffBtn);
            event.consume();
        });

        this.bookingsBtn.setOnMouseClicked(event ->
        {
            switchButtonAndPane(this.bookingsBtn);
            event.consume();
        });

        this.dayViewingBtn.setOnMouseClicked(event ->
        {
            this.hourViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.dayViewingBtn.setStyle("-fx-background-color: #202020;");
            this.weekViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.dayViewingBtn.setSelected(true);
            event.consume();
        });

        this.hourViewingBtn.setOnMouseClicked(event ->
        {
            this.hourViewingBtn.setStyle("-fx-background-color: #202020; -fx-background-radius: 15 0 0 0;");
            this.dayViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.weekViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.hourViewingBtn.setSelected(true);
            event.consume();
        });

        this.weekViewingBtn.setOnMouseClicked(event ->
        {
            this.hourViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.dayViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.weekViewingBtn.setStyle("-fx-background-color: #202020; -fx-background-radius: 0 15 0 0;");
            this.weekViewingBtn.setSelected(true);
            event.consume();
        });

        this.adminFreeViewingBtn.setOnMouseClicked(event ->
        {
            this.adminFreeViewingBtn.setStyle("-fx-background-color: #202020;");
            this.adminBookedViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.adminFreeViewingBtn.setSelected(true);
            ToggleButton selected = (ToggleButton) this.toggleViewGroup.getSelectedToggle();
            if(selected.equals(this.hourViewingBtn))
            {
                this.dashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.HOUR_VIEW, true));
                this.dashboardBookingTable.refresh();
            }
            else if(selected.equals(this.dayViewingBtn))
            {
                this.dashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.DAY_VIEW, true));
                this.dashboardBookingTable.refresh();
            }
            else
            {
                this.dashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.WEEK_VIEW, true));
                this.dashboardBookingTable.refresh();
            }
            event.consume();
        });

        this.adminBookedViewingBtn.setOnMouseClicked(event ->
        {
           this.adminBookedViewingBtn.setStyle("-fx-background-color: #202020;");
           this.adminFreeViewingBtn.setStyle("-fx-background-color: #00000000;");
           this.adminBookedViewingBtn.setSelected(true);
            ToggleButton selected = (ToggleButton) this.toggleViewGroup.getSelectedToggle();
            if(selected.equals(this.hourViewingBtn))
            {
                this.dashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.HOUR_VIEW, false));
                this.dashboardBookingTable.refresh();
            }
            else if(selected.equals(this.dayViewingBtn))
            {
                this.dashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.DAY_VIEW, false));
                this.dashboardBookingTable.refresh();
            }
            else
            {
                this.dashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.WEEK_VIEW, false));
                this.dashboardBookingTable.refresh();
            }
           event.consume();
        });

        this.searchDeskBtn.setOnMouseClicked(event ->
        {
            try
            {
                String idText = this.searchDeskId.getText();
                if(idText.isEmpty())
                    throw new Exception("invalid details provided!");
                int id = Integer.parseInt(idText);
                if(id < 1)
                    throw new Exception("invalid details provided!");

                if(id > Desk.getNumberOfDesks())
                    throw new Exception("trying to access more than the desks available!");

                this.searchDeskId.clear();

                Desk.DESK_POINTER = id - 1;
                completeArrowFunction();
            }
            catch (Exception ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("ACME Staff Manager");
                alert.setContentText("An error occurred due to " + ex.getMessage());
                alert.show();
            }
        });

    }

    private void setToggleGroups()
    {
        this.toggleGroup = new ToggleGroup();
        this.dayBookingBtn.setToggleGroup(this.toggleGroup);
        this.hourBookingBtn.setToggleGroup(this.toggleGroup);
        this.weekBookingBtn.setToggleGroup(this.toggleGroup);

        this.toggleViewGroup = new ToggleGroup();
        this.dayViewingBtn.setToggleGroup(this.toggleViewGroup);
        this.hourViewingBtn.setToggleGroup(this.toggleViewGroup);
        this.weekViewingBtn.setToggleGroup(this.toggleViewGroup);

        this.userToggleViewGroup = new ToggleGroup();
        this.userHourViewingBtn.setToggleGroup(this.userToggleViewGroup);
        this.userDayViewingBtn.setToggleGroup(this.userToggleViewGroup);
        this.userWeekViewingBtn.setToggleGroup(this.userToggleViewGroup);
    }

    private void addAdminDialogs()
    {
        this.usertypeComboBox.getItems().addAll(UserType.User, UserType.Administrator);

        this.cancelAddDesk.setOnMouseClicked(event ->
        {
            this.addDeskField.clear();
            this.addDeskDialog.toBack();
            this.deskPane.toFront();
            event.consume();
        });

        this.cancelNewStaff.setOnMouseClicked(event ->
        {
            this.usernameLogin.clear();
            this.userPasswordField.clear();
            this.confirmUserPassword.clear();
            this.addStaffDialog.toBack();
            this.staffPane.toFront();
            event.consume();
        });

        this.cancelRemoveDesk.setOnMouseClicked(event ->
        {
            this.removeDeskField.clear();
            this.removeDeskDialog.toBack();
            this.deskPane.toFront();
            event.consume();
        });

        this.cancelRemoveStaff.setOnMouseClicked(event ->
        {
            this.removeStaffName.clear();
            this.leaveStatusLabel.setText("");
            this.bookedDesk.setText("");
            this.staffInfoUsertype.setText("");
            this.staffInfoUsertype.setText("");
            this.removeStaffDialog.toBack();
            this.staffPane.toFront();
            event.consume();
        });

        this.checkStaffBackBtn.setOnMouseClicked(event ->
        {
            this.checkUserSearchField.clear();
            this.checkStaffUsername.setText("");
            this.checkStaffUsertype.setText("");
            this.checkStaffLeaveStatus.setText("");
            this.checkStaffBookings.setText("");
            this.checkStaffDialog.toBack();
            this.staffPane.toFront();
            event.consume();
        });

        this.confirmAddDesk.setOnMouseClicked(event ->
        {
            try
            {
                int numberOfDesks = Integer.parseInt(this.addDeskField.getText().trim());
                Desk.addOrRemoveDesk(numberOfDesks, Desk.ADD_DESK);
                this.addDeskField.clear();
                this.addDeskDialog.toBack();
                this.deskPane.toFront();
                refreshDashboards();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("Atrium Desk Manager");
                alert.setContentText(numberOfDesks + ((numberOfDesks == 1) ? " desk has " : " desks have ") +
                        "been added to the database successfully!");
                alert.show();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("Atrium Desk Manager");
                alert.setContentText("An error occurred due to " + e.getMessage());
                alert.show();
            }
        });

        this.addNewStaff.setOnMouseClicked(event ->
        {
            try
            {
                String username = this.usernameField.getText().trim();
                String password = this.userPasswordField.getText();
                String confirmPassword = this.confirmUserPassword.getText();
                String email = this.emailField.getText().trim();
                if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty())
                    throw new Exception("one or more of the fields being empty");
                if(!password.equals(confirmPassword))
                    throw new Exception("the two passwords not matching!");
                UserType userType = this.usertypeComboBox.getValue();
                if(userType == null)
                    throw new Exception("the user type not selected!");
                User.addUser(username, email, password, userType);
                refreshDashboards();
                this.usernameField.clear();
                this.emailField.clear();
                this.userPasswordField.clear();
                this.confirmUserPassword.clear();
                this.usertypeComboBox.setValue(null);
                this.addStaffDialog.toBack();
                this.staffPane.toFront();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("Atrium Desk Manager");
                alert.setContentText("ACME Staff, " + username + " has been added to the database successfully!");
                alert.show();
            }
            catch(Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("ACME Staff Manager");
                alert.setContentText("An error occurred due to " + e.getMessage());
                alert.show();
            }
        });

        this.confirmRemoveDesk.setOnMouseClicked(event ->
        {
            try
            {
                int numberOfDesks = Integer.parseInt(this.removeDeskField.getText().trim());
                Desk.addOrRemoveDesk(numberOfDesks, Desk.REMOVE_DESK);
                this.removeDeskField.clear();
                this.removeDeskDialog.toBack();
                this.deskPane.toFront();
                refreshDashboards();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("Atrium Desk Manager");
                alert.setContentText(numberOfDesks + ((numberOfDesks == 1) ? " desk has " : " desks have ") +
                        "been removed from the database successfully!");
                alert.show();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("Atrium Desk Manager");
                alert.setContentText("An error occurred due to " + e.getMessage());
                alert.show();
            }
        });

        this.confirmRemoveStaff.setOnMouseClicked(event ->
        {
            try
            {
                String username = this.removeStaffName.getText().trim();
                User user = User.getUser(username);
                if(this.currentUser.equals(user))
                    throw new Exception(" attempting to remove the same user currently logged in from the database!");
                User.removeUser(username);
                this.removeStaffName.clear();
                this.removeStaffDialog.toBack();
                this.staffPane.toFront();
                refreshDashboards();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("Atrium Desk Manager");
                alert.setContentText("User " + username +
                        " has been removed from the database successfully!");
                alert.show();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("Atrium Desk Manager");
                alert.setContentText("An error occurred due to " + e.getMessage());
                alert.show();
            }
        });

        this.checkStaffBtn.setOnMouseClicked(event ->
        {
            try
            {
                String username = this.checkUserSearchField.getText().trim();
                User user = User.getUser(username);
                this.checkStaffUsername.setText(user.getUsername());
                this.checkStaffUsertype.setText(user.getUserType().toString());
                this.checkStaffLeaveStatus.setText((user.isOnLeave() ? "ON LEAVE" : "AVAILABLE"));
                this.checkStaffBookings.setText((!user.getBooking().isEmpty()) ? "DESK " + user.getBooking().getId() : "NONE");
                this.checkUserSearchField.clear();
            }
            catch (Exception ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("ACME Staff Manager");
                alert.setContentText("An error occurred due to " + ex.getMessage());
                alert.show();
            }
        });

        this.searchStaffBtn.setOnMouseClicked(event ->
        {
            try
            {
                String username = this.removeStaffName.getText().trim();
                User user = User.getUser(username);
                this.staffInfoUsername.setText(user.getUsername());
                this.staffInfoUsertype.setText(user.getUserType().toString());
                this.leaveStatusLabel.setText((user.isOnLeave() ? "ON LEAVE" : "AVAILABLE"));
                this.bookedDesk.setText((!user.getBooking().isEmpty()) ? "DESK " + user.getBooking().getId() : "NONE");
                this.removeStaffName.clear();
            }
            catch (Exception ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("ACME Staff Manager");
                alert.setContentText("An error occurred due to " + ex.getMessage());
                alert.show();
            }
        });

        this.rightArrowBtn.setOnMouseClicked(event ->
        {
            Desk.DESK_POINTER = (Desk.DESK_POINTER + 1 >= Desk.getNumberOfDesks()) ? 0 : ++Desk.DESK_POINTER;
            completeArrowFunction();
        });

        this.leftArrowBtn.setOnMouseClicked(event ->
        {
            Desk.DESK_POINTER = (Desk.DESK_POINTER - 1 < 0) ? Desk.getNumberOfDesks() - 1 : --Desk.DESK_POINTER;
            completeArrowFunction();
        });

        this.reserveDeskBtn.setOnMouseClicked(event ->
        {
            Desk currentDesk = Desk.getDesk(Desk.DESK_POINTER);
            if(currentDesk.isReserved())
            {
                this.reserveDeskBtn.setText("Reserve Desk");
                this.deskReservedStatus.setText("NOT RESERVED");
                currentDesk.isReserved(false);
            }
            else
            {
                this.reserveDeskBtn.setText("Release Desk");
                this.deskReservedStatus.setText("RESERVED");
                currentDesk.isReserved(true);
            }
            Loader.saveDataFile();
        });

        this.deskCancelBookingBtn.setOnMouseClicked(event ->
        {
            boolean result = generateDeleteBookingAlert();
            if(result && !this.deskCancelBookingBtn.isDisabled())
            {
                Desk currentDesk = Desk.getDesk(Desk.DESK_POINTER);
                if(!currentDesk.isAvailable())
                {
                    this.deskCancelBookingBtn.setDisable(true);
                    this.deskBookingStatus.setText("AVAILABLE");
                    Booking booking = currentDesk.getBooking();
                    Desk.cancelBooking(booking);
                    try
                    {
                        User user = User.getUser(booking.getUser());
                        user.setBooking(new Booking());
                        user.setAlertOnLoad(true);
                        this.bookingTable.getItems().removeAll(booking);
                        repopulateTables();
                        this.bookingTable.refresh();
                        Loader.saveDataFile();
                    }
                    catch (Exception ex)
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Desk Management and Booking System");
                        alert.setHeaderText("ACME Staff Manager");
                        alert.setContentText("An error occurred due to " + ex.getMessage());
                        alert.show();
                    }
                    refreshDashboards();
                }
            }
        });

        this.checkDeskBackBtn.setOnMouseClicked(event ->
        {
            this.checkDeskDialog.toBack();
            this.deskPane.toFront();
        });

    }

    private boolean generateDeleteBookingAlert()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Desk Management and Booking System");
        alert.setHeaderText("Delete Booking?");
        alert.setContentText("This process is not reversible. Do you want to proceed?");
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.isPresent();
    }

    private void completeArrowFunction()
    {
        Desk currentDesk = Desk.getDesk(Desk.DESK_POINTER);
        this.deskNumber.setText("Desk " + (Desk.DESK_POINTER + 1) + " of " + Desk.getNumberOfDesks());
        if(currentDesk.isReserved())
        {
            this.deskReservedStatus.setText("UN-BOOKABLE");
            this.reserveDeskBtn.setText("Release Desk");
        }
        else
        {
            this.deskReservedStatus.setText("BOOKABLE");
            this.reserveDeskBtn.setText("Reserve Desk");
        }

        if(currentDesk.isAvailable())
        {
            this.deskBookingStatus.setText("AVAILABLE");
            this.deskCancelBookingBtn.setDisable(true);
            this.reserveDeskBtn.setDisable(false);
        }
        else
        {
            this.deskBookingStatus.setText("Booked by "
                    + currentDesk.getBooking().getUser());
            this.deskCancelBookingBtn.setDisable(false);
            this.reserveDeskBtn.setDisable(true);
        }
    }

    private void addUserDialogs()
    {
        this.userDashboardBtn.setOnMouseClicked(event ->
        {
            switchButtonAndPane(this.userDashboardBtn);
            event.consume();
        });

        this.userBookingBtn.setOnMouseClicked(event ->
        {
            switchButtonAndPane(this.userBookingBtn);
            event.consume();
        });

        this.userLogOutBtn.setOnMouseClicked(event ->
        {
            Loader.saveDataFile();
            this.userPane.toBack();
            this.loginPane.toFront();
            this.usernameLogin.toFront();
            this.passwordLogin.toFront();
            this.confirmLogin.toFront();
            this.currentUser = null;
            event.consume();
        });

        this.hourBookingBtn.setOnMouseClicked(event ->
        {
            this.hourBookingBtn.setStyle("-fx-background-color: #202020;");
            this.dayBookingBtn.setStyle("-fx-background-color: #00000000;");
            this.weekBookingBtn.setStyle("-fx-background-color: #00000000;");
            event.consume();
        });

        this.dayBookingBtn.setOnMouseClicked(event ->
        {
            this.hourBookingBtn.setStyle("-fx-background-color: #00000000;");
            this.dayBookingBtn.setStyle("-fx-background-color: #202020;");
            this.weekBookingBtn.setStyle("-fx-background-color: #00000000;");
            event.consume();
        });

        this.weekBookingBtn.setOnMouseClicked(event ->
        {
            this.hourBookingBtn.setStyle("-fx-background-color: #00000000;");
            this.dayBookingBtn.setStyle("-fx-background-color: #00000000;");
            this.weekBookingBtn.setStyle("-fx-background-color: #202020;");
            event.consume();
        });

        this.userHourViewingBtn.setOnMouseClicked(event ->
        {
            this.userHourViewingBtn.setStyle("-fx-background-color: #202020; -fx-background-radius: 15 0 0 0;");
            this.userDayViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.userWeekViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.userHourViewingBtn.setSelected(true);
            event.consume();
        });

        this.userDayViewingBtn.setOnMouseClicked(event ->
        {
            this.userHourViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.userDayViewingBtn.setStyle("-fx-background-color: #202020;");
            this.userWeekViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.userDayViewingBtn.setSelected(true);
            event.consume();
        });

        this.userWeekViewingBtn.setOnMouseClicked(event ->
        {
            this.userHourViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.userDayViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.userWeekViewingBtn.setStyle("-fx-background-color: #202020; -fx-background-radius: 0 15 0 0;");
            this.userWeekViewingBtn.setSelected(true);
            event.consume();
        });

        this.cancelBookingBtn.setOnMouseClicked(event ->
        {
            this.bookingDurationField.clear();
            this.hourBookingBtn.setSelected(true);
            this.makeBiddingDialogue.toBack();
        });

        this.makeBookingBtn.setOnMouseClicked(event ->
        {
            try
            {
                ToggleButton selectedBtn = (ToggleButton) this.toggleGroup.getSelectedToggle();
                int bookingDuration;

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("Atrium Desk Manager");

                Desk desk = this.availableDesksCombo.getValue();
                if (desk == null)
                    alert.setContentText("Please select a desk!");
                else
                {
                    int deskID = desk.getID();
                    String durationText = this.bookingDurationField.getText();
                    if(durationText.isEmpty())
                        throw new Exception("providing an invalid input!");

                    bookingDuration = Integer.parseInt(durationText);
                    Date currentDate = Calendar.getInstance().getTime();
                    Booking booking;

                    if(selectedBtn.equals(this.hourBookingBtn))
                    {
                        if(bookingDuration < 1 || bookingDuration > Booking.MAX_BOOKING_HOURS)
                        {
                            throw new Exception("not being able to book for more than "
                                    + Booking.MAX_BOOKING_HOURS + " hours!");
                        }
                        booking = desk.makeBooking(this.currentUser.getUsername(), currentDate, bookingDuration, 0, 0);
                        alert.setContentText("ACME Staff, " + this.currentUser.getUsername() +
                                " has been assigned Desk " + deskID + " for " + bookingDuration
                                + " hours successfully! " +
                                "Please login again to mark your attendance otherwise this booking will be forfeit in 2 hours!");
                    }
                    else if(selectedBtn.equals(this.dayBookingBtn))
                    {
                        if(bookingDuration < 1 || bookingDuration > Booking.MAX_BOOKING_DAYS)
                        {
                            throw new Exception("not being able to book for more than "
                                    + Booking.MAX_BOOKING_DAYS + " days!");
                        }
                        booking = desk.makeBooking(this.currentUser.getUsername(), currentDate, 0, bookingDuration, 0);
                        alert.setContentText("ACME Staff, " + this.currentUser.getUsername() +
                                " has been assigned Desk " + deskID + " for " + bookingDuration
                                + " days successfully! " +
                                "Please login again to mark your attendance otherwise this booking will be forfeit in 2 hours!");
                    }
                    else
                    {
                        if(bookingDuration < 1 || bookingDuration > Booking.MAX_BOOKING_WEEKS)
                        {
                            throw new Exception("not being able to book for more than "
                                    + Booking.MAX_BOOKING_WEEKS + " weeks!");
                        }
                        booking = desk.makeBooking(this.currentUser.getUsername(), currentDate, 0, 0, bookingDuration);
                        alert.setContentText("ACME Staff, " + this.currentUser.getUsername() +
                                " has been assigned Desk " + deskID + " for " + bookingDuration
                                + " weeks successfully! " +
                                "Please login again to mark your attendance otherwise this booking will be forfeit in 2 hours!");
                    }
                    refreshDashboards();

                    this.currentUser.setBooking(booking);
                    this.dayBookingBtn.setSelected(true);
                    this.bookingDurationField.clear();

                    this.makeBiddingDialogue.toBack();
                    this.createBookingPane.setVisible(false);
                    this.showBookingPane.setVisible(true);
                    this.showBookingPane.toFront();
                    this.showBookingDeskId.setText("Desk " + deskID + " of " + Desk.getNumberOfDesks());
                    this.bookingDurationLabel.setText(booking.getStartTime());

                }
                alert.show();
            }
            catch (Exception ex)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("ACME Staff Manager");
                alert.setContentText("An error occurred due to " + ex.getMessage());
                alert.show();
            }
        });

        this.downloadReceipt.setOnMouseClicked(event ->
        {
            Booking booking = this.currentUser.getBooking();
            if(!booking.isEmpty())
            {
                boolean result = Loader.saveReceipt(booking.getUser(), booking.getStartTime(), booking.getId());
                Alert alert;
                if(result)
                {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Your desk receipt has been successfully saved to your home directory!");
                }
                else
                {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("An error occurred while trying to download the receipt");
                }
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("Download Receipt");
                alert.show();
            }
        });

        this.userFreeViewingBtn.setOnMouseClicked(event ->
        {
             this.userFreeViewingBtn.setStyle("-fx-background-color: #202020;");
             this.userBookedViewingBtn.setStyle("-fx-background-color: #00000000;");

             ToggleButton selected = (ToggleButton) this.userToggleViewGroup.getSelectedToggle();
             if(selected.equals(this.userHourViewingBtn))
             {
                 this.userDashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.HOUR_VIEW, true));
                 this.userDashboardBookingTable.refresh();
             }
             else if(selected.equals(this.userDayViewingBtn))
             {
                 this.userDashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.DAY_VIEW, true));
                 this.userDashboardBookingTable.refresh();
             }
             else
             {
                 this.userDashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.WEEK_VIEW, true));
                 this.userDashboardBookingTable.refresh();
             }
             event.consume();
        });

        this.userBookedViewingBtn.setOnMouseClicked(event ->
        {
            this.userFreeViewingBtn.setStyle("-fx-background-color: #00000000;");
            this.userBookedViewingBtn.setStyle("-fx-background-color: #202020;");

            ToggleButton selected = (ToggleButton) this.userToggleViewGroup.getSelectedToggle();
            if(selected.equals(this.userHourViewingBtn))
            {
                this.userDashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.HOUR_VIEW, false));
                this.userDashboardBookingTable.refresh();
            }
            else if(selected.equals(this.userDayViewingBtn))
            {
                this.userDashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.DAY_VIEW, false));
                this.userDashboardBookingTable.refresh();
            }
            else
            {
                this.userDashboardBookingTable.getItems().setAll(Desk.getBookings(Desk.WEEK_VIEW, false));
                this.userDashboardBookingTable.refresh();
            }
            event.consume();
        });

    }

    @FXML
    private void showDashboardTableViewMenu(Event event)
    {
        ContextMenu menu = new ContextMenu();
        MenuItem booking = new MenuItem("Go to Booking");
        menu.getItems().add(booking);
        booking.setOnAction(actionEvent ->
        {
            this.deskPane.toFront();
            this.checkDeskDialog.toFront();
        });
        menu.show(Main.mainStage);
        event.consume();
    }

    @FXML
    private void showTableViewMenu(Event event)
    {
        ContextMenu menu = new ContextMenu();
        MenuItem cancel = new MenuItem("Cancel Booking");
        menu.getItems().add(cancel);
        cancel.setOnAction(actionEvent ->
        {
            boolean result = generateDeleteBookingAlert();
            if(result)
            {
                Booking selected = this.bookingTable.getSelectionModel().getSelectedItem();
                try
                {

                    User user = User.getUser(selected.getUser());
                    user.setBooking(new Booking());
                    user.setAlertOnLoad(true);
                    Desk.cancelBooking(selected);
                    Loader.saveDataFile();
                }
                catch (Exception ignored)
                {

                }

                this.bookingTable.getItems().removeAll(selected);
                repopulateTables();
                this.bookingTable.refresh();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Desk Management and Booking System");
                alert.setHeaderText("ACME Staff Manager");
                alert.setContentText("The booking for staff, " + selected.getUser() + " has been canceled!");
                alert.show();
                refreshDashboards();
            }
        });
        menu.show(Main.mainStage);
        event.consume();
    }

    private void switchButtonAndPane(Button button)
    {
        if(button.equals(this.dashboardBtn))
        {
            if(!currentButtonPane.equals(this.dashboardBtn))
            {
                currentButtonPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                button.setStyle("-fx-background-color: #1976D2;");
                this.dashboardPane.toFront();
                currentButtonPane = button;
            }
        }
        else if(button.equals(this.bookingsBtn))
        {
            if(!currentButtonPane.equals(this.bookingsBtn))
            {
                currentButtonPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                button.setStyle("-fx-background-color: #1976D2;");
                this.bookingPane.toFront();
                currentButtonPane = button;
            }
        }
        else if(button.equals(this.deskBtn))
        {
            if(!currentButtonPane.equals(this.deskBtn))
            {
                currentButtonPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                button.setStyle("-fx-background-color: #1976D2;");
                this.deskPane.toFront();
                currentButtonPane = button;
            }
        }
        else if(button.equals(this.staffBtn))
        {
            if(!currentButtonPane.equals(this.staffBtn))
            {
                currentButtonPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                button.setStyle("-fx-background-color: #1976D2;");
                this.staffPane.toFront();
                currentButtonPane = button;
            }
        }
        else if(button.equals(this.userDashboardBtn))
        {
            if(!currentButtonPane.equals(this.userDashboardBtn))
            {
                currentButtonPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                button.setStyle("-fx-background-color: #1976D2;");
                this.userDashboardPane.toFront();
                currentButtonPane = button;
            }
        }
        else if(button.equals(this.userBookingBtn))
        {
            if(!currentButtonPane.equals(this.userBookingBtn))
            {
                currentButtonPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
                button.setStyle("-fx-background-color: #1976D2;");
                this.bidPane.toFront();
                currentButtonPane = button;
            }
        }
    }

    @FXML
    public void requestFocus(MouseEvent event)
    {
        Node node = (Node) event.getSource();
        node.requestFocus();
        event.consume();
    }

    public static void playAnimation()
    {
        AnimationFX fade = new BounceOut(splash);
        fade.setDelay(Duration.millis(3000));
        fade.setSpeed(0.75);
        fade.setOnFinished(event -> main.toFront());
        fade.play();
    }

    @FXML
    private void showLabelTooltip(MouseEvent event)
    {
        Label label = (Label) event.getSource();
        if(label.equals(this.adminNameLabel))
            showTooltip(this.adminNameTooltip, this.adminNameLabel.getText(), this.adminNameLabel);
        else if(label.equals(this.adminEmailLabel))
            showTooltip(this.adminEmailTooltip, this.adminEmailLabel.getText(), this.adminEmailLabel);
        else if(label.equals(this.userNameLabel))
            showTooltip(this.userNameTooltip, this.userNameLabel.getText(), this.userNameLabel);
        else if(label.equals(this.userEmailLabel))
            showTooltip(this.userEmailTooltip, this.userEmailLabel.getText(), this.userEmailLabel);
        event.consume();
    }

    @FXML
    private void hideLabelTooltip(MouseEvent event)
    {
        Label label = (Label) event.getSource();
        if(label.equals(this.adminNameLabel))
            hideTooltip(this.adminNameTooltip);
        else if(label.equals(this.adminEmailLabel))
            hideTooltip(this.adminEmailTooltip);
        else if(label.equals(this.userNameLabel))
            hideTooltip(this.userNameTooltip);
        else if(label.equals(this.userEmailLabel))
            hideTooltip(this.userEmailTooltip);
        event.consume();
    }

    @FXML
    private void showButtonTooltip(MouseEvent event)
    {
        Button source = (Button) event.getSource();
        if(source.equals(this.dashboardBtn))
            showTooltip(this.adminDashboardTooltip, "Dashboard", source);
        else if(source.equals(this.staffBtn))
            showTooltip(this.adminStaffTooltip, "Staff", source);
        else if(source.equals(this.deskBtn))
            showTooltip(this.adminDeskTooltip, "Desks", source);
        else if(source.equals(this.bookingsBtn))
            showTooltip(this.adminBookingTooltip, "Bookings", source);
        else if(source.equals(this.logOutBtn))
            showTooltip(this.adminLogoutTooltip, "Log Out", source);
        else if(source.equals(this.userDashboardBtn))
            showTooltip(this.userDashboardTooltip, "Dashboard", source);
        else if(source.equals(this.userLogOutBtn))
            showTooltip(this.userLogoutTooltip, "Log Out", source);
        else if(source.equals(this.userBookingBtn))
            showTooltip(this.userBookingTooltip, "Bookings", source);
        event.consume();
    }

    @FXML
    private void hideButtonTooltip(MouseEvent event)
    {
        Button source = (Button) event.getSource();
        if (source.equals(this.dashboardBtn))
            hideTooltip(this.adminDashboardTooltip);
        else if(source.equals(this.staffBtn))
            hideTooltip(this.adminStaffTooltip);
        else if(source.equals(this.deskBtn))
            hideTooltip(this.adminDeskTooltip);
        else if(source.equals(this.bookingsBtn))
            hideTooltip(this.adminBookingTooltip);
        else if(source.equals(this.logOutBtn))
            hideTooltip(this.adminLogoutTooltip);
        else if(source.equals(this.userDashboardBtn))
            hideTooltip(this.userDashboardTooltip);
        else if(source.equals(this.userLogOutBtn))
            hideTooltip(this.userLogoutTooltip);
        else if(source.equals(this.userBookingBtn))
            hideTooltip(this.userBookingTooltip);
        event.consume();
    }

    @FXML
    public void quit(MouseEvent event)
    {
        boolean saved = Loader.saveDataFile();
        if(saved)
            Platform.exit();
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Desk Management and Booking System");
            alert.setHeaderText("Database Error");
            alert.setContentText("An error has occurred due to " + Loader.getErrorMessage());
            alert.show();
        }
        if(event != null)
            event.consume();
    }

    @FXML
    private void hide(MouseEvent event)
    {
        Main.mainStage.setIconified(true);
        event.consume();
    }

    @FXML
    private void showAddDeskDialog(MouseEvent event)
    {
        this.addDeskDialog.toFront();
        event.consume();
    }

    @FXML
    private void showAddStaffDialog(MouseEvent event)
    {
        this.addStaffDialog.toFront();
        event.consume();
    }

    @FXML
    private void showRemoveStaffDialog(MouseEvent event)
    {
        this.removeStaffDialog.toFront();
        event.consume();
    }

    @FXML
    private void showRemoveDeskDialog(MouseEvent event)
    {
        this.removeDeskDialog.toFront();
        event.consume();
    }

    @FXML
    private void showDeskDetailsDialog(MouseEvent event)
    {
        this.checkDeskDialog.toFront();
        event.consume();
    }

    @FXML
    private void showStaffDetails(MouseEvent event)
    {
        this.checkStaffDialog.toFront();
        event.consume();
    }

    @FXML
    private void showMakeBooking(MouseEvent event)
    {
        this.makeBiddingDialogue.toFront();
        event.consume();
    }

    private void refreshDashboards()
    {
        int total = Desk.getHourTrends() + Desk.getDayTrends() + Desk.getWeekTrends();
        double hourTrend = Desk.getHourTrends();
        if(hourTrend != 0)
        {
            hourTrend = (hourTrend / total) * 100;
            hourTrend = Math.round(hourTrend);
        }

        double dayTrend = Desk.getDayTrends();
        if(dayTrend != 0)
        {
            dayTrend = (dayTrend / total) * 100;
            dayTrend = Math.round(dayTrend);
        }

        double weekTrend = Desk.getWeekTrends();
        if(weekTrend != 0)
        {
            weekTrend = (weekTrend / total) * 100;
            weekTrend = Math.round(weekTrend);
        }

        this.hourTrendLabel.setText((int)(hourTrend) + "%");
        this.userHourTrendLabel.setText((int)(hourTrend) + "%");

        this.dayTrendLabel.setText((int)(dayTrend) + "%");
        this.userDayTrendLabel.setText((int)(dayTrend) + "%");

        this.weekTrendLabel.setText((int)(weekTrend) + "%");
        this.userWeekTrendLabel.setText((int)(weekTrend) + "%");

        int allUsers = User.getNumberOfUsers();
        int allDesks = Desk.getNumberOfDesks();

        this.totalUsersLabel.setText("" + allUsers);
        this.totalDesksLabel.setText("" + allDesks);

        ObservableList<Desk> deskObservableList = FXCollections.observableList(Desk.getAllAvailableDesks());
        this.availableDesksCombo.setItems(deskObservableList);
    }

}
