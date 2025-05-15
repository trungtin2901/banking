package com.tinhuynhtrung.BankManager.Exception;

public class TransactionLimitExceededException extends RuntimeException{
    public TransactionLimitExceededException(String message){
        super(message);
    }
}
