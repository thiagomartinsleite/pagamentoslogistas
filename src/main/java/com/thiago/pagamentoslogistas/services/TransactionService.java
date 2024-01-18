package com.thiago.pagamentoslogistas.services;

import com.thiago.pagamentoslogistas.configuration.ModelMapperConfiguration;
import com.thiago.pagamentoslogistas.domain.entity.Transaction;
import com.thiago.pagamentoslogistas.domain.entity.User;
import com.thiago.pagamentoslogistas.dtos.TransactionDTO;
import com.thiago.pagamentoslogistas.exceptions.BusinessException;
import com.thiago.pagamentoslogistas.model.dto.TransactionRequestV1;
import com.thiago.pagamentoslogistas.model.dto.TransactionResponsetV1;
import com.thiago.pagamentoslogistas.model.dto.UsersResponseV1;
import com.thiago.pagamentoslogistas.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userImpl;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelMapperConfiguration modelMapper;

    public TransactionResponsetV1 createTransaction(TransactionRequestV1 transactionRequestV1){

        User sender = this.userImpl.findUserById(transactionRequestV1.getSenderId());
        User receiver = this.userImpl.findUserById(transactionRequestV1.getReceiverId());

        userImpl.validateTransaction(sender,transactionRequestV1.getAmount());

        if(!this.autorizeTransaction(sender,transactionRequestV1.getAmount())){
            throw new BusinessException("Transação não autorizada");
        }

        Transaction newTransaction = Transaction.builder().amount(transactionRequestV1.getAmount())
                                                          .receiver(receiver)
                                                          .sender(sender)
                                                          .timestamp(LocalDateTime.now()).build();

        sender.setBalance(sender.getBalance().subtract(transactionRequestV1.getAmount()));
        receiver.setBalance(receiver.getBalance().add(transactionRequestV1.getAmount()));

        this.transactionRepository.save(newTransaction);
        this.userImpl.saveUser(sender);
        this.userImpl.saveUser(receiver);

        this.notificationService.sendNotification(sender,"transação enviada com sucesso!!");
        this.notificationService.sendNotification(receiver,"transação recebida com sucesso!!");

        return mapperTransaction(newTransaction);
    }

    public boolean autorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<Map> autorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/5ee477dc-3b09-40e0-a9eb-30310e61b1a2", Map.class);

        if(autorizationResponse.getStatusCode().equals(HttpStatus.OK)){
            return autorizationResponse.getBody().get("message").equals("Autorizado");
        }else{
            return false;
        }
    }
    private TransactionResponsetV1 mapperTransaction(Transaction transaction){
        TransactionResponsetV1 responsetV1 = new TransactionResponsetV1();
        responsetV1.setId(transaction.getId());
        responsetV1.setAmount(transaction.getAmount());
        responsetV1.setSenderId(modelMapper.modelMapper().map(transaction.getSender(), UsersResponseV1.class));
        responsetV1.setReceiverId(modelMapper.modelMapper().map(transaction.getReceiver(), UsersResponseV1.class));
        return responsetV1;
    }
}
