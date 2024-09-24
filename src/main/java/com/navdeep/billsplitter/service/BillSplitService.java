package com.navdeep.billsplitter.service;

import com.navdeep.billsplitter.dto.BillSplitDTO;
import com.navdeep.billsplitter.dto.BillSplitRequestDTO;
import com.navdeep.billsplitter.entity.BillSplit;
import com.navdeep.billsplitter.entity.Bills;
import com.navdeep.billsplitter.entity.Users;
import com.navdeep.billsplitter.repository.BillSplitRepository;
import com.navdeep.billsplitter.repository.UsersRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillSplitService {

    private final UsersRepository usersRepository;
    private final BillSplitRepository billSplitRepository;

    public Boolean add(@NonNull Bills billId, List<BillSplitRequestDTO> requestDTO) {

        List<BillSplit> members = new ArrayList<>();
        for(BillSplitRequestDTO member: requestDTO) {

            Users participate = usersRepository.findByUsername(member.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(member.getUsername()));

            members.add(BillSplit.builder()
                    .bill(billId)
                    .user(participate)
                    .amount(member.getAmount())
                    .build()
            );
        }

        List<BillSplit> addedMembers = billSplitRepository.saveAll(members);

        return addedMembers.size() == members.size();
    }


    public List<BillSplitDTO> getBillSplits(@NotNull UUID billId){


        return billSplitRepository.findAllByBillId(billId).stream()
                .map(
                        billSplit -> {
                            new BillSplitDTO();
                            return BillSplitDTO.builder()
                                    .billSplitId(billSplit.getId())
                                    .userName(billSplit.getUser().getUsername())
                                    .amount(billSplit.getAmount())
                                    .percentage(billSplit.getSharePercentage())
                                    .build();
                        }
                ).toList();
    }


    public boolean delete(@NotNull UUID billId){
        return (billSplitRepository.deleteByBillId(billId) > 0);
    }
}
