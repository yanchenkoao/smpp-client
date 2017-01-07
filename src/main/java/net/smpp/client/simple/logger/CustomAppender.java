package net.smpp.client.simple.logger;

import javafx.scene.control.TextArea;
import org.apache.log4j.Level;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

public class CustomAppender extends WriterAppender {

    private static final String splitLine = "------------------------------------------------------------------------------------------------------------------------------------------------------";
    private static TextArea logArea;

    @Override
    public void append(LoggingEvent loggingEvent) {


        String logRow = layout.format(loggingEvent) + System.lineSeparator() + splitLine + System.lineSeparator();

        if (loggingEvent.getLevel() == Level.ERROR || loggingEvent.getLevel() == Level.WARN) {
            logArea.setStyle("-fx-text-fill: red");
        } else {
            logArea.setStyle("-fx-text-fill: black;");
        }

        logArea.appendText(logRow);
        logArea.positionCaret(logArea.getLength());
    }

    public static void setLogTextArea(TextArea logArea) {
        CustomAppender.logArea = logArea;
    }
}
