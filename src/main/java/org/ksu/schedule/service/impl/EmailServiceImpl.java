package org.ksu.schedule.service.impl;

import org.ksu.schedule.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = Logger.getLogger(EmailServiceImpl.class.getName());

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        logger.info("Начинается процесс отправки письма для сброса пароля на адрес: " + to);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("ksu@24schedule.ru"); // Устанавливаем новый адрес отправителя
        message.setSubject("Запрос на сброс пароля");
        message.setText("Для сброса пароля перейдите по ссылке:\n"
                + "https://24schedule.ru/password-reset/confirm?token=" + token);

        try {
            mailSender.send(message);
            logger.info("Письмо для сброса пароля успешно отправлено на адрес: " + to);
        } catch (Exception e) {
            logger.severe("Ошибка при отправке письма для сброса пароля на адрес: " + to + ". Ошибка: " + e.getMessage());
        }
    }
}
