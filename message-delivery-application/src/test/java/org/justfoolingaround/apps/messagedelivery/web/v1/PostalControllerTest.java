package org.justfoolingaround.apps.messagedelivery.web.v1;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.base.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class PostalControllerTest {

    @Autowired
    private MockMvc mvc;

    @Value(Constants.URL_API_V1_PUT_EMAIL)
    private String urlPutNewMessage;

    @Value(Constants.URL_API_V1_SEARCH)
    private String urlSearchMessage;

    @Test
    public void put() {

    }

    @Test
    public void get() {
    }
}