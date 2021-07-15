package com.dev.takehome.stream;


import com.dev.takehome.config.PropertiesConfig;
import com.dev.takehome.joiner.CustomerBalanceJoin;
import com.dev.takehome.reducer.BalanceReducer;
import com.dev.takehome.reducer.CustomerReducer;
import com.ibm.gbs.schema.Customer;
import com.ibm.gbs.schema.CustomerBalance;
import com.ibm.gbs.schema.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

/**
 * This class builds calls the BuildBalanceTable and BuildCustomerTable classes
 * and then performs the final join.
 *
 * Reference: https://kafka-tutorials.confluent.io/foreign-key-joins/kstreams.html
 */

@Slf4j
@Configuration
@EnableKafkaStreams
public class BuildJoinStream {
    @Autowired Serde<String> keySerde;
    @Autowired Serde<CustomerBalance> customerBalanceSerde;
    @Autowired
    PropertiesConfig propertiesConfig;

    @Autowired BuildBalanceTable buildBalanceTable;
    @Autowired BuildCustomerTable buildCustomerTable;
    @Autowired BalanceReducer balanceReducer;
    @Autowired CustomerReducer customerReducer;
    @Autowired CustomerBalanceJoin customerBalanceJoiner;
    @Autowired Materialized<String, Transaction, KeyValueStore<Bytes, byte[]>> balanceStateStore;
    @Autowired Materialized<String, Customer, KeyValueStore<Bytes, byte[]>> customerStateStore;

    @Bean
    public StreamsBuilder buildCustomerBalanceJoinStream(StreamsBuilder streamsBuilder) {
        KTable<String, Transaction> balanceKTable = buildBalanceTable
                .buildBalanceStream(streamsBuilder, balanceReducer, balanceStateStore);

        KTable<String, Customer> customerKTable = buildCustomerTable
                .buildCustomerStream(streamsBuilder, customerReducer, customerStateStore);

        customerKTable
                .join(balanceKTable, customerBalanceJoiner)
                .filter((key,value) -> (value != null))
                .toStream()
                .to(propertiesConfig.getCustomerBalanceTopic(), Produced.with(keySerde, customerBalanceSerde));

        return streamsBuilder;

    }


}
