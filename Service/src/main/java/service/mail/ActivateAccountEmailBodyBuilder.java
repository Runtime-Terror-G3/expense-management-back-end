package service.mail;

public class ActivateAccountEmailBodyBuilder extends EmailBodyBuilder{

    protected ActivateAccountEmailBodyBuilder(){
        this.bodyFilePath = "mail/accountActivation.html";
    }

    @Override
    EmailBodyBuilder personalizeBody(EmailModel model) {
        ActivateAccountEmailModel activateAccountEmailModel = (ActivateAccountEmailModel)model;
        body = body.replace("{{name}}", activateAccountEmailModel.getName())
                .replace("{{token}}", activateAccountEmailModel.getToken());
        return this;
    }

}
