package com.navdeep.billsplitter.service;

import com.navdeep.billsplitter.dto.GroupDetailRequest;
import com.navdeep.billsplitter.entity.GroupDetail;
import com.navdeep.billsplitter.entity.GroupType;
import com.navdeep.billsplitter.entity.Users;
import com.navdeep.billsplitter.repository.GroupDetailRepository;
import com.navdeep.billsplitter.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GroupDetailService {

    private final GroupDetailRepository groupDetailRepository;
    private final UsersRepository usersRepository;


//    public List<Group> getAllGroups(UUID userId) {
//        if (userId == null) {
//            return new ArrayList<>();
//        }
//
//        var user = usersRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("User not exists"));
//
//
//        return List.of();
//    }

    public GroupDetail createGroup(@NotNull GroupDetailRequest groupDetailRequest, String username) {
        Users users = usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        GroupDetail newGroupDetail = GroupDetail.builder()
                .groupName(groupDetailRequest.getGroupName())
                .groupDescription(groupDetailRequest.getGroupDescription())
                .groupType(GroupType.valueOf(groupDetailRequest.getGroupType()))
                .createdBy(users)
                .build();

        return groupDetailRepository.save(newGroupDetail);
    }

    public List<String> groupList(String username){
        Users user = usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        List<String> groupList = new ArrayList<>();
        for (GroupDetail groupDetail : groupDetailRepository.findByCreatedBy(user)) {
            groupList.add(groupDetail.getGroupName());
        }

        return groupList;
    }

}
