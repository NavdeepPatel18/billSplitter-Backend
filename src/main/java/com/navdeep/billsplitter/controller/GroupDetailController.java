package com.navdeep.billsplitter.controller;

import com.navdeep.billsplitter.dto.*;
import com.navdeep.billsplitter.service.GroupDetailService;
import com.navdeep.billsplitter.service.GroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/groups")
@RequiredArgsConstructor
public class GroupDetailController {

    private final GroupDetailService groupDetailService;
    private final GroupMemberService groupMemberService;


    @GetMapping("/")
    public ResponseEntity<List<GroupListDTO>> groupList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<GroupListDTO> groupListDTO = groupDetailService.getGroups(username);

        if(groupListDTO.isEmpty()){
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(200).body(groupListDTO);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDetailDTO> getGroupDetail(@PathVariable UUID groupId) {

        GroupDetailDTO groupDetailDTO = groupDetailService.getGroupDetail(groupId);

        if(groupDetailDTO == null){
            return ResponseEntity.status(400).body(null);
        }

        return ResponseEntity.status(200).body(groupDetailDTO);
    }

    @PutMapping("/group")
    public ResponseEntity<GroupDetailDTO> updateGroupDetail(@RequestBody  GroupDetailDTO groupDetailDTO) {

        GroupDetailDTO updatedGroupDetail = groupDetailService.updateGroupDetail(groupDetailDTO);

        if(updatedGroupDetail == null){
            return ResponseEntity.status(400).body(null);
        }

        return ResponseEntity.status(200).body(updatedGroupDetail);
    }


    @PostMapping("/group/create")
    public ResponseEntity<GroupDetailDTO> createGroup(@RequestBody GroupDetailRequest groupDetailRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        GroupDetailDTO newGroupDetail = groupDetailService.createGroup(groupDetailRequest,username);

        if(newGroupDetail == null){
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(200).body(newGroupDetail);
    }

    @GetMapping("/{group}/members")
    public ResponseEntity<List<GroupMemberDTO>> groupMemberList(@PathVariable UUID group){

        List<GroupMemberDTO> memberList = groupDetailService.groupMemberList(group);

        if(memberList.isEmpty()){
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(200).body(memberList);
    }

    @PostMapping("/group/member/add")
    public ResponseEntity<List<GroupMemberDTO>> groupMemberAdd(@RequestBody GroupMemberRequest groupMemberRequest){
        List<GroupMemberDTO> addedMember = groupMemberService.addGroupMember(groupMemberRequest);

        if(addedMember.isEmpty()){
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(200).body(addedMember);
    }
}
