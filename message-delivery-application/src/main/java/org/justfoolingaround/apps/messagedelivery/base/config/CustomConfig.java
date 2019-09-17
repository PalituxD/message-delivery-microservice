package org.justfoolingaround.apps.messagedelivery.base.config;

import org.justfoolingaround.apps.messagedelivery.domain.entity.BatchEntity;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Batch;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class CustomConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(Boolean.TRUE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.addMappings(new PropertyMap<Batch, BatchEntity>() {
            @Override
            protected void configure() {
                skip().setMessages(null);
            }
        });

        modelMapper.addMappings(new PropertyMap<BatchEntity, Batch>() {
            @Override
            protected void configure() {
                skip().setMessages(null);
            }
        });

        return modelMapper;
    }
}
