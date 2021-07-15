package com.dev.takehome.reducer;

import com.ibm.gbs.schema.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.Reducer;
import org.springframework.stereotype.Component;

/**
 * This class implements a reducer that is used to transform the Customer topic KStream -> KTable.
 *
 * reference: https://stackoverflow.com/questions/53495828/kafka-streams-ktable-creation-from-a-given-topic
 * reference: https://stackoverflow.com/questions/42937057/kafka-streams-api-kstream-to-ktable
 */

@Component
@Slf4j
public class CustomerReducer implements Reducer<Customer> {

    @Override
    public Customer apply(Customer existingValue, Customer newValue){
        //initialize return value
        Customer finalResult = newValue;
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
