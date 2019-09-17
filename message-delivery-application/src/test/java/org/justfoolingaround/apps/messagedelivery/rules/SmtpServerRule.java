package org.justfoolingaround.apps.messagedelivery.rules;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import lombok.extern.slf4j.Slf4j;
import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

@Service
@ActiveProfiles("test")
@Slf4j
public class SmtpServerRule extends ExternalResource {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${spring.mail.port}")
    private int port;

    private GreenMail smtpServer;

    @Override
    protected void before() throws Throwable {

        super.before();
        smtpServer = new GreenMail(new ServerSetup(port, host, protocol));
        smtpServer.start();
    }

    public List<MimeMessage> getMessages() {

        MimeMessage[] receivedMessages = smtpServer.getReceivedMessages();

        log.info("MimeMessage:{}", Arrays.toString(receivedMessages));

        return Arrays.asList(receivedMessages);
    }

    @Override
    protected void after() {
        super.after();
        smtpServer.stop();
    }
}
