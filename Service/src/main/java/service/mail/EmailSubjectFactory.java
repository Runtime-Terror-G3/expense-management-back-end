package service.mail;

public abstract class EmailSubjectFactory {
    public static String getSubject(EmailType emailType){
        switch (emailType){
            case ACTIVATE_ACCOUNT:
                return "Activate account";
        }
        return null;
    }
}
