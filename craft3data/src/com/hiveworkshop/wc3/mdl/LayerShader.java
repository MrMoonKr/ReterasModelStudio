package com.hiveworkshop.wc3.mdl;


/**
 * 레이어 타입. SD or HD
 */
public enum LayerShader {
	SD,
	HD;

	private static final LayerShader[] VALUES = values();

	public static LayerShader fromId(final int id) {
		if ((id < 0) || (id >= VALUES.length)) {
			throw new IllegalArgumentException("Unknown LayerShader ID: " + id);
		}
		return VALUES[id];
	}
}
