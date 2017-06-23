package net.smpp.client.simple.service;

import lombok.Getter;
import net.smpp.client.simple.utils.Constants;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SessionBinder {

    private Logger logger = Logger.getLogger(getClass());

    private final MessageReceiver messageReceiver;
    private SMPPSession session;

    @Autowired
    public SessionBinder(MessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public void bindSession(BindType bindType,
                            String login,
                            String pass,
                            String ip,
                            Integer port
    ) throws Exception {

        BasicConfigurator.configure();
        session = new SMPPSession();

        session.connectAndBind(ip,
                port,
                new BindParameter(bindType,
                        login,
                        pass,
                        "",
                        TypeOfNumber.UNKNOWN,
                        NumberingPlanIndicator.UNKNOWN,
                        null));
        session.setEnquireLinkTimer(Constants.ENQUIRE_LINK_TIMER);

        // Set listener to receive deliver_sm
        session.setMessageReceiverListener(messageReceiver);
    }
}
