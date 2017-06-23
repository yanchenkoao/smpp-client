package net.smpp.client.simple.service;

import net.smpp.client.simple.controller.MainController;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import static net.smpp.client.simple.utils.Constants.*;
import static org.apache.commons.lang.StringUtils.isBlank;

@Component
public class Validator {

    private Logger logger = Logger.getLogger(getClass());

    public boolean validateLoginPass(String login, String pass) {
        return validateLogin(login) && validatePass(pass);
    }

    private boolean validateLogin(String login) {
        if (!isBlank(login)) {
            return login.length() <= MAX_LOGIN_LENGTH;
        } else {
            logger.error("login can`t be null");
            return false;
        }
    }

    private boolean validatePass(String pass) {
        if (!isBlank(pass)) {
            return pass.length() <= MAX_PASSWORD_LENGTH;
        } else {
            logger.error("password can`t be null");
            return false;
        }
    }
}
