package service.mail;

public interface IEmailService {

    /**
     * Sends an email to the recipient specified by emailModel.recipientEmail
     * @param emailType - type of email to be sent
     * @param emailModel - model containing the email of the recipient and extra information needed for email body creation
     */
    void sendMail(EmailType emailType, EmailModel emailModel);
}
