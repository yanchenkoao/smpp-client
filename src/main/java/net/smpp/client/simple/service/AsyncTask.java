package net.smpp.client.simple.service;

import net.smpp.client.simple.domain.DataMessage;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

public class AsyncTask extends Thread {

    private Logger logger = Logger.getLogger(getClass());

    private MessageSender messageSender;
    private SessionBinder sessionBinder;
    private DataMessage dataMessage;
    private int countMessages;

    public AsyncTask(MessageSender messageSender, SessionBinder sessionBinder, DataMessage dataMessage, int countMessages) {
        this.messageSender = messageSender;
        this.sessionBinder = sessionBinder;
        this.dataMessage = dataMessage;
        this.countMessages = countMessages;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            for (int i = 0; i < countMessages; i++) {
                if (sessionBinder.getSession() != null) {
                    messageSender.sendMessage(dataMessage.getUdhType(),
                            dataMessage.getText(),
                            dataMessage.getAlphaName(),
                            dataMessage.getPhone(),
                            sessionBinder.getSession(),
                            dataMessage.getServiceType(),
                            dataMessage.getValidityPeriod(),
                            dataMessage.getSourceAddrTon(),
                            dataMessage.getSourceAddrNpi(),
                            dataMessage.getDestAddrTon(),
                            dataMessage.getDestAddrNpi(),
                            true);
                } else {
                    logger.error("smpp session not connected");
                }
            }
            logger.info(String.format("sended batch: %s messages", countMessages));

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
                interrupt();
            }
        }
    }
}
