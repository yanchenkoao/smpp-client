package net.smpp.client.simple.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import net.smpp.client.simple.logger.CustomAppender;
import net.smpp.client.simple.service.MessageSender;
import net.smpp.client.simple.service.SessionBinder;
import net.smpp.client.simple.utils.TextUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.jsmpp.bean.BindType;
import org.jsmpp.session.SMPPSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.jsmpp.bean.BindType.*;

@Component
public class MainController {

    private Logger logger = Logger.getLogger(MainController.class);

    private final MessageSender messageSender;
    private final SessionBinder sessionBinder;

    @FXML
    private ChoiceBox sessionTypeChoiceBox;
    @FXML
    private TextField serverIpField;
    @FXML
    private TextField serverPortField;
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField alphaNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextArea enterTextArea;
    @FXML
    private TextArea logArea;
    @FXML
    private Button connectButton;
    @FXML
    private Button disconnectButton;
    @FXML
    private Button sendTextButton;
    @FXML
    private Label countPartsLabel;

    @Autowired
    public MainController(MessageSender messageSender, SessionBinder sessionBinder) {
        this.messageSender = messageSender;
        this.sessionBinder = sessionBinder;
    }

    /**
     * Инициализация контроллера от JavaFX.
     * Метод вызывается после того как FXML загрузчик произвел инъекции полей.
     * <p>
     * Обратите внимание, что имя метода <b>обязательно</b> должно быть "initialize",
     * в противном случае, метод не вызовется.
     * <p>
     * Также на этом этапе еще отсутствуют бины спринга
     * и для инициализации лучше использовать метод,
     * описанный аннотацией @PostConstruct.
     * Который вызовется спрингом, после того,
     * как им будут произведены все оставшиеся инъекции.
     * {@link MainController#init()}
     */
    @FXML
    public void initialize() {
        sessionTypeChoiceBox.setItems(FXCollections.observableArrayList(BIND_TX, BIND_RX, BIND_TRX));
        sessionTypeChoiceBox.getSelectionModel().selectFirst();
        sendTextButton.disableProperty().set(true);

        CustomAppender.setLogTextArea(logArea);
    }

    /**
     * На этом этапе уже произведены все возможные инъекции.
     */
    @PostConstruct
    public void init() {
        System.out.println("init actions");
    }

    /**
     * Метод, вызываемый при нажатии на кнопку "Добавить".
     * Привязан к кнопке в FXML файле представления.
     */

    @FXML
    public void connectButtonPressed(ActionEvent actionEvent) {
        try {
            //check session type;
            BindType bindType = (BindType) sessionTypeChoiceBox.getSelectionModel().getSelectedItem();

            String login = loginField.getText();
            String pass = passwordField.getText();
            String ip = serverIpField.getText();
            String port = serverPortField.getText();

            sessionBinder.bindSession(bindType, login, pass, ip, Integer.valueOf(port));

            logger.info("Connected" + System.lineSeparator());

            connectButton.disableProperty().set(true);
            disconnectButton.disableProperty().set(false);
            sendTextButton.disableProperty().set(false);
        } catch (Exception e) {
            connectButton.disableProperty().set(false);
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @FXML
    public void disconnectButtonPressed(ActionEvent actionEvent) {
        SMPPSession session = sessionBinder.getSession();

        if (session != null) {
            session.unbindAndClose();
            System.out.println("Disconnected" + System.lineSeparator());

            disconnectButton.disableProperty().set(true);
            connectButton.disableProperty().set(false);
        }
    }

    @FXML
    public void sendMessageButtonPressed(ActionEvent actionEvent) {
        try {
            String alphaName = alphaNameField.getText();
            String phone = phoneNumberField.getText();
            String text = enterTextArea.getText();

            if (alphaName.isEmpty()) {
                System.out.println("alpha name can`t be empty");
                return;
            } else if (phone.isEmpty() || !phone.matches("[0-9]+")) {
                System.out.println("phone is incorrect");
                return;
            } else if (text.isEmpty()) {
                System.out.println("text can`t be empty");
                return;
            }

            if (sessionBinder.getSession() != null) {
                messageSender.sendMessage(text, alphaName, phone, sessionBinder.getSession());
            } else {
                System.out.println("smpp session not connected");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @FXML
    public void clearMessageAreaButtonPressed(ActionEvent actionEvent) {
        enterTextArea.clear();
    }

    @FXML
    public void clearLogButtonPressed(ActionEvent actionEvent) {
        logArea.clear();
    }


    public void textAreaChangedTextAction(KeyEvent keyEvent) {
        countPartsLabel.setText(String.valueOf(TextUtils.getPartsOfMessage(enterTextArea.getText()).length));
    }
}