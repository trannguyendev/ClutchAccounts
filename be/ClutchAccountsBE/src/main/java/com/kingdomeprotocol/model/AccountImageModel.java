package com.kingdomeprotocol.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "AccountImage")
@NoArgsConstructor
@AllArgsConstructor
public class AccountImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int image_id;
    String image_url;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", unique = true, nullable = false)
    public AccForSellModel accId;
}
