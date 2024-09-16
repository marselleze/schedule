package org.ksu.schedule.service.impl;

import org.ksu.schedule.domain.PasswordResetToken;
import org.ksu.schedule.domain.User;
import org.ksu.schedule.repository.PasswordResetTokenRepository;
import org.ksu.schedule.repository.UserRepository;
import org.ksu.schedule.service.EmailService;
import org.ksu.schedule.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    @Override
    public PasswordResetToken createPasswordResetToken(String email) {
        Logger logger = Logger.getLogger(PasswordResetService.class.getName());
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent()) {
            return null;
        }

        User user = optionalUser.get();


        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(24));
        passwordResetTokenRepository.save(token);

        emailService.sendPasswordResetEmail(user.getEmail(), token.getToken());

        return token;
    }


    @Override
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        Logger logger = Logger.getLogger(PasswordResetService.class.getName());
        if (resetToken == null) {
            //logger.severe("Password reset token not found: " + token);
            return false;
        }
        if (resetToken.isExpired()) {
            //logger.severe("Password reset token has expired: " + token);
            return false;
        }


        //logger.info("Password reset token found: " + token);

        User user = resetToken.getUser();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        //logger.info("Non-encoded password: " + newPassword);
        //logger.info("Encoded password: " + encodedPassword);
        //logger.info("Attempting to save new password for user: " + user.getEmail());
        userRepository.save(user);
        logger.info("Password successfully updated for user: " + user.getEmail());

        return true;
    }
}
