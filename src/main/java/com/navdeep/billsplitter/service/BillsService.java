package com.navdeep.billsplitter.service;

import com.navdeep.billsplitter.dto.BillUpdateRequestDTO;
import com.navdeep.billsplitter.dto.BillsDTO;
import com.navdeep.billsplitter.dto.BillsRequestDTO;
import com.navdeep.billsplitter.entity.Bills;
import com.navdeep.billsplitter.entity.GroupDetail;
import com.navdeep.billsplitter.entity.Users;
import com.navdeep.billsplitter.repository.BillsRepository;
import com.navdeep.billsplitter.repository.GroupDetailRepository;
import com.navdeep.billsplitter.repository.UsersRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BillsService {


    private final BillsRepository billsRepository;
    private final UsersRepository usersRepository;
    private final GroupDetailRepository groupDetailRepository;
    private final BillSplitService billSplitService;


    public BillsDTO saveBill(@NonNull BillsRequestDTO requestDTO, @NonNull String username) {
        Users createdBy = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Users paidBy = usersRepository.findByUsername(requestDTO.getPaidBy())
                .orElseThrow(() -> new UsernameNotFoundException(requestDTO.getPaidBy()));

        GroupDetail groupDetail = groupDetailRepository.findById(requestDTO.getGroupId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        Bills newBill = Bills.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .amount(requestDTO.getAmount())
                .billDate(requestDTO.getBillDate())
                .groupDetail(groupDetail)
                .addedBy(createdBy)
                .paidBy(paidBy)
                .build();

        Bills savedBill = billsRepository.save(newBill);
        if (savedBill.getId() != null) {

            if(billSplitService.add(savedBill,requestDTO.getMembers()))
                return BillsDTO.builder()
                        .billId(savedBill.getId())
                        .title(savedBill.getTitle())
                        .description(savedBill.getDescription())
                        .amount(savedBill.getAmount())
                        .billDate(savedBill.getBillDate())
                        .paidBy(savedBill.getPaidBy().getUsername())
                        .addedBy(savedBill.getAddedBy().getUsername())
                        .billMember(billSplitService.getBillSplits(savedBill.getId()))
                        .createdAt(savedBill.getCreatedAt())
                        .updatedAt(savedBill.getUpdatedAt())
                        .build();

            return null;

        }

        return null;
    }

    public List<BillsDTO> getAllBillsByGroupId(@NonNull UUID groupId) {

        GroupDetail groupDetail = groupDetailRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        List<Bills> bills = billsRepository.findAllByGroupDetail(groupDetail);

        return bills.stream()
                .map(
                        bill -> BillsDTO.builder()
                                .billId(bill.getId())
                                .title(bill.getTitle())
                                .description(bill.getDescription())
                                .amount(bill.getAmount())
                                .billDate(bill.getBillDate())
                                .paidBy(bill.getPaidBy().getUsername())
                                .addedBy(bill.getAddedBy().getUsername())
                                .billMember(billSplitService.getBillSplits(bill.getId()))
                                .createdAt(bill.getCreatedAt())
                                .updatedAt(bill.getUpdatedAt())
                                .build()
                ).toList();
    }

    public BillsDTO getBillById(@NonNull UUID id) {

        Bills bill = billsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill not found"));

        return BillsDTO.builder()
                .billId(bill.getId())
                .title(bill.getTitle())
                .description(bill.getDescription())
                .amount(bill.getAmount())
                .billDate(bill.getBillDate())
                .paidBy(bill.getPaidBy().getUsername())
                .addedBy(bill.getAddedBy().getUsername())
                .billMember(billSplitService.getBillSplits(bill.getId()))
                .createdAt(bill.getCreatedAt())
                .updatedAt(bill.getUpdatedAt())
                .build();
    }


    public BillsDTO updateById(@NotNull BillUpdateRequestDTO requestDTO) {

        Bills bill = billsRepository.findById(requestDTO.getBillId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill not found"));

        Users paidBy = usersRepository.findByUsername(requestDTO.getPaidBy())
                .orElseThrow(() -> new UsernameNotFoundException(requestDTO.getPaidBy()));

        if(!billSplitService.delete(bill.getId()))  return null;

        System.out.println(requestDTO);

        if(!billSplitService.add(bill,requestDTO.getSplitMembers())) return null;


        bill.setTitle(requestDTO.getTitle());
        bill.setDescription(requestDTO.getDescription());
        bill.setAmount(requestDTO.getAmount());
        bill.setBillDate(requestDTO.getBillDate());
        bill.setPaidBy(paidBy);
        Bills updatedBill = billsRepository.save(bill);



        return BillsDTO.builder()
                .billId(updatedBill.getId())
                .title(updatedBill.getTitle())
                .description(updatedBill.getDescription())
                .amount(updatedBill.getAmount())
                .billDate(updatedBill.getBillDate())
                .addedBy(updatedBill.getAddedBy().getUsername())
                .paidBy(updatedBill.getPaidBy().getUsername())
                .billMember(billSplitService.getBillSplits(updatedBill.getId()))
                .createdAt(updatedBill.getCreatedAt())
                .updatedAt(updatedBill.getUpdatedAt())
                .build();

    }


    public Boolean deleteBillById(@NonNull UUID id) {

        Bills bill = billsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill not found"));

        billsRepository.deleteById(bill.getId());

        if(billsRepository.existsById(bill.getId())) return Boolean.FALSE;

        return Boolean.TRUE;
    }


}
