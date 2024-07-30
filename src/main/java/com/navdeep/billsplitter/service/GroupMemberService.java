package com.navdeep.billsplitter.service;

import com.navdeep.billsplitter.dto.GroupMemberRequest;
import com.navdeep.billsplitter.entity.GroupDetail;
import com.navdeep.billsplitter.entity.GroupMember;
import com.navdeep.billsplitter.entity.GroupUserId;
import com.navdeep.billsplitter.entity.Users;
import com.navdeep.billsplitter.repository.GroupDetailRepository;
import com.navdeep.billsplitter.repository.GroupMemberRepository;
import com.navdeep.billsplitter.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    private final UsersRepository usersRepository;
    private final GroupDetailRepository groupDetailRepository;

    public ResponseEntity<String> addGroupMember(GroupMemberRequest groupMemberRequest) {
        GroupDetail groupDetail = groupDetailRepository.findById(groupMemberRequest.getGroupDetail())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        for(String user : groupMemberRequest.getUsername()) {
            Users getUser = usersRepository.findByUsername(user).orElseThrow(() -> new RuntimeException("User not found"));

            GroupUserId groupUserId = GroupUserId.builder()
                    .groupId(groupDetail)
                    .userId(getUser)
                    .build();

            GroupMember groupMember = GroupMember.builder()
                            .id(groupUserId)
                    .build();

            groupMemberRepository.save(groupMember);
        }

        return ResponseEntity.ok("Successfully added group member");
    }
}
