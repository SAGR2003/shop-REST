package com.shop.service.exception;

public class DailyTransactionLimitExceededException extends RuntimeException {
    public DailyTransactionLimitExceededException(int document) {
        super("Document: " + document + ", can't make more transactions for today, remember there are 3 per day");
    }
}
