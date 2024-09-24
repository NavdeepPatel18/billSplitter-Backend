package com.navdeep.billsplitter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDetailDTO {

    private UUID groupId;
    private String groupName;
    private String groupDescription;
    private String groupType;
    private String groupStatus;
    private String createdBy;
    private List<GroupMemberDTO> members;


}
