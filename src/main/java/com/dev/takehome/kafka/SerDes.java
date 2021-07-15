package com.dev.takehome.kafka;

import com.dev.takehome.constant.Constants;
import com.ibm.gbs.schema.Customer;
import com.ibm.gbs.schema.CustomerBalance;
import com.ibm.gbs.schema.Transaction;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroDeserializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;

/**
 * This class contains the configuration for the serializer/deserializers used for consuming/producing to topics.
 *
 * reference: https://docs.confluent.io/platform/current/streams/developer-guide/datatypes.html
 */

@Configuration
public class SerDes {
    // Create a serdeConfig to use for the custom serdes
    final Map<String, String> serdeConfig = Collections.singletonMap(Constants.SCHEMA_REGISTRY_CONFIG,
            Constants.SCHEMA_REGISTRY_URL);

    //Serde for keys, which are strings
    @Bean
    public Serde<String> keySerde(){
        return Serdes.String();
    }

    // Balance object serde
    @Bean
    public Serde<Transaction> balanceSerde(){
        Serde<Transaction> serde =
                Serdes.serdeFrom(new SpecificAvroSerializer<>(), new SpecificAvroDeserializer<>());
        serde.configure(serdeConfig, false);
        return serde;
    }

    // Customer object serde
    @Bean
    public Serde<Customer> customerSerde(){
        Serde<Customer> serde =
                Serdes.serdeFrom(new SpecificAvroSerializer<>(), new SpecificAvroDeserializer<>());
        serde.configure(serdeConfig, false);
        return serde;
    }

    // CustomerBalance object serde
    @Bean
    public Serde<CustomerBalance> customerBalanceSerde(){
        Serde<CustomerBalance> serde =
                Serdes.serdeFrom(new SpecificAvroSerializer<>(), new SpecificAvroDeserializer<>());
        serde.configure(serdeConfig, false);
        return serde;
    }

    // Balance state store serde
    @Bean
    public Serde<Transaction> balanceStateStoreSerde(){
        Serde<Transaction> serde =
                Serdes.serdeFrom(new SpecificAvroSerializer<>(), new SpecificAvroDeserializer<>());
        serde.configure(serdeConfig, false);
        return serde;
    }

    // Customer state store serde
    @Bean
    public Serde<Customer> customerStateStoreSerde(){
        Serde<Customer> serde =
                Serdes.serdeFrom(new SpecificAvroSerializer<>(), new SpecificAvroDeserializer<>());
        serde.configure(serdeConfig, false);
        return serde;
    }

}
