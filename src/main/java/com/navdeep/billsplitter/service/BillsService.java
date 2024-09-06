package com.navdeep.billsplitter.service;

import com.navdeep.billsplitter.dto.BillsRequestDTO;
import com.navdeep.billsplitter.entity.Bills;
import com.navdeep.billsplitter.entity.GroupDetail;
import com.navdeep.billsplitter.entity.Users;
import com.navdeep.billsplitter.repository.BillsRepository;
import com.navdeep.billsplitter.repository.GroupDetailRepository;
import com.navdeep.billsplitter.repository.UsersRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillsService {

    private final BillsRepository billsRepository;
    private final UsersRepository usersRepository;
    private final GroupDetailRepository groupDetailRepository;

//    public List<Bills> getAllBills() {}

//    public Bills getBillById(Long id) {}

    public ResponseEntity<String> saveBill(@NonNull BillsRequestDTO requestDTO, @NonNull String username) {
        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        GroupDetail groupDetail = groupDetailRepository.findById(requestDTO.getGroupId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        List<Users> members = new ArrayList<>();
        for(String member: requestDTO.getMembers()) {

            Users participate = usersRepository.findByUsername(member)
                    .orElseThrow(() -> new UsernameNotFoundException(member));

            members.add(participate);
        }

        Bills newbill = Bills.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .amount(requestDTO.getAmount())
                .billDate(requestDTO.getBillDate())
                .groupDetail(groupDetail)
                .addedBy(users)
                .users(members)
                .build();

        Bills savedBill = billsRepository.save(newbill);
        if (savedBill.getId() != null) return ResponseEntity.ok("Successfully bill generate.");

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Bill is not generated.");
    }

//    public Bills updateBill(Bills bills) {}

//    public void deleteBill(Long id) {}



}
