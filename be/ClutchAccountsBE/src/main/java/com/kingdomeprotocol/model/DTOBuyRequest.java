package com.kingdomeprotocol.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DTOBuyRequest {
	@JsonProperty("email")
	String email;
	@JsonProperty("type")
	String type;
	@JsonProperty("accId")
	int accId;
}
