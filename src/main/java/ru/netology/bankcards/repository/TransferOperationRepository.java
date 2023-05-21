package ru.netology.bankcards.repository;

import org.springframework.stereotype.Repository;
import ru.netology.bankcards.model.TransferOperation;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransferOperationRepository {

    private static final Map<String, TransferOperation> mapTransferOperation = new ConcurrentHashMap<>();

    public String save(TransferOperation transferOperation) {
        String uuid = UUID.randomUUID().toString();
        mapTransferOperation.put(uuid,transferOperation);
        return uuid;
    }

    public TransferOperation getById (String id){
       return mapTransferOperation.get(id);
    }

}
