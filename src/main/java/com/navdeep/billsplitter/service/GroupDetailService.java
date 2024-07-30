package com.navdeep.billsplitter.service;

import com.navdeep.billsplitter.dto.GroupDetailRequest;
import com.navdeep.billsplitter.entity.*;
import com.navdeep.billsplitter.repository.GroupDetailRepository;
import com.navdeep.billsplitter.repository.GroupMemberRepository;
import com.navdeep.billsplitter.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupDetailService {

    private final GroupDetailRepository groupDetailRepository;
    private final UsersRepository usersRepository;
    private final GroupMemberRepository groupMemberRepository;


    public ResponseEntity<String> createGroup(@NotNull GroupDetailRequest groupDetailRequest, String username) {
        Users users = usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        GroupDetail newGroupDetail = GroupDetail.builder()
                .groupName(groupDetailRequest.getGroupName())
                .groupDescription(groupDetailRequest.getGroupDescription())
                .groupType(GroupType.valueOf(groupDetailRequest.getGroupType()))
                .createdBy(users)
                .build();


        GroupDetail groupDetail = groupDetailRepository.save(newGroupDetail);

        if(groupDetail.getGroupId() != null){
            GroupUserId groupUserId = GroupUserId.builder()
                    .groupId(groupDetail)
                    .userId(users)
                    .build();
            GroupMember groupMember = GroupMember.builder()
                    .id(groupUserId)
                    .build();
            groupMemberRepository.save(groupMember);

            return ResponseEntity.ok("Successfully created group.");

        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Group is not created");
    }

    public List<GroupDetail> groupList(String username){
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return groupMemberRepository.findByIdUserId(user)
                .stream()
                .map(group -> group.getId().getGroupId())
                .collect(Collectors.toList());
    }

    public ResponseEntity<List<String>> groupMemberList(UUID groupId) {
        GroupDetail groupDetail = groupDetailRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        List<GroupMember> groupMember = groupMemberRepository.findByIdGroupId(groupDetail);
        List<String> groupMemberList = new ArrayList<>();

        for(GroupMember member: groupMember){
            groupMemberList.add(member.getId().getUserId().getUsername());
        }

        return ResponseEntity.ok(groupMemberList);

    }
}
