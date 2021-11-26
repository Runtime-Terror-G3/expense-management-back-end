package service.mail;

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
public class EmailService implements IEmailService {

    private static String host = "smtp.gmail.com";
    private static int port = 587; //465
    private static boolean debug = true;
    private static String senderEmail = "expense.management.noreply@gmail.com";
    private static String password = "expenseTEST2021!@#";


    private Properties getProperties(){
        Properties props = new Properties();

        props.put( "mail.smtp.auth", "true" );
        props.put( "mail.smtp.host", host );
        props.put( "mail.smtp.port", port );
        props.put( "mail.smtp.starttls.enable", "true" );
        props.put( "mail.debug", debug );
        props.put( "mail.smtp.socketFactory.port", port );
        props.put( "mail.smtp.socketFactory.fallback", "false" );
        props.put( "mail.smtp.ssl.trust", host );

        return props;
    }

    private Session getSession(Properties props){
        return Session.getDefaultInstance(
                props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication( senderEmail, password );
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

            message.setFrom( new InternetAddress( senderEmail ) );
            message.setReplyTo( InternetAddress.parse( senderEmail ) );
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

