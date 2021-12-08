package service.mail;

public class ActivateAccountEmailModel extends EmailModel{
    private String name;
    private String token;

    public ActivateAccountEmailModel(String recipientEmail, String name, String token){
        this.recipientEmail = recipientEmail;
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}
