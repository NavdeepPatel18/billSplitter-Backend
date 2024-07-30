package com.navdeep.billsplitter.controller;

import com.navdeep.billsplitter.dto.GroupDetailRequest;
import com.navdeep.billsplitter.entity.GroupDetail;
import com.navdeep.billsplitter.service.GroupDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupDetailController {


    private final GroupDetailService groupDetailService;

    @PostMapping("/create")
    public ResponseEntity<String> createGroup(@RequestBody GroupDetailRequest groupDetailRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return groupDetailService.createGroup(groupDetailRequest,username);
    }
}
