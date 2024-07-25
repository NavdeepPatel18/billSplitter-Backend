package com.navdeep.billsplitter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDetailRequest {
    private String groupName;
    private String groupDescription;
    private String groupType;
}
