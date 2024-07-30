package com.navdeep.billsplitter.repository;

import com.navdeep.billsplitter.entity.GroupDetail;
import com.navdeep.billsplitter.entity.GroupMember;
import com.navdeep.billsplitter.entity.GroupUserId;
import com.navdeep.billsplitter.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupUserId> {
    List<GroupMember> findByIdGroupId(GroupDetail id_groupId);

    List<GroupMember> findByIdUserId(Users user);

}
