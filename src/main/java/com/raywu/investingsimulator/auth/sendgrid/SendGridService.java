package com.raywu.investingsimulator.auth.sendgrid;

import com.raywu.investingsimulator.utility.EnvVariable;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridService {
    private SendGrid sendGridClient;

    @Autowired
    public SendGridService(EnvVariable env) {
        this.sendGridClient = new SendGrid(env.SENDGRID_API_KEY());
    }

    public void sendResetPasswordLink() throws IOException {
        Email from = new Email("rwu13795.work@gmail.com");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email("rwu13795@gmail.com");
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGridClient.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}