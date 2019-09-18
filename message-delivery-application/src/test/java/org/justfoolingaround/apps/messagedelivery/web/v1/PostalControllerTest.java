package org.justfoolingaround.apps.messagedelivery.web.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.base.Constants;
import org.justfoolingaround.apps.messagedelivery.services.CodeGeneratorService;
import org.justfoolingaround.apps.messagedelivery.utils.Util;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageDto;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
@AutoConfigureMockMvc
public class PostalControllerTest {

    private static final String EVALUATION_PATH_RESULT = "$";
    private static final String EVALUATION_PATH_RESULT_NOT_SENT = "$.sent";

    private static final String CUSTOM_CODE_REFERENCE = "MY-CUSTOM-CODE";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CodeGeneratorService codeGeneratorService;

    @Value(Constants.URL_API_V1_MESSAGE + Constants.URL_API_V1_MESSAGE_EMAIL)
    private String urlPutNewMessage;

    @Value(Constants.URL_API_V1_MESSAGE + Constants.URL_API_V1_MESSAGE_SEARCH)
    private String urlSearchMessage;

    @Value(Constants.URL_API_V1_MESSAGE + Constants.URL_API_V1_MESSAGE_SEARCH_BY + "/code")
    private String urlSearchMessageByCode;

    private ResultActions resultActions;

    private MessageDto messageDto;

    @Before
    public void before() {

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, Boolean.FALSE);
        Mockito.when(codeGeneratorService.generate()).thenReturn(CUSTOM_CODE_REFERENCE);
    }

    @Test
    public void putEmail() throws Exception {

        givenMessage();
        whenPerformPutEmail();
        thenOkExpectedCodeReference(EVALUATION_PATH_RESULT);
    }

    @Test
    public void putBadEmailThenFails() throws Exception {

        givenMessage();

        messageDto.setTo(Util.randomValue());

        whenPerformPutEmail();
        thenBadRequestExpected();
    }

    @Test
    public void search() throws Exception {

        givenMessage();
        whenPerformPutEmail();
        thenOkExpectedCodeReference(EVALUATION_PATH_RESULT);

        whenPerformSearchMessage();
        thenOkExpectedNotSentYet(EVALUATION_PATH_RESULT_NOT_SENT);
    }

    @Test
    public void searchThenNotFound() throws Exception {

        givenMessage();
        whenPerformPutEmail();
        thenOkExpectedCodeReference(EVALUATION_PATH_RESULT);
        whenAlterMessageHeaderDto();
        whenPerformSearchMessage();
        thenNotFoundExpected();
    }

    @Test
    public void searchBy() throws Exception {

        givenMessage();
        whenPerformPutEmail();
        thenOkExpectedCodeReference(EVALUATION_PATH_RESULT);

        whenPerformSearchMessageByCode(CUSTOM_CODE_REFERENCE);
        thenOkExpectedNotSentYet(EVALUATION_PATH_RESULT_NOT_SENT);
    }

    @Test
    public void searchByThenNotFound() throws Exception {

        givenMessage();
        whenPerformPutEmail();
        thenOkExpectedCodeReference(EVALUATION_PATH_RESULT);

        whenPerformSearchMessageByCode(Util.randomValue());
        thenNotFoundExpected();
    }

    private void givenMessage() {

        messageDto = Util.generateMessage(Util.generateKey(), 0);
    }

    private void whenPerformPutEmail() throws Exception {

        resultActions = mvc.perform(MockMvcRequestBuilders.post(urlPutNewMessage)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDto))
        );
    }

    private void whenPerformSearchMessage() throws Exception {

        resultActions = mvc.perform(MockMvcRequestBuilders.get(urlSearchMessage)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .params(getParametersForSearch())
        );
    }

    private void whenPerformSearchMessageByCode(String code) throws Exception {

        resultActions = mvc.perform(MockMvcRequestBuilders.get(urlSearchMessageByCode)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("value", code)
        );
    }

    private void whenAlterMessageHeaderDto() {
        messageDto.getHeaderDto().setPayloadId(Util.randomValue());
    }

    private MultiValueMap<String, String> getParametersForSearch() {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> maps = objectMapper.convertValue(messageDto.getHeaderDto(),
                new TypeReference<Object>() {
                });
        params.setAll(maps);
        return params;
    }

    private void thenOkExpectedCodeReference(String path) throws Exception {

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(path, CoreMatchers.equalTo(CUSTOM_CODE_REFERENCE)));
    }

    private void thenNotFoundExpected() throws Exception {

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private void thenBadRequestExpected() throws Exception {

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private void thenOkExpectedNotSentYet(String path) throws Exception {

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(path, CoreMatchers.equalTo(Boolean.FALSE)));
    }
}