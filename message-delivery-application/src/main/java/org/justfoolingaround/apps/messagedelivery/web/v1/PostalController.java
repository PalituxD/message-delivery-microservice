package org.justfoolingaround.apps.messagedelivery.web.v1;

import org.json.JSONObject;
import org.justfoolingaround.apps.messagedelivery.base.Constants;
import org.justfoolingaround.apps.messagedelivery.services.PostalService;
import org.justfoolingaround.apps.messagedelivery.web.base.ResourceNotFoundException;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageDto;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageHeaderDto;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Constants.URL_API_V1_MESSAGE)
public class PostalController {

    @Autowired
    private PostalService postalService;

    @PostMapping(value = Constants.URL_API_V1_MESSAGE_EMAIL
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public String putEmail(@Valid @RequestBody MessageDto messageDto) {

        return postalService.receive(messageDto);
    }

    @GetMapping(value = Constants.URL_API_V1_MESSAGE_SEARCH
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageInfoDto search(@Valid MessageHeaderDto messageHeaderDto) throws ResourceNotFoundException {

        return postalService.lookup(messageHeaderDto).orElseThrow(() ->
                new ResourceNotFoundException("MESSAGE NOT FOUND", new JSONObject(messageHeaderDto).toString())
        );
    }

    @GetMapping(value = Constants.URL_API_V1_MESSAGE_SEARCH_BY + "/{key}"
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageInfoDto searchBy(@PathVariable(value = "key") String key,
                                   @Valid String value) throws ResourceNotFoundException {

        final String supportedFilter = "code";
        if (supportedFilter.equalsIgnoreCase(key)) {
            return postalService.lookup(value).orElseThrow(() ->
                    new ResourceNotFoundException("MESSAGE NOT FOUND", value)
            );
        }

        return null;
    }
}
