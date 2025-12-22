package com.kingdomeprotocol.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsProc {
	@JsonProperty("email")
	public String email;
	@JsonProperty("password")
	public String psw;
}
