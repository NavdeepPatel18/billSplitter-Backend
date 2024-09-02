package com.navdeep.billsplitter.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GroupMember {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @UuidGenerator(style = UuidGenerator.Style.TIME)
//    private UUID id;

    @EmbeddedId
    private GroupUserId id;

//    @ManyToOne
//    @MapsId("userId")  // Maps the userId attribute of GroupUserId to Users entity
//    @JoinColumn(name = "user_id")
//    private Users user;
//
//    @ManyToOne
//    @MapsId("groupId")  // Maps the groupId attribute of GroupUserId to GroupDetail entity
//    @JoinColumn(name = "group_id")
////    @JsonBackReference
//    private GroupDetail groupDetail;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
