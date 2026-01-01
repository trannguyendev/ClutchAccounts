package com.kingdomeprotocol.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
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
@Table(name = "AccForSell")
public class AccForSellModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("acc_id")
	int account_id;
	@JsonProperty("email")
	String email;
	@JsonProperty("username")
	String username;
	@JsonProperty("password")
	String account_psw;
	@JsonProperty("price")
	int price;
	@JsonProperty("listed_at")
	LocalDateTime listed_at;
	@JsonProperty("isSold")
	boolean isSold;
	@JsonProperty("isLocked")
	boolean isLocked;
	@JsonProperty("lockedUntil")
	LocalDateTime lockedUntil;
	@JsonProperty("account_type")
	String account_type;
	
	@OneToOne(mappedBy = "accSold", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public BoughtLogModel boughtLog;
	
	public void setSoldBoughtLog(BoughtLogModel boughtLog) {
		this.boughtLog = boughtLog;
		this.isSold = true;
	}
}
