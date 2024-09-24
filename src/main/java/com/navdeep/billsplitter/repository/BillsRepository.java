package com.navdeep.billsplitter.repository;

import com.navdeep.billsplitter.entity.Bills;
import com.navdeep.billsplitter.entity.GroupDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillsRepository extends JpaRepository<Bills, UUID> {

    public Optional<Bills> findById(UUID id);

    List<Bills> findAllByGroupDetail(GroupDetail groupDetail);
}
