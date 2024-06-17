package com.bookbla.americano.domain.notification.service;

public interface MailService {
    void sendTransactionFailureEmail(String transactionName, String message);

}
