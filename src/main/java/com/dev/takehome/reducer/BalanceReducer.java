package com.dev.takehome.reducer;

import com.ibm.gbs.schema.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.Reducer;
import org.springframework.stereotype.Component;

/**
 * This class implements a reducer that is used to transform the Balance topic KStream -> KTable.
 *
 * reference: https://stackoverflow.com/questions/53495828/kafka-streams-ktable-creation-from-a-given-topic
 * reference: https://stackoverflow.com/questions/42937057/kafka-streams-api-kstream-to-ktable
 */

@Component
@Slf4j
public class BalanceReducer implements Reducer<Transaction> {

    @Override
    public Transaction apply(Transaction existingValue, Transaction newValue){
        //initialize return value
        Transaction finalResult = newValue;
        if (existingValue == null){
            return newValue;
        }
        if (newValue == null) {
            return null;
        }
        if (!existingValue.getAccountId().equals(newValue.getAccountId())) {
            finalResult = existingValue;
        }
        return finalResult;

    }
}
