package com.navdeep.billsplitter.repository;

import com.navdeep.billsplitter.entity.Bills;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillsRepository extends JpaRepository<Bills, UUID> {

    public Optional<Bills> findById(UUID id);

}
