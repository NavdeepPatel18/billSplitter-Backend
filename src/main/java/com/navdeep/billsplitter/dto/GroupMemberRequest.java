package com.navdeep.billsplitter.dto;

import com.navdeep.billsplitter.entity.GroupDetail;
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
public class GroupMemberRequest {
    private UUID groupDetail;
    private List<String> username;
}
