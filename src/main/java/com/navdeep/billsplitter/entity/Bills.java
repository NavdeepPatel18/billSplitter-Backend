package com.navdeep.billsplitter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bills {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;
    private String title;
    private String description;
    @Column(nullable = false)
    private int amount;
    private Date billDate;

    @OneToOne
    private Users addedBy;

    @OneToOne
    private Users paidBy;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupDetail groupDetail;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BillSplit> billSplits; // Updated to use BillSplit entity


    @Column(nullable = false, updatable = false)
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
