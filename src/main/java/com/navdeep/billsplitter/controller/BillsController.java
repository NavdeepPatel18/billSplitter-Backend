package com.navdeep.billsplitter.controller;

import com.navdeep.billsplitter.dto.BillUpdateRequestDTO;
import com.navdeep.billsplitter.dto.BillsDTO;
import com.navdeep.billsplitter.dto.BillsRequestDTO;
import com.navdeep.billsplitter.service.BillsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/groups/group/bills")
@RequiredArgsConstructor
public class BillsController {

    private final BillsService billsService;

    @GetMapping("/{groupId}")
    public ResponseEntity<List<BillsDTO>> getAllBills(@PathVariable UUID groupId) {

        List<BillsDTO> billsDTOList = billsService.getAllBillsByGroupId(groupId);

        if (billsDTOList.isEmpty()) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.status(200).body(billsDTOList);

    }

    @PostMapping("/bill/create")
    public ResponseEntity<BillsDTO> createBill(@RequestBody BillsRequestDTO billsRequestDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        BillsDTO createBill =  billsService.saveBill(billsRequestDTO,username);
        if(createBill != null){
            return ResponseEntity.status(200).body(createBill);
        }
        return ResponseEntity.status(400).body(null);
    }

    @GetMapping("/bill/{id}")
    public ResponseEntity<BillsDTO> getBillById(@PathVariable UUID id){
        BillsDTO billsDTO = billsService.getBillById(id);
        if(billsDTO == null) return ResponseEntity.status(400).body(null);

        return ResponseEntity.status(200).body(billsDTO);
    }

    @PutMapping("/bill")
    public ResponseEntity<BillsDTO> updateBillById(@RequestBody BillUpdateRequestDTO billUpdateRequestDTO){

        BillsDTO billsDTO = billsService.updateById(billUpdateRequestDTO);
        if(billsDTO == null) return ResponseEntity.status(400).body(null);
        return ResponseEntity.status(200).body(billsDTO);
    }

    @DeleteMapping("/bill/{id}")
    public ResponseEntity<String> deleteBillById(@PathVariable UUID id){

        if(billsService.deleteBillById(id)) return ResponseEntity.status(200).body("Bill Deleted Successfully");
        return ResponseEntity.status(400).body("Bill Deletion Failed");
    }
}
