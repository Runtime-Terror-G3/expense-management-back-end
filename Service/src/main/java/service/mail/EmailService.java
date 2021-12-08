package service.mail;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Service used to send emails via sendEmail method
 *
 * In order to send a different type of email
 * (1) define a new class that extends EmailModel to hold the extra info needed for email sending
 * (2) create a new html template in resources/mail folder
 * (3) add a new EmailType for this new type of email
 * (4) add a new case statement in the EmailSubjectFactory.getSubject
 * (5) add a new case statement in the EmailBodyBuilder.getBuilder
 * (6) create a new class that extends EmailBodyBuilder
 *       (6.1) define implicit constructor that sets bodyFilePath to point to html template created at (2)
 *       (6.2) override personalizeBody
 * (7) call EmailSender.sendEmail in code
 */
@Component
public class EmailService implements IEmailService {

    private Properties getProperties(){
        Properties props = new Properties();

        String mailConfigPath = getClass()
                .getClassLoader()
                .getResource("mail/mail.properties")
                .getPath();

        try {
            props.load(new FileInputStream(mailConfigPath));
        } catch (IOException e) {
            System.out.println("Error on loading mail.properties file");
            e.printStackTrace();
        }

        return props;
    }

    private Session getSession(Properties props){
        return Session.getDefaultInstance(
                props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication( props.getProperty("mail.account.address"), props.getProperty("mail.account.password") );
                    }
                }
        );
    }

    @Override
    public void sendMail(EmailType emailType, EmailModel emailModel) {

        Properties props = getProperties();
        Session session = getSession(props);

        try {

            MimeMessage message = new MimeMessage( session );

            message.setFrom( new InternetAddress( props.getProperty("mail.account.address") ) );
            message.setReplyTo(null);
            message.addRecipient( Message.RecipientType.TO, new InternetAddress( emailModel.getRecipientEmail() ) );

            message.setSubject( EmailSubjectFactory.getSubject(emailType) );
            message.setContent( EmailBodyBuilder
                            .getBuilder(emailType)
                            .loadBodyFromFile()
                            .personalizeBody(emailModel)
                            .build()
                    , "text/html; charset=utf-8" );

            Transport.send( message );
        }
        catch(MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }
}

