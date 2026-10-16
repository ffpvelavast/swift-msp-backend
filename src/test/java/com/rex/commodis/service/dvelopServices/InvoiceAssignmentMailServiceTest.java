package com.rex.commodis.service.dvelopServices;

import com.rex.commodis.model.EmailAccount;
import com.rex.commodis.model.EmailDesign;
import com.rex.commodis.repository.EmailAccountRepository;
import com.rex.commodis.repository.EmailDesignRepository;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceAssignmentMailServiceTest {

    @Mock private EmailAccountRepository emailAccountRepository;
    @Mock private EmailDesignRepository emailDesignRepository;
    @Mock private JavaMailSender mailSender;

    private InvoiceAssignmentMailService service;

    @BeforeEach
    void setUp() {
        service = new InvoiceAssignmentMailService(emailAccountRepository, emailDesignRepository);
    }

    @Test
    void init_loadsEmailAccountAndDesignIntoMailConfiguration() {
        when(emailAccountRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(emailAccount(true)));
        when(emailDesignRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(emailDesign()));

        service.init();

        JavaMailSenderImpl configuredSender =
                (JavaMailSenderImpl) ReflectionTestUtils.getField(service, "invoiceMailSender");
        assertThat(configuredSender.getHost()).isEqualTo("smtp.example.com");
        assertThat(configuredSender.getPort()).isEqualTo(465);
        assertThat(configuredSender.getUsername()).isEqualTo("smtp-user");
        assertThat(ReflectionTestUtils.getField(service, "fromAddress")).isEqualTo("noreply@example.com");
        assertThat(ReflectionTestUtils.getField(service, "fromName")).isEqualTo("REX");
        assertThat(ReflectionTestUtils.getField(service, "mailHeader")).isEqualTo("<header>");
        assertThat(ReflectionTestUtils.getField(service, "mailFooter")).isEqualTo("<footer>");
        assertThat(configuredSender.getJavaMailProperties().get("mail.smtp.ssl.enable")).isEqualTo("true");
    }

    @Test
    void init_whenEmailAccountMissing_disablesMailSender() {
        when(emailAccountRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());

        assertThatCode(() -> service.init()).doesNotThrowAnyException();

        assertThat(ReflectionTestUtils.getField(service, "invoiceMailSender")).isNull();
    }

    @Test
    void init_whenEmailDesignMissing_disablesMailSender() {
        when(emailAccountRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(emailAccount(false)));
        when(emailDesignRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.empty());

        assertThatCode(() -> service.init()).doesNotThrowAnyException();

        assertThat(ReflectionTestUtils.getField(service, "invoiceMailSender")).isNull();
    }

    @Test
    void sendAssignmentMail_sendsMailWithProjectNumbersAndDocumentLink() throws Exception {
        MimeMessage message = mimeMessage();
        when(mailSender.createMimeMessage()).thenReturn(message);
        injectMailRuntime();

        service.sendAssignmentMail(
                "recipient@example.com",
                "Jane Doe",
                Set.of("100/200", "300/400"),
                "https://dms.example/document"
        );

        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        MimeMessage sent = messageCaptor.getValue();
        assertThat(sent.getSubject()).isEqualTo("New Invoice Assigned");
        assertThat(sent.getRecipients(Message.RecipientType.TO)[0].toString())
                .isEqualTo("recipient@example.com");
        String content = sent.getContent().toString();
        assertThat(content).contains("Dear Jane Doe");
        assertThat(content).contains("100/200");
        assertThat(content).contains("300/400");
        assertThat(content).contains("https://dms.example/document");
        assertThat(content).contains("<header>");
        assertThat(content).contains("<footer>");
    }

    @Test
    void sendMissingRexUserEmail_sendsAdminNotification() throws Exception {
        MimeMessage message = mimeMessage();
        when(mailSender.createMimeMessage()).thenReturn(message);
        injectMailRuntime();

        service.sendMissingRexUserEmail("missing@example.com");

        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        MimeMessage sent = messageCaptor.getValue();
        assertThat(sent.getSubject()).isEqualTo("REX User Not Found");
        assertThat(sent.getRecipients(Message.RecipientType.TO)[0].toString())
                .isEqualTo("admin@example.com");
        assertThat(sent.getContent().toString()).contains("missing@example.com");
    }

    private void injectMailRuntime() {
        ReflectionTestUtils.setField(service, "invoiceMailSender", mailSender);
        ReflectionTestUtils.setField(service, "fromAddress", "noreply@example.com");
        ReflectionTestUtils.setField(service, "fromName", "REX");
        ReflectionTestUtils.setField(service, "mailHeader", "<header>");
        ReflectionTestUtils.setField(service, "mailFooter", "<footer>");
        ReflectionTestUtils.setField(service, "adminEmail", "admin@example.com");
    }

    private MimeMessage mimeMessage() {
        return new MimeMessage(Session.getInstance(new Properties()));
    }

    private EmailAccount emailAccount(boolean ssl) {
        EmailAccount account = new EmailAccount();
        account.setEmailServer("smtp.example.com");
        account.setEmailPort((short) 465);
        account.setAccountUser("smtp-user");
        account.setAccountPassword("smtp-password");
        account.setSenderAddress("noreply@example.com");
        account.setSenderName("REX");
        account.setEmailSsl(ssl);
        return account;
    }

    private EmailDesign emailDesign() {
        EmailDesign design = new EmailDesign();
        design.setMailHeader("<header>");
        design.setMailFooter("<footer>");
        return design;
    }
}
