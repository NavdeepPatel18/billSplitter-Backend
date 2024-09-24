package com.navdeep.billsplitter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillSplitDTO {

    private UUID billSplitId;
    private String userName;
    private int amount;
    private int percentage;
}
