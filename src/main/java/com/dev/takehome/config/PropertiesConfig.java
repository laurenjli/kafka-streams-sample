package com.dev.takehome.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains a few of the property configurations for the application,
 * specifically the topic names for the input topics and output topic.
 * The variables are referencing the names located in java/resources/application.yml.
 */

@Configuration
@Data
public class PropertiesConfig {

    @Value("${kafka.topic.input.balance:Balance}")
    private String balanceTopic;

    @Value("${kafka.topic.input.customer:Customer}")
    private String customerTopic;

    @Value("${kafka.topic.output.customerBalance:CustomerBalance}")
    private String customerBalanceTopic;

}
