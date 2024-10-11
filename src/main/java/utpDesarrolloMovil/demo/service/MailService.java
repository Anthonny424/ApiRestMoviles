package utpDesarrolloMovil.demo.service;


import org.springframework.stereotype.Service;
import utpDesarrolloMovil.demo.configuration.MailManager;

@Service
public class MailService {

    MailManager mailManager;

    public MailService(MailManager mailManager) {
        this.mailManager = mailManager;
    }

    public void sendMessageUser(String email, String message){
        mailManager.sendMessage(email, message);
    }
}
