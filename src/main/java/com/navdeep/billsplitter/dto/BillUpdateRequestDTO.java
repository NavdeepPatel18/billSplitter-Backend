package com.navdeep.billsplitter.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillUpdateRequestDTO {

    private UUID billId;
    private String title;
    private String description;
    private int amount;
    private String paidBy;

    @JsonbDateFormat("yyyy-MM-dd")
    private Date billDate;
    private List<BillSplitRequestDTO> splitMembers;

}
