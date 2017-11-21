package com.example.spring.beans;


import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdnDataBean {
	@Getter	@NonNull
	private final String yesr;
	@Getter	@NonNull
	private final String dir;

}
