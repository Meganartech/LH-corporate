package com.knowledgeVista.Payments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Paypalsettings {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String paypal_client_id;
	private String paypal_secret_key;
	private String institutionName;
	
	
}
