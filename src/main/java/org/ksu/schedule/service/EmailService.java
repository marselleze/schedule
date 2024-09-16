package org.ksu.schedule.service;

public interface EmailService {
    void sendPasswordResetEmail(String to, String token);
}
