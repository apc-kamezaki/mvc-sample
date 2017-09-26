package com.example.spring.beans;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Component
public class ExternalFolderProperty {
	
	@Getter @Setter
	@NonNull
	private String externalFolder;
}
