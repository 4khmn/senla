package bank.consumer.repository;

import bank.common.TransferMessage;
import bank.common.TransferStatus;

public interface TransferRepository {
    void save(TransferMessage msg, TransferStatus status);
}
