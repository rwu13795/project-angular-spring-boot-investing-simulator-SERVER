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
    private final SendGrid sendGridClient;

    @Autowired
    public SendGridService(EnvVariable env) {

        this.sendGridClient = new SendGrid(env.SENDGRID_API_KEY());
    }

    public void sendResetPasswordLink(String email, String token) throws IOException {

        String href = "<b><a href='http://localhost:4200/user/reset-password?token=" + token + "' >link</a></b>";
        String link = "<div style='color: black;'>Please click this " + href
                + " to open the page for resetting your password.</div>";

        Email from = new Email("rwu13795.work@gmail.com");
        String subject = "[Investing Simulator] Reset Password Link";
        Email to = new Email(email);
        Content content = new Content("text/html", link);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGridClient.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }
}
