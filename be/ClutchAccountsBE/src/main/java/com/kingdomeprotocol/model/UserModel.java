package com.kingdomeprotocol.model;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class UserModel {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@JsonProperty("id")
int id;
@JsonProperty("email")
@NotBlank(message = "Don't leave email is empty")
String email;
@JsonProperty("user_psw")
@NotBlank(message = "Please full filled the password")
String user_psw;
@JsonProperty("balance")
int balance = 0;
@JsonProperty("created_at")
LocalDateTime created_at;
@JsonProperty("role")
String role = "USER";

@OneToMany(mappedBy = "auditUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
List<AuditLogModel> auditList = new ArrayList<>();
@OneToMany(mappedBy = "transactionUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
List<TransactionsModel> transactionList = new ArrayList<>();
}
