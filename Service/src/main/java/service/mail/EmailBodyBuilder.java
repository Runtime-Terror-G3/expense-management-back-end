package service.mail;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public abstract class EmailBodyBuilder {
    protected String body;
    protected String bodyFilePath;

    public static EmailBodyBuilder getBuilder(EmailType emailType){
        switch (emailType){
            case ACTIVATE_ACCOUNT:
                return new ActivateAccountEmailBodyBuilder();
        }
        return null;
    }

    EmailBodyBuilder loadBodyFromFile(){
        URL url = getClass()
                .getClassLoader()
                .getResource(bodyFilePath);
        if (url == null){
            throw new IllegalArgumentException(bodyFilePath+ " not found");
        }
        File bodyFile = new File(url.getFile());
        try {
            body = Files.readString(bodyFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    abstract EmailBodyBuilder personalizeBody(EmailModel model);

    public String build(){
        return body;
    }
}
