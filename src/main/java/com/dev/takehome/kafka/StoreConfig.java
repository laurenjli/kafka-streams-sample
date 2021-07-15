package com.dev.takehome.kafka;

import com.dev.takehome.constant.Constants;
import com.ibm.gbs.schema.Customer;
import com.ibm.gbs.schema.Transaction;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * This class contains the configuration for the state stores that I am using for the KTables I am joining.
 *
 * reference: https://chrzaszcz.dev/2019/09/kafka-streams-store-basics/
 * reference: https://jaceklaskowski.gitbooks.io/mastering-kafka-streams/content/kafka-streams-Materialized.html#cachingEnabled
 */

@Configuration
@EnableKafka
public class StoreConfig {

    @Bean
    public Materialized<String, Customer, KeyValueStore<Bytes, byte[]>> customerStateStore(
            Serde<Customer> customerStateStoreSerde
    ) {
        return Materialized.<String, Customer, KeyValueStore<Bytes, byte[]>>as(
                Constants.CUSTOMER_STATE_STORE)
                .withKeySerde(Serdes.String())
                .withValueSerde(customerStateStoreSerde)
                .withCachingDisabled();
    }

    @Bean
    public Materialized<String, Transaction, KeyValueStore<Bytes, byte[]>> balanceStateStore(
            Serde<Transaction> balanceStateStoreSerde
    ) {
        return Materialized.<String, Transaction, KeyValueStore<Bytes, byte[]>>as(
                Constants.BALANCE_STATE_STORE)
                .withKeySerde(Serdes.String())
                .withValueSerde(balanceStateStoreSerde)
                .withCachingDisabled();
    }

}
