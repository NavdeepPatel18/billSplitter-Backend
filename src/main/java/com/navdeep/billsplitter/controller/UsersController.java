package com.navdeep.billsplitter.controller;

import com.navdeep.billsplitter.dto.BillsRequestDTO;
import com.navdeep.billsplitter.dto.GroupListDTO;
import com.navdeep.billsplitter.dto.GroupMemberRequest;
import com.navdeep.billsplitter.entity.Bills;
import com.navdeep.billsplitter.entity.GroupDetail;
import com.navdeep.billsplitter.service.BillsService;
import com.navdeep.billsplitter.service.GroupDetailService;
import com.navdeep.billsplitter.service.GroupMemberService;
import com.navdeep.billsplitter.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UsersController {

    private final GroupDetailService groupDetailService;
    private final GroupMemberService groupMemberService;
    private final BillsService billsService;


    @GetMapping("/groups")
    public ResponseEntity<List<GroupListDTO>> groupList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(groupDetailService.getGroups(username));
    }

    @GetMapping("/groups/{group}/members")
    public ResponseEntity<List<String>> groupMemberList(@PathVariable UUID group){
        return groupDetailService.groupMemberList(group);
    }

    @PostMapping("/groups/member/add")
    public ResponseEntity<String> groupMemberAdd(@RequestBody GroupMemberRequest groupMemberRequest){
        System.out.println(groupMemberRequest);
        return groupMemberService.addGroupMember(groupMemberRequest);
    }

    @PostMapping("/bills/bill")
    public ResponseEntity<String> createBill(@RequestBody BillsRequestDTO billsRequestDTO){
        System.out.println(billsRequestDTO);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return billsService.saveBill(billsRequestDTO,username);
    }

    @GetMapping("/bills/{id}")
    public ResponseEntity<Bills> getBillById(@PathVariable UUID id){
        return billsService.getBillById(id);
    }
}
