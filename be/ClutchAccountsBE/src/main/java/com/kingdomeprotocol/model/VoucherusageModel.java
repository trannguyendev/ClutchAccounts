package com.kingdomeprotocol.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VoucherUsage", uniqueConstraints = @UniqueConstraint(name = "unique_user_voucher", columnNames = {"user_id", "voucher_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherusageModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public UserModel user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voucher_id")
	public Vouchers voucher;
	
	@JsonProperty("used_at")
	LocalDateTime used_at = LocalDateTime.now();
}
