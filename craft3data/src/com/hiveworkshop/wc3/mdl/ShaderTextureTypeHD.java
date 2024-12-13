package com.hiveworkshop.wc3.mdl;

/**
 * 텍스쳐의 타입. 대부분 HD 모델에 사용된다.
 */
public enum ShaderTextureTypeHD {
	
	Diffuse,
	Normal,
	ORM,
	Emissive,
	TeamColor,
	Reflections;

	public static final ShaderTextureTypeHD[] VALUES = values();
}
