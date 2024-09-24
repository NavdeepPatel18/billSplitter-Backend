package com.navdeep.billsplitter.repository;

import com.navdeep.billsplitter.entity.BillSplit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillSplitRepository extends JpaRepository<BillSplit, UUID> {
    List<BillSplit> findAllByBillId(UUID billId);

    Boolean deleteAllByBillId(UUID billId);

    @Modifying
    @Transactional
    @Query("DELETE FROM BillSplit bs WHERE bs.bill.id = :billId")
    int deleteByBillId(UUID billId);
}
