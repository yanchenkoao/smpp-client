package net.smpp.client.simple;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;

@Lazy
@SpringBootApplication
public class SmppClient extends Application {

    private static String[] savedArgs;
    private ConfigurableApplicationContext context;

    @Value("Smpp client")
    private String windowTitle;

    @Autowired
    private Parent parent;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(parent));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launchApp(SmppClient.class, args);
    }

    private static void launchApp(Class<? extends Application> clazz, String[] args) {
        SmppClient.savedArgs = args;
        Application.launch(clazz, args);
    }

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(getClass(), savedArgs);
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        context.close();
    }
}
