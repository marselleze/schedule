package org.ksu.schedule.service;

import org.ksu.schedule.domain.PasswordResetToken;

public interface PasswordResetService {

    PasswordResetToken createPasswordResetToken(String email);

    boolean resetPassword(String token, String newPassword);
}
