package amaraj.searchjob.application.mail;

public interface EmailSenderService {

    void sendEmail(Long companyId, String from, String to, String subject, String message);

    String sendEmailWithAttachment(String from, String to, String subject, String messag);
}
