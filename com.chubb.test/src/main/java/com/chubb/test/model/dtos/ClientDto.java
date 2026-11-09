package com.chubb.test.model.dtos;

import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Client information")
public class ClientDto {

	@Schema(
        description = "Client's identifier",
        example = "1"
    )
	private int id;
	
	@Schema(
        description = "Client first name",
        example = "John"
    )
	private String firstName;
	
	@Schema(
        description = "Client last name",
        example = "Smith"
    )
	private String lastName;
	
	@Schema(
        description = "Client surname",
        example = "Johnson"
    )
	private String surname;
	
	@Schema(
        description = "Client phone number",
        example = "3107896542"
    )
	private String phone;
	

	 public ClientDto(int id, String firstName, String lastName, String surname, String phone){
		super();
		this.id = id; 
		this.firstName = firstName; 
		this.lastName = lastName; 
		this.surname = surname; 
		this.phone = phone;
	 }
}
