package com.navdeep.billsplitter.service;

import com.navdeep.billsplitter.dto.BillSplitRequestDTO;
import com.navdeep.billsplitter.dto.BillsRequestDTO;
import com.navdeep.billsplitter.entity.BillSplit;
import com.navdeep.billsplitter.entity.Bills;
import com.navdeep.billsplitter.entity.GroupDetail;
import com.navdeep.billsplitter.entity.Users;
import com.navdeep.billsplitter.repository.BillsRepository;
import com.navdeep.billsplitter.repository.GroupDetailRepository;
import com.navdeep.billsplitter.repository.UsersRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
@RequiredArgsConstructor
public class BillsService {


    private final BillsRepository billsRepository;
    private final UsersRepository usersRepository;
    private final GroupDetailRepository groupDetailRepository;
    private final BillSplitService billSplitService;

//    public List<Bills> getAllBills() {}

//    public Bills getBillById(Long id) {}

    public ResponseEntity<String> saveBill(@NonNull BillsRequestDTO requestDTO, @NonNull String username) {
        Users users = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        GroupDetail groupDetail = groupDetailRepository.findById(requestDTO.getGroupId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        Bills newbill = Bills.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .amount(requestDTO.getAmount())
                .billDate(requestDTO.getBillDate())
                .groupDetail(groupDetail)
                .addedBy(users)
                .build();

        Bills savedBill = billsRepository.save(newbill);
        if (savedBill.getId() != null) {
            List<BillSplit> members = new ArrayList<>();
            for(BillSplitRequestDTO member: requestDTO.getMembers()) {

                Users participate = usersRepository.findByUsername(member.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(member.getUsername()));

                members.add(BillSplit.builder()
                        .bill(savedBill)
                        .user(participate)
                        .amount(member.getAmount())
                        .build()
                );
            }

            if(billSplitService.add(members))
                return ResponseEntity.ok("Successfully bill user's spend added.");

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Bill user's spend is not added");

        }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Bill is not generated.");
    }

    public ResponseEntity<Bills> getBillById(@NonNull UUID id) {
        Optional<Bills> bill = billsRepository.findById(id);
        return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

}
