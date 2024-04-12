package org.ksu.schedule.rest.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    @GetMapping("/user")
    public String user(){
        return "<h2>User</h2>";
    }

    @GetMapping("/admin")
    public String admin(){
        return "<h2>Admin</h2>";
    }
}
