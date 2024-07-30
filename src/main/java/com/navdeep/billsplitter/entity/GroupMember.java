package com.navdeep.billsplitter.entity;

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

//    @JoinColumn(name = "user_id")
//    @ManyToOne
//    private Users user_id;
//
//    @JoinColumn(name = "group_id")
//    @ManyToOne
//    private GroupDetail group_id;

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
