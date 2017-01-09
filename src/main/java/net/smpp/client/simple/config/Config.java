package net.smpp.client.simple.config;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@ComponentScan({"net.smpp.client.simple.*"})
public class Config {

    private final ApplicationContext appContext;

    @Autowired
    public Config(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Bean
    public Parent getRoot() throws IOException {
        try (InputStream fxmlStream = getClass().getClassLoader().getResourceAsStream("main_form.fxml")) {
            FXMLLoader loader = new FXMLLoader();
            //this factory tels that need create bean over spring
            loader.setControllerFactory(appContext::getBean);
            return loader.load(fxmlStream);
        }
    }
}
