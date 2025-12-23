package com.kingdomeprotocol.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DTOAuditLogRequest {
	@JsonProperty("logged_ip")
	public String logged_ip;
	@JsonProperty("logged_device")
	public String logged_device;
	@JsonProperty("logged_time")
	public LocalDateTime logged_time;
}
