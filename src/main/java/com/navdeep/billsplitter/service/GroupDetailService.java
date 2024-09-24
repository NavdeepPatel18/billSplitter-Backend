package com.navdeep.billsplitter.service;

import com.navdeep.billsplitter.dto.GroupDetailDTO;
import com.navdeep.billsplitter.dto.GroupDetailRequest;
import com.navdeep.billsplitter.dto.GroupListDTO;
import com.navdeep.billsplitter.dto.GroupMemberDTO;
import com.navdeep.billsplitter.entity.*;
import com.navdeep.billsplitter.repository.GroupDetailRepository;
import com.navdeep.billsplitter.repository.GroupMemberRepository;
import com.navdeep.billsplitter.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupDetailService {
    private final GroupDetailRepository groupDetailRepository;
    private final UsersRepository usersRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupMemberService groupMemberService;


    public GroupDetailDTO createGroup(@NotNull GroupDetailRequest groupDetailRequest, String username) {
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

            return GroupDetailDTO.builder()
                    .groupId(groupDetail.getGroupId())
                    .groupName(groupDetail.getGroupName())
                    .groupDescription(groupDetail.getGroupDescription())
                    .groupType(groupDetail.getGroupType().name())
                    .groupStatus(groupDetail.getGroupStatus())
                    .createdBy(groupDetail.getCreatedBy().getUsername())
                    .members(groupMemberService.getAllGroupMembers(groupDetail.getGroupId()))
                    .build();

        }

        return null;
    }

    public GroupDetailDTO getGroupDetail(@NotNull UUID groupId) {
        GroupDetail groupDetail = groupDetailRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new GroupDetailDTO(
                groupDetail.getGroupId(),
                groupDetail.getGroupName(),
                groupDetail.getGroupDescription(),
                groupDetail.getGroupType().name(),
                groupDetail.getGroupStatus(),
                groupDetail.getCreatedBy().getUsername(),
                groupMemberService.getAllGroupMembers(groupDetail.getGroupId())
        );
    }


    public GroupDetailDTO updateGroupDetail(
            @NotNull GroupDetailDTO groupDetailDTO
    ) {

        GroupDetail groupDetail = groupDetailRepository.findById(groupDetailDTO.getGroupId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        groupDetail.setGroupName(groupDetailDTO.getGroupName());
        groupDetail.setGroupDescription(groupDetailDTO.getGroupDescription());
        groupDetail.setGroupType(GroupType.valueOf(groupDetailDTO.getGroupType()));

        GroupDetail updatedGroupDetail = groupDetailRepository.save(groupDetail);

        return GroupDetailDTO.builder()
                .groupId(updatedGroupDetail.getGroupId())
                .groupName(updatedGroupDetail.getGroupName())
                .groupDescription(updatedGroupDetail.getGroupDescription())
                .groupType(updatedGroupDetail.getGroupType().name())
                .groupStatus(updatedGroupDetail.getGroupStatus())
                .createdBy(updatedGroupDetail.getCreatedBy().getUsername())
                .members(groupMemberService.getAllGroupMembers(updatedGroupDetail.getGroupId()))
                .build();

    }


    public List<GroupListDTO> getGroups(String username) {
        // Find user by username
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<GroupMember> groupMembers = groupMemberRepository.findByIdUserId(user);

        return groupMembers.stream()
                .map(member -> {
                    GroupDetail groupDetail = member.getId().getGroupId();
                    List<String> memberNames = groupDetail.getGroupMembers().stream()
                            .map(m -> m.getId().getUserId().getUsername())
                            .collect(Collectors.toList());

                    return new GroupListDTO(
                            groupDetail.getGroupId(),
                            groupDetail.getGroupName(),
                            groupDetail.getGroupDescription(),
                            groupDetail.getGroupStatus(),
                            groupDetail.getGroupType().name(),
                            groupDetail.getCreatedBy().getUsername(),
                            memberNames,
                            groupDetail.getCreatedAt(),
                            groupDetail.getUpdatedAt()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<GroupMemberDTO> groupMemberList(UUID groupId) {
        GroupDetail groupDetail = groupDetailRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        return groupMemberService.getAllGroupMembers(groupDetail.getGroupId());
    }

}
