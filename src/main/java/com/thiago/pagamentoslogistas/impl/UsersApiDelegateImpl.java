package com.thiago.pagamentoslogistas.impl;

import com.thiago.pagamentoslogistas.domain.entity.User;
import com.thiago.pagamentoslogistas.model.dto.UsersRequestV1;
import com.thiago.pagamentoslogistas.model.dto.UsersResponseV1;
import com.thiago.pagamentoslogistas.rest.UsersApiDelegate;
import com.thiago.pagamentoslogistas.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsersApiDelegateImpl implements UsersApiDelegate {

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<Void> insertUsers(UsersRequestV1 usersRequestV1){
        User newUser = userService.createUser(usersRequestV1);
    return null;
    }

    @Override
    public ResponseEntity<UsersResponseV1> searchUsers(String document){
        UsersResponseV1 usersResponseV1 = userService.findUserByDocument(document);
        return new ResponseEntity<>(usersResponseV1, HttpStatus.OK);
    }

}
