package net.smpp.client.simple.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import net.smpp.client.simple.domain.DataMessage;
import net.smpp.client.simple.domain.ServiceType;
import net.smpp.client.simple.domain.UdhType;
import net.smpp.client.simple.enums.LatinEncodingType;
import net.smpp.client.simple.service.AsyncTask;
import net.smpp.client.simple.service.MessageSender;
import net.smpp.client.simple.service.SessionBinder;
import net.smpp.client.simple.service.Validator;
import net.smpp.client.simple.utils.TextUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.jsmpp.bean.BindType;
import org.jsmpp.session.SMPPSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static net.smpp.client.simple.domain.ServiceType.*;
import static net.smpp.client.simple.domain.UdhType.*;
import static net.smpp.client.simple.enums.LatinEncodingType.GSM_0338;
import static net.smpp.client.simple.enums.LatinEncodingType.LATIN_ISO8859_1;
import static net.smpp.client.simple.utils.Constants.MAX_MESSAGES_PER_BATCH;
import static org.jsmpp.bean.BindType.*;

@Component
public class MainController {

    private Logger logger = Logger.getLogger(MainController.class);

    private final MessageSender messageSender;
    private final SessionBinder sessionBinder;
    private final Validator validator;
    private Thread asyncTaskSender;

    @FXML
    private ChoiceBox<BindType> sessionTypeChoiceBox;
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
    private Button connectButton;
    @FXML
    private Button disconnectButton;
    @FXML
    private Button sendTextButton;
    @FXML
    private Label countPartsLabel;
    @FXML
    private ChoiceBox<ServiceType> serviceTypeChoiceBox;
    @FXML
    private TextField registeredDeliveryField;
    @FXML
    private TextField validityPeriodField;
    @FXML
    private TextField sourceAddrTonField;
    @FXML
    private TextField sourceAddrNpiField;
    @FXML
    private TextField destAddrTonField;
    @FXML
    private TextField destAddrNpiField;
    @FXML
    private ChoiceBox<UdhType> udhTypeChoiceBox;
    @FXML
    private ChoiceBox<LatinEncodingType> latinEncodingChoiceBox;
    @FXML
    private TextField countMessagesPerSecondField;

    @Autowired
    public MainController(MessageSender messageSender, SessionBinder sessionBinder, Validator validator) {
        this.messageSender = messageSender;
        this.sessionBinder = sessionBinder;
        this.validator = validator;
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
        sessionTypeChoiceBox.setItems(FXCollections.observableArrayList(BIND_TRX, BIND_TX, BIND_RX));
        sessionTypeChoiceBox.getSelectionModel().selectFirst();

        udhTypeChoiceBox.setItems(FXCollections.observableArrayList(tlv, udh_8bit, no_udh, udh_16bit));
        udhTypeChoiceBox.getSelectionModel().selectFirst();

        latinEncodingChoiceBox.setItems(FXCollections.observableArrayList(GSM_0338, LATIN_ISO8859_1));
        latinEncodingChoiceBox.getSelectionModel().selectFirst();

        sendTextButton.disableProperty().set(true);

        serviceTypeChoiceBox.setItems(FXCollections.observableArrayList(
                default_type,
                cellular_messaging_CMT,
                cellular_paging_CPT,
                voice_mail_notification_VMN,
                voice_mail_alerting_VMA,
                wireless_application_protocol_WAP,
                unstructured_supplementary_services_data_USSD
        ));
        serviceTypeChoiceBox.getSelectionModel().selectFirst();
    }

    /**
     * На этом этапе уже произведены все возможные инъекции.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Метод, вызываемый при нажатии на кнопку "Добавить".
     * Привязан к кнопке в FXML файле представления.
     */

