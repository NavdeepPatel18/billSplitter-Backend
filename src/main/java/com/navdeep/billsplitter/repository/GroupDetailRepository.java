package com.navdeep.billsplitter.repository;

import com.navdeep.billsplitter.entity.GroupDetail;
import com.navdeep.billsplitter.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupDetailRepository extends JpaRepository<GroupDetail, UUID> {
    List<GroupDetail> findByCreatedBy(Users createdBy);
}
