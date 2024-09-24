package com.navdeep.billsplitter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillsDTO {

    private UUID billId;
    private String title;
    private String description;
    private int amount;
    private Date billDate;
    private String paidBy;
    private String addedBy;
    private List<BillSplitDTO> billMember;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