    @FXML
    public void connectButtonPressed(ActionEvent actionEvent) {
        try {
            //check session type;
            BindType bindType = sessionTypeChoiceBox.getSelectionModel().getSelectedItem();

            String login = loginField.getText();
            String pass = passwordField.getText();
            String ip = serverIpField.getText();
            Integer port = Integer.valueOf(serverPortField.getText());

            if (!validator.validateLoginPass(login, pass)) {
                logger.error("not correct login or password");
                return;
            }

            sessionBinder.bindSession(bindType, login, pass, ip, port);
            logger.info("Connected" + System.lineSeparator());

            connectButton.disableProperty().set(true);
            disconnectButton.disableProperty().set(false);

            setServerPropertiesDisabled(true);


            if (bindType != BIND_RX) {
                sendTextButton.disableProperty().set(false);
            }
        } catch (Exception e) {
            connectButton.disableProperty().set(false);
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @FXML
    public void disconnectButtonPressed(ActionEvent actionEvent) {
        SMPPSession session = sessionBinder.getSession();

        try {
            if (session != null) {
                session.unbindAndClose();
                logger.info("Disconnected" + System.lineSeparator());
                disconnectButton.disableProperty().set(true);
                sendTextButton.disableProperty().set(true);
                connectButton.disableProperty().set(false);
                setServerPropertiesDisabled(false);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    @FXML
    public void onLoadStartSend(ActionEvent actionEvent) {

        DataMessage dataMessage = new DataMessage(
                alphaNameField.getText(),
                phoneNumberField.getText(),
                enterTextArea.getText(),
                udhTypeChoiceBox.getSelectionModel().getSelectedItem(),
                serviceTypeChoiceBox.getSelectionModel().getSelectedItem(),
                Integer.valueOf(validityPeriodField.getText()),
                Byte.valueOf(sourceAddrTonField.getText()),
                Byte.valueOf(sourceAddrNpiField.getText()),
                Byte.valueOf(destAddrTonField.getText()),
                Byte.valueOf(destAddrNpiField.getText()),
                latinEncodingChoiceBox.getSelectionModel().getSelectedItem(),
                1
        );

        int countMessagesPerSecond = Integer.valueOf(countMessagesPerSecondField.getText());

        if (countMessagesPerSecond < 1 || countMessagesPerSecond > MAX_MESSAGES_PER_BATCH) {
            countMessagesPerSecond = MAX_MESSAGES_PER_BATCH;
        }
        if (asyncTaskSender == null) {
            asyncTaskSender = new AsyncTask(messageSender, sessionBinder, dataMessage, countMessagesPerSecond);
            asyncTaskSender.start();
        }
    }

    @FXML
    public void onLoadStopSend(ActionEvent actionEvent) {
        asyncTaskSender.interrupt();
        asyncTaskSender = null;
    }

    @FXML
    public void sendMessageButtonPressed(ActionEvent actionEvent) {
        try {
            String alphaName = alphaNameField.getText();
            String phone = phoneNumberField.getText();
            String text = enterTextArea.getText();
            UdhType udhType = udhTypeChoiceBox.getSelectionModel().getSelectedItem();
            LatinEncodingType latinEncodingType = latinEncodingChoiceBox.getSelectionModel().getSelectedItem();

            if (alphaName.isEmpty()) {
                logger.error("alpha name can`t be empty");
                return;
            } else if (phone.isEmpty() || !phone.matches("[0-9]+")) {
                logger.error("phone is incorrect");
                return;
            } else if (text.isEmpty()) {
                logger.error("text can`t be empty");
                return;
            }

            ServiceType serviceType = serviceTypeChoiceBox.getSelectionModel().getSelectedItem();
            Integer validityPeriod = Integer.valueOf(validityPeriodField.getText());
            Integer registeredDelivery = Integer.valueOf(registeredDeliveryField.getText());
            Byte sourceAddrTon = Byte.valueOf(sourceAddrTonField.getText());
            Byte sourceAddrNpi = Byte.valueOf(sourceAddrNpiField.getText());
            Byte destAddrTon = Byte.valueOf(destAddrTonField.getText());
            Byte destAddrNpi = Byte.valueOf(destAddrNpiField.getText());

            DataMessage dataMessage = new DataMessage(
                    alphaName,
                    phone,
                    text,
                    udhType,
                    serviceType,
                    validityPeriod,
                    sourceAddrTon,
                    sourceAddrNpi,
                    destAddrTon,
                    destAddrNpi,
                    latinEncodingType,
                    registeredDelivery
            );

            if (sessionBinder.getSession() != null) {
                messageSender.sendMessage(
                        sessionBinder.getSession(),
                        dataMessage);
            } else {
                logger.error("smpp session not connected");
            }
        } catch (Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    @FXML
    public void clearMessageAreaButtonPressed(ActionEvent actionEvent) {
        enterTextArea.clear();
    }

    @FXML
    public void textAreaChangedTextAction(KeyEvent keyEvent) {
        countPartsLabel.setText(String.valueOf(TextUtils.getPartsOfMessage(enterTextArea.getText(), LatinEncodingType.GSM_0338).length));
    }

    private void setServerPropertiesDisabled(boolean isDisabled) {
        serverIpField.disableProperty().set(isDisabled);
        serverPortField.disableProperty().set(isDisabled);
        loginField.disableProperty().set(isDisabled);
        passwordField.disableProperty().set(isDisabled);
        sessionTypeChoiceBox.disableProperty().set(isDisabled);
    }
}