package com.navdeep.billsplitter.dto;

import com.navdeep.billsplitter.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupListDTO {

    private UUID groupId;
    private String groupName;
    private String groupDescription;
    private String groupStatus;
    private String groupType;
    private String createdBy;
    private List<String> members;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

}
