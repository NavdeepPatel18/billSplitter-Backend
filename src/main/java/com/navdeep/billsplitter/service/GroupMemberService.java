package com.navdeep.billsplitter.service;

import com.navdeep.billsplitter.dto.GroupMemberDTO;
import com.navdeep.billsplitter.dto.GroupMemberRequest;
import com.navdeep.billsplitter.entity.GroupDetail;
import com.navdeep.billsplitter.entity.GroupMember;
import com.navdeep.billsplitter.entity.GroupUserId;
import com.navdeep.billsplitter.entity.Users;
import com.navdeep.billsplitter.repository.GroupDetailRepository;
import com.navdeep.billsplitter.repository.GroupMemberRepository;
import com.navdeep.billsplitter.repository.UsersRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    private final UsersRepository usersRepository;
    private final GroupDetailRepository groupDetailRepository;

    public List<GroupMemberDTO> addGroupMember(GroupMemberRequest groupMemberRequest) {
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

        return getAllGroupMembers(groupDetail.getGroupId());
    }

    public List<GroupMemberDTO> getAllGroupMembers(@NonNull UUID groupId) {
        GroupDetail groupDetail = groupDetailRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<GroupMember> memberList = groupMemberRepository.findByIdGroupId(groupDetail);

        return memberList.stream()
                .map(
                        groupMember -> {
                            return GroupMemberDTO.builder()
                                    .groupId(groupMember.getId().getGroupId().getGroupId())
                                    .userId(groupMember.getId().getUserId().getId())
                                    .userName(groupMember.getId().getUserId().getUsername())
                                    .build();
                        }
                )
                .collect(Collectors.toList());
    }
}
