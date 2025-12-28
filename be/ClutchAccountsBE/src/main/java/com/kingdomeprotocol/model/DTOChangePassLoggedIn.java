package com.kingdomeprotocol.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DTOChangePassLoggedIn {
	@JsonProperty("email")
	@NotBlank(message = "Don't leave the email blank")
	String email;
	@JsonProperty("oldPassword")
	@NotBlank(message = "Don't let your password is blank")
	String oldPass;
	@JsonProperty("newPassword")
	@NotBlank(message = "Don't let the new password blank")
	String newPass;
}
