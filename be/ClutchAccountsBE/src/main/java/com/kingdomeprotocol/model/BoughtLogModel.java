package com.kingdomeprotocol.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BoughtLog")
public class BoughtLogModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int bought_id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public UserModel user;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", unique = true, nullable = false)
	public AccForSellModel accSold;
	public LocalDateTime bought_time = LocalDateTime.now();
}
