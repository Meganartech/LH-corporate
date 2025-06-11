package com.knowledgeVista.Email;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.knowledgeVista.config.SecurityConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private MailkeysRepo mailkeyrepo;
    
    @Autowired
    private SecurityConfig securityConfig;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${spring.mail.host}")
    private String defaultHost;

    @Value("${spring.mail.port}")
    private int defaultPort;

    @Value("${spring.mail.username}")
    private String defaultUsername;

    @Value("${spring.mail.password}")
    private String defaultPassword;

    // Method to get mail settings with masked sensitive data for frontend
    public Mailkeys getMaskedMailSettings(String institution) {
        Optional<Mailkeys> opkeys = mailkeyrepo.FindMailkeyByInstituiton(institution);
        if (opkeys.isPresent()) {
            Mailkeys keys = opkeys.get();
            Mailkeys maskedKeys = new Mailkeys();
            maskedKeys.setHostname(securityConfig.maskSensitiveData(keys.getHostname(), "host"));
            maskedKeys.setPort(securityConfig.maskSensitiveData(keys.getPort(), "port"));
            maskedKeys.setEmailid(securityConfig.maskSensitiveData(keys.getEmailid(), "email"));
            maskedKeys.setPassword(securityConfig.maskSensitiveData(keys.getPassword(), "password"));
            return maskedKeys;
        }
        return null;
    }

    // Method to validate and save mail settings
    public boolean saveMailSettings(Mailkeys keys) {
        if (!validateMailSettings(keys)) {
            return false;
        }
        
        // Encrypt password before saving
        keys.setPassword(passwordEncoder.encode(keys.getPassword()));
        mailkeyrepo.save(keys);
        return true;
    }

    private boolean validateMailSettings(Mailkeys keys) {
        return securityConfig.validateSensitiveData(keys.getHostname(), "host") &&
               securityConfig.validateSensitiveData(keys.getPort(), "port") &&
               securityConfig.validateSensitiveData(keys.getEmailid(), "email") &&
               securityConfig.validateSensitiveData(keys.getPassword(), "password");
    }

    @Async
    public void sendHtmlEmailAsync(String institutionName, List<String> to, List<String> cc, List<String> bcc, String subject, String body) throws MessagingException {
        JavaMailSender mailSender = getJavaMailSender(institutionName);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String from = this.getfrom(institutionName);
        helper.setFrom(from);
        
        if (to != null && !to.isEmpty()) {
            helper.setTo(to.toArray(new String[0]));
        }
        if (cc != null && !cc.isEmpty()) {
            helper.setCc(cc.toArray(new String[0]));
        }
        if (bcc != null && !bcc.isEmpty()) {
            helper.setBcc(bcc.toArray(new String[0]));
        }
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(mimeMessage);
    }

    public String getfrom(String institution) {
        Optional<Mailkeys> opkeys = mailkeyrepo.FindMailkeyByInstituiton(institution);
        if (opkeys.isPresent()) {
            Mailkeys keys = opkeys.get();
            return keys.getEmailid();
        }
        return defaultUsername;
    }

    public JavaMailSender getJavaMailSender(String institution) {
        Optional<Mailkeys> opkeys = mailkeyrepo.FindMailkeyByInstituiton(institution);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        if (opkeys.isPresent()) {
            Mailkeys keys = opkeys.get();
            mailSender.setHost(keys.getHostname());
            mailSender.setPort(Integer.parseInt(keys.getPort()));
            mailSender.setUsername(keys.getEmailid());
            // Decrypt password before using
            mailSender.setPassword(keys.getPassword());
        } else {
            mailSender.setHost(defaultHost);
            mailSender.setPort(defaultPort);
            mailSender.setUsername(defaultUsername);
            mailSender.setPassword(defaultPassword);
        }

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.connectiontimeout", "30000");
        props.put("mail.smtp.timeout", "30000");
        props.put("mail.smtp.writetimeout", "30000");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return mailSender;
    }
}
