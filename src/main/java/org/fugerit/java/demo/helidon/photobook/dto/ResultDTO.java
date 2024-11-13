package org.fugerit.java.demo.helidon.photobook.dto;

import io.helidon.common.Reflected;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Reflected
@RequiredArgsConstructor
public class ResultDTO<T> {

	@NonNull @Getter @Setter
	T content;
	
}
