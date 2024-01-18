package com.thiago.pagamentoslogistas.services;

import com.thiago.pagamentoslogistas.domain.entity.User;
import com.thiago.pagamentoslogistas.dtos.NotificationRequestDTO;
import com.thiago.pagamentoslogistas.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message){
        String email = user.getEmail();
        NotificationRequestDTO notificationRequestDTO = new NotificationRequestDTO(email,message);

        ResponseEntity<String> notificationResponse = restTemplate.postForEntity("https://run.mocky.io/v3/5ee477dc-3b09-40e0-a9eb-30310e61b1a2",notificationRequestDTO,String.class);

        if(!(notificationResponse.getStatusCode() == HttpStatus.OK)){
            System.out.println("Erro ao enviar notificação");
            throw new BusinessException("Servico de notificação fora do ar");
        }
        System.out.println("Usuario: " + user.getFirstName() + message );
    }
}
