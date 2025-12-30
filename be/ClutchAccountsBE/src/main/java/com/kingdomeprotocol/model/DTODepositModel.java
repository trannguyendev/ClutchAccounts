package com.kingdomeprotocol.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DTODepositModel {
	@JsonProperty("email")
	@NotBlank(message = "Don't leave email is blank")
	String email;
	@JsonProperty("amount")
	@NotNull(message = "Don't leave amount is null")
	int amount;
	@JsonProperty("descrp")
	String descrp;
}
