package com.dev.takehome.stream;


import com.dev.takehome.config.PropertiesConfig;
import com.dev.takehome.reducer.CustomerReducer;
import com.ibm.gbs.schema.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class builds the Customer KTable that will be joined with the Balance KTable.
 * Since the Customer and Balance topics have different keys, the Customer topic is first streamed,
 * then it is re-keyed to have AccountId as the key. Then, it is converted into a KTable.
 *
 * Reference: https://kafka.apache.org/20/documentation/streams/developer-guide/dsl-api.html
 * Reference: https://betterprogramming.pub/how-to-use-stateful-operations-in-kafka-streams-1cff4da41329
 */

@Slf4j
@Component
public class BuildCustomerTable {

    @Autowired protected Serde<String> keySerde;
    @Autowired protected Serde<Customer> customerSerde;
    @Autowired protected PropertiesConfig propertiesConfig;

    public KTable<String, Customer> buildCustomerStream(
            StreamsBuilder streamsBuilder,
            CustomerReducer customerReducer,
            Materialized<String, Customer, KeyValueStore<Bytes, byte[]>> customerStateStore
    ) {
        return streamsBuilder
                .stream(propertiesConfig.getCustomerTopic(), Consumed.with(keySerde, customerSerde))
                .map(
                        (key, value) -> {
                            try {
                                String accountId = value.getAccountId().toString();
                                return KeyValue.pair(
                                        accountId,
                                        value
                                );
                            }
                            catch (Exception e) {
                                log.error("Not able to rekey");
                                return KeyValue.pair(null,null);
                            }
                        })
                .filter((key, value) -> value != null)
                .groupByKey(Grouped.with(keySerde, customerSerde))
                .reduce(customerReducer, customerStateStore);

    }
}
