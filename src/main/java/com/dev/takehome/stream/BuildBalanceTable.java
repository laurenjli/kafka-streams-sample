package com.dev.takehome.stream;


import com.dev.takehome.config.PropertiesConfig;
import com.dev.takehome.reducer.BalanceReducer;
import com.ibm.gbs.schema.Transaction;
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
 * This class builds the Balance KTable that will be joined with the Customer KTable.
 * The Balance topic has AccountId as the key which is the key which we would like to join with Customer.
 * However, in some of my investigation, the key had a few leading characters. So, to be safe,
 * the Balance stream is also re-keyed to have the AccountId as the key and then it is converted into a KTable.
 *
 * Reference: https://kafka.apache.org/20/documentation/streams/developer-guide/dsl-api.html
 * Reference: https://betterprogramming.pub/how-to-use-stateful-operations-in-kafka-streams-1cff4da41329
 */

@Slf4j
@Component
public class BuildBalanceTable {

    @Autowired protected Serde<String> keySerde;
    @Autowired protected Serde<Transaction> balanceSerde;
    @Autowired protected PropertiesConfig propertiesConfig;

    public KTable<String, Transaction> buildBalanceStream(
            StreamsBuilder streamsBuilder,
            BalanceReducer balanceReducer,
            Materialized<String, Transaction, KeyValueStore<Bytes, byte[]>> balanceStateStore
    ) {
        return streamsBuilder
                .stream(propertiesConfig.getBalanceTopic(), Consumed.with(keySerde, balanceSerde))
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
                .groupByKey(Grouped.with(keySerde, balanceSerde))
                .reduce(balanceReducer, balanceStateStore);

    }
}
