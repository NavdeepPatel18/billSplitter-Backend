package com.navdeep.billsplitter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    @GetMapping
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Ok Auth is working.");
    }
}
