package com.navdeep.billsplitter.service;

import com.navdeep.billsplitter.entity.BillSplit;
import com.navdeep.billsplitter.repository.BillSplitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillSplitService {
    private final BillSplitRepository billSplitRepository;

    public Boolean add(List<BillSplit> members) {

        List<BillSplit> addedmembers = billSplitRepository.saveAll(members);

        return addedmembers.size() == members.size();
    }
}
