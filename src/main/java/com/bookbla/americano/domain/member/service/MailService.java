package com.bookbla.americano.domain.member.service;

public interface MailService {

    void sendTransactionFailureEmail(String transactionName, String message);

}
