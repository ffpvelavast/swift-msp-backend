package com.rex.commodis.service;

import com.rex.commodis.model.EmailAccount;
import com.rex.commodis.model.EmailDesign;
import com.rex.commodis.repository.EmailAccountRepository;
import com.rex.commodis.repository.EmailDesignRepository;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Properties;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamEventMailService {

    private final EmailAccountRepository emailAccountRepository;
    private final EmailDesignRepository emailDesignRepository;

    private JavaMailSender teamEventMailSender;
    private String fromAddress;
    private String fromName;
    private String mailHeader;
    private String mailFooter;

    @PostConstruct
    public void init() {
        EmailAccount dbEmailAccount = emailAccountRepository.findFirstByOrderByIdAsc()
                .orElse(null);

        if (dbEmailAccount == null) {
            log.warn("No Email Account found in DB — team event mail sending will be disabled.");
            return;
        }

        log.info(
                "Email account loaded from DB. SMTP Host={}, Port={}, Sender={}",
                dbEmailAccount.getEmailServer(),
                dbEmailAccount.getEmailPort(),
                dbEmailAccount.getSenderAddress()
        );

        EmailDesign dbEmailDesign = emailDesignRepository.findFirstByOrderByIdAsc()
                .orElse(null);

        if (dbEmailDesign == null) {
            log.warn("No Email Design found in DB — team event mail sending will be disabled.");
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

        this.teamEventMailSender = javaMailSenderImpl;
        this.fromAddress = dbEmailAccount.getSenderAddress();
        this.fromName = dbEmailAccount.getSenderName();
        this.mailHeader = dbEmailDesign.getMailHeader();
        this.mailFooter = dbEmailDesign.getMailFooter();
    }

    public void sendTeamEventReadyForApprovalMail(String emailAddress, String recipientName, String eventTitle, LocalDate eventDate) {
        String body = """
            <div style="font-family: Arial, Helvetica, sans-serif; font-size: 14px; color: #333333; line-height: 1.6;">
            
                <p>Dear <strong>%s</strong>,</p>
            
                <p>
                    A team event has been <strong>released for processing</strong> and is now awaiting your approval.
                </p>

                <div style="
                    background-color: #f8f9fa;
                    border: 1px solid #dcdcdc;
                    border-radius: 6px;
                    padding: 15px;
                    margin: 20px 0;
                ">
                    <p style="margin: 0 0 10px 0;">
                        <strong>Event Title:</strong> %s
                    </p>
            
                    <p style="margin: 0;">
                        <strong>Event Date:</strong> %s
                    </p>
                </div>
            
                <p>
                    Please review and approve this team event at your earliest convenience.
                </p>
            
                <br>
            
                <p style="margin-bottom: 0;">
                    Best Regards,<br>
                    <strong>The REX Team</strong>
                </p>
            
                <hr style="border: none; border-top: 1px solid #dddddd; margin: 20px 0;">
            
                <p style="font-size: 12px; color: #777777;">
                    This is an automated notification. Please do not reply to this email.
                </p>
            
            </div>
            """.formatted(recipientName, eventTitle, eventDate);

        sendMail(emailAddress, "Team Event Ready For Approval", body);
    }

    private void sendMail(String to, String subject, String body) {
        if (teamEventMailSender == null) {
            log.warn("Mail sender not initialized — skipping email to {}", to);
            return;
        }
        try {
            MimeMessage message = teamEventMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(fromAddress, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mailHeader + body.replace("\n", "<br>") + mailFooter, true);

            teamEventMailSender.send(message);

            log.info("Email sent successfully for team event approval");
        } catch (Exception e) {
            log.error("Failed to send mail to {}: {}", to, e.getMessage());
        }
    }
}
