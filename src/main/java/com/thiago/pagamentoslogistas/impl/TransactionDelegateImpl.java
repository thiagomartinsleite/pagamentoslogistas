package com.thiago.pagamentoslogistas.impl;

import com.thiago.pagamentoslogistas.model.dto.TransactionRequestV1;
import com.thiago.pagamentoslogistas.model.dto.TransactionResponsetV1;
import com.thiago.pagamentoslogistas.rest.TransactionApiDelegate;
import com.thiago.pagamentoslogistas.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionDelegateImpl implements TransactionApiDelegate {

    @Autowired
    private TransactionService transactionService;

    @Override
    public ResponseEntity<TransactionResponsetV1> insertTransaction(TransactionRequestV1 transactionRequestV1) {
        return new ResponseEntity<>(transactionService.createTransaction(transactionRequestV1), HttpStatus.OK);
    }
}
