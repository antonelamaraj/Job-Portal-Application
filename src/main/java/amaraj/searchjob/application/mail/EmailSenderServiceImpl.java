package amaraj.searchjob.application.mail;

import amaraj.searchjob.application.dao.CompanyRepository;
import amaraj.searchjob.application.entity.Company;
import amaraj.searchjob.application.service.CompanyService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

  private final JavaMailSender mailSender;
  private final CompanyRepository companyRepository;

    @Override
    public void sendEmail(Long companyId, String from, String to, String subject, String message) {
    Company company = companyRepository.findById(companyId).orElse(null);
    if (company == null){
        throw new IllegalArgumentException("Company not found for ID: " + companyId);
    }
    String companyEmail = company.getEmail();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);   //ktu duhet te vendos emailine Companise
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        simpleMailMessage.setReplyTo(companyEmail);
        this.mailSender.send(simpleMailMessage);
    }

    @Override
    public String sendEmailWithAttachment(String from, String to, String subject, String message) {
        try {
            MimeMessage messageMim = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(messageMim, true);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(message);

            File file = new File("C:\\Users\\admin\\OneDrive\\Desktop\\hello.txt");
            messageHelper.addAttachment(file.getName(), file);
            mailSender.send(messageMim);
            return "Mail sent succesfulluy";
        } catch (Exception e) {

            return "Mail sent failed!";
        }
    }
}
