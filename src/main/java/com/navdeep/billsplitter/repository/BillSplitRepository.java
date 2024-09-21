package com.navdeep.billsplitter.repository;

import com.navdeep.billsplitter.entity.BillSplit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillSplitRepository extends JpaRepository<BillSplit, UUID> {
}
