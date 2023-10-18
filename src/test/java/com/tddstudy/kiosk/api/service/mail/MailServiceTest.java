package com.tddstudy.kiosk.api.service.mail;

import com.tddstudy.kiosk.client.mail.MailClient;
import com.tddstudy.kiosk.domain.history.mail.MailSendHistory;
import com.tddstudy.kiosk.domain.history.mail.MailSendHistoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * 순수 Mockito Test
 */
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private MailClient mailClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
//        MailClient mailClient = Mockito.mock(MailClient.class);
//        MailSendHistoryRepository mailSendHistoryRepository = mock(MailSendHistoryRepository.class);
//        MailService mailService = new MailService(mailClient, mailSendHistoryRepository);

        Mockito.when(mailClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        boolean result = mailService.sendMail("", "", "", "");

        Assertions.assertThat(result).isTrue();
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }
}