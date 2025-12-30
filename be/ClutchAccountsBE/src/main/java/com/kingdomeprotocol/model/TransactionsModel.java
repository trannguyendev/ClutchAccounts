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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Transactions")
public class TransactionsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	int id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	UserModel transactionUser;
	@JsonProperty("amount")
	int amount;
	@JsonProperty("type")
	String type;
	@JsonProperty("descrp")
	String descrp;
	@JsonProperty("created_at")
	LocalDateTime created_at = LocalDateTime.now();
	@JsonProperty("status")
	String status;
}
