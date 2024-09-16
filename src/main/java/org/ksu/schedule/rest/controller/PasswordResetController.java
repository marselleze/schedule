package org.ksu.schedule.rest.controller;

import jakarta.servlet.http.HttpSession;
import org.ksu.schedule.domain.PasswordResetToken;
import org.ksu.schedule.service.impl.PasswordResetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/password-reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetServiceImpl passwordResetService;

    /**
     * Обработка запроса на сброс пароля.
     *
     * @param email Электронная почта пользователя.
     * @return Ответ о результате создания токена для сброса пароля.
     */
    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        PasswordResetToken token = passwordResetService.createPasswordResetToken(request.getEmail());
        if (token == null) {
            return ResponseEntity.badRequest().body("Invalid email address.");
        }
        return ResponseEntity.ok("Password reset token generated and email sent.");
    }

    /**
     * Отображение страницы сброса пароля.
     *
     * @param token Токен для сброса пароля.
     * @param model Модель для передачи данных на страницу.
     * @return Имя HTML файла для отображения.
     */
    @GetMapping("/confirm")
    public String showPasswordResetPage(@RequestParam("token") String token, HttpSession session) {
        session.setAttribute("resetToken", token);
        return "resetPassword";  // Имя HTML файла
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPasswordReset(HttpSession session, @RequestParam("newPassword") String newPassword) {
        String token = (String) session.getAttribute("resetToken");
        if (token == null) {
            return ResponseEntity.badRequest().body("No reset token found in session.");
        }
        boolean result = passwordResetService.resetPassword(token, newPassword);
        if (!result) {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}
