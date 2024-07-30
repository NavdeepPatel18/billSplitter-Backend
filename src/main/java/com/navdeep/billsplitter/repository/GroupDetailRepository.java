package com.navdeep.billsplitter.repository;

import com.navdeep.billsplitter.entity.GroupDetail;
import com.navdeep.billsplitter.entity.Users;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupDetailRepository extends JpaRepository<GroupDetail, UUID> {
//    List<GroupDetail> findByCreatedBy(Users createdBy);

    @NotNull
    Optional<GroupDetail> findById(@NotNull UUID uuid);

//    GroupDetail findById(@NotNull UUID uuid);
}
