package com.dev.takehome.joiner;

import com.ibm.gbs.schema.Customer;
import com.ibm.gbs.schema.CustomerBalance;
import com.ibm.gbs.schema.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.stereotype.Component;

/**
 * This class implements the ValueJoiner class to join the Balance and Customer KTables
 * and return a new object Customer Balance
 *
 * reference: https://kafka-tutorials.confluent.io/foreign-key-joins/kstreams.html
 * reference https://kafka.apache.org/10/javadoc/org/apache/kafka/streams/kstream/ValueJoiner.html
 *
 */

@Slf4j
@Component
public class CustomerBalanceJoin implements
        ValueJoiner<Customer, Transaction, CustomerBalance> {
    public CustomerBalance apply(Customer customer, Transaction balance) {
        return CustomerBalance.newBuilder()
                .setAccountId(customer.getAccountId())
                .setCustomerId(customer.getCustomerId())
                .setPhoneNumber(customer.getPhoneNumber())
                .setBalance((balance.getBalance()))
                .build();
    }
}
