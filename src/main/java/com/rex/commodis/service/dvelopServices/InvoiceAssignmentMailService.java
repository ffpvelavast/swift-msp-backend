/** Service handling InvoiceAssignmentMail operations; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service.dvelopServices;

import com.rex.commodis.model.EmailAccount;
import com.rex.commodis.model.EmailDesign;
import com.rex.commodis.repository.EmailAccountRepository;
import com.rex.commodis.repository.EmailDesignRepository;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceAssignmentMailService {

    @Value("${app.notifications.admin-email}")
    private String adminEmail;

    private final EmailAccountRepository emailAccountRepository;
    private final EmailDesignRepository emailDesignRepository;

    private JavaMailSender invoiceMailSender;
    private String fromAddress;
    private String fromName;
    private String mailHeader;
    private String mailFooter;

    @PostConstruct
    public void init() {
        EmailAccount dbEmailAccount = emailAccountRepository.findFirstByOrderByIdAsc()
                .orElse(null);

        if (dbEmailAccount == null) {
            log.warn("No Email Account found in DB — invoice assignment mail sending will be disabled.");
            return;
        }

        EmailDesign dbEmailDesign = emailDesignRepository.findFirstByOrderByIdAsc()
                .orElse(null);

        if (dbEmailDesign == null) {
            log.warn("No Email Design found in DB — invoice assignment mail sending will be disabled.");
            return;
        }

        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(dbEmailAccount.getEmailServer());
        javaMailSenderImpl.setPort(dbEmailAccount.getEmailPort().intValue());
        javaMailSenderImpl.setUsername(dbEmailAccount.getAccountUser());
        javaMailSenderImpl.setPassword(dbEmailAccount.getAccountPassword());

        Properties properties = javaMailSenderImpl.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        if (Boolean.TRUE.equals(dbEmailAccount.getEmailSsl())) {
            properties.put("mail.smtp.ssl.enable", "true");
        } else {
            properties.put("mail.smtp.starttls.enable", "true");
        }

        this.invoiceMailSender = javaMailSenderImpl;
        this.fromAddress = dbEmailAccount.getSenderAddress();
        this.fromName = dbEmailAccount.getSenderName();
        this.mailHeader = dbEmailDesign.getMailHeader();
        this.mailFooter = dbEmailDesign.getMailFooter();
    }

    public void sendAssignmentMail(String emailAddress, String recipientName,
                                   Set<String> projectNumbers, String documentLink) {

        String projectNumberText = projectNumbers.stream()
                .map(projectNumber -> "- " + projectNumber)
                .collect(Collectors.joining("\n"));

        String body = """
                Dear %s,

                A new invoice has been assigned to you for processing.

                Project numbers: 
                %s
                
                Document link: %s
                
                Please review and process it at your earliest convenience.

                Best Regards,
                The REX Team
                
                This is an automated notification. Please do not reply to this email.
                """.formatted(recipientName, projectNumberText, documentLink);

        sendMail(emailAddress, "New Invoice Assigned", body);
    }

    public void sendInvoiceAssignmentFailureMail(String emailAddress, String tableName, String fieldName) {
        String body = """
            Dear User,

            An invoice intended for your processing could not be assigned because required data 
            was missing during import.

            Table: %s
            Missing field: %s

            Please contact "support@commodis.org" for further assistance.

            Best Regards,
            The REX Team

            This is an automated notification. Please do not reply to this email.
            """.formatted(tableName, fieldName);

        sendMail(emailAddress, "Invoice Assignment Failed", body);
    }

    public void sendMissingRequiredFieldEmail(String tableName, String fieldName) {
        String body = """
                Dear Admin,
                
                A required field was found to be missing during invoice processing.
                
                Table: %s
                Missing field: %s
                
                Please review the source payload and correct the missing data.
                
                Best Regards,
                The REX Team
    
                This is an automated notification. Please do not reply to this email.
                """.formatted(tableName, fieldName);

        sendMail(adminEmail, "Missing Field Required", body);
    }

    public void sendMissingRexUserEmail(String verifierEmail) {
        String body = """
                Dear Admin,

                A corresponding REX user for the following email was not found:
                %s

                Please verify the user details before retrying.

                Best Regards,
                The REX Team
                
                This is an automated notification. Please do not reply to this email.
                """.formatted(verifierEmail);

        sendMail(adminEmail, "REX User Not Found", body);
    }

    public void sendMissingProjectNumberEmail(String projectNumber) {
        String body = """
                Dear Admin,

                A corresponding project number was not found for:
                %s

                Please verify the project number before retrying.

                Best Regards,
                The REX Team
                
                This is an automated notification. Please do not reply to this email.
                """.formatted(projectNumber);

        sendMail(adminEmail, "Project Number Not Found", body);
    }

    private void sendMail(String to, String subject, String body) {
        if (invoiceMailSender == null) {
            log.warn("Mail sender not initialized — skipping email to {}", to);
            return;
        }
        try {
            MimeMessage message = invoiceMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(fromAddress, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mailHeader + body.replace("\n", "<br>") + mailFooter, true);

            invoiceMailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send mail to {}: {}", to, e.getMessage());
        }
    }
}
