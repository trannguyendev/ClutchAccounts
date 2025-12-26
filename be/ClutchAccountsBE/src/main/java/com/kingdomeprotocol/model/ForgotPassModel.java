package com.kingdomeprotocol.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPassModel {
	String email;
	String otp;
	String newPass;
}
