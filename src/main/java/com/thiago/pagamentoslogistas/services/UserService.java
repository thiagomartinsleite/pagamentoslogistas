package com.thiago.pagamentoslogistas.services;

import com.thiago.pagamentoslogistas.configuration.ModelMapperConfiguration;
import com.thiago.pagamentoslogistas.domain.entity.User;
import com.thiago.pagamentoslogistas.domain.enuns.UserType;
import com.thiago.pagamentoslogistas.exceptions.BusinessException;
import com.thiago.pagamentoslogistas.model.dto.UsersRequestV1;
import com.thiago.pagamentoslogistas.model.dto.UsersResponseV1;
import com.thiago.pagamentoslogistas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapperConfiguration modelMapperConfiguration;

    public void validateTransaction(User sender, BigDecimal amount){
        if(sender.getUserType().equals(UserType.MERCHANT))
            throw new BusinessException("usuário do tipo lojista não esta autorizado para realizar transações");
        if(sender.getBalance().compareTo(amount) < 0)
            throw new BusinessException("Saldo insuficiente");
    }


    public User findUserById(Long id){
        return this.userRepository.findUserById(id).orElseThrow(() -> new BusinessException("Usuario não encontrado"));
    }

    public UsersResponseV1 findUserByDocument(String document) {
        User user = this.userRepository.findUserByDocument(document).orElseThrow(() -> new BusinessException("Usuario não encontrado"));
        return modelMapperConfiguration.modelMapper().map(user,UsersResponseV1.class);
    }

    public void saveUser(User user){
        this.userRepository.save(user);
    }

    public User createUser(UsersRequestV1 item){
        User newUser = modelMapperConfiguration.modelMapper().map(item,User.class);
        this.saveUser(newUser);
        return newUser;
    }
}
