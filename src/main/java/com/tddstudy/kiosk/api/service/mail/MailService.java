package com.tddstudy.kiosk.api.service.mail;

import com.tddstudy.kiosk.client.mail.MailClient;
import com.tddstudy.kiosk.domain.history.mail.MailSendHistory;
import com.tddstudy.kiosk.domain.history.mail.MailSendHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final MailClient mailClient;
    private final MailSendHistoryRepository mailSendHistoryRepository;

    public boolean sendMail(String fromEmail, String toEmail, String subject, String content) {
        boolean result = mailClient.sendEmail(fromEmail, toEmail, subject, content);
        if (!result) {
            return false;
        }

        MailSendHistory mailSendHistory = MailSendHistory.builder()
                .fromEmail(fromEmail)
                .toEmail(toEmail)
                .subject(subject)
                .content(content)
                .build();
        mailSendHistoryRepository.save(mailSendHistory);
        return true;
    }
}
