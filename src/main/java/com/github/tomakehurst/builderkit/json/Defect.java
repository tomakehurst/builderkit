package com.github.tomakehurst.builderkit.json;

public class Defect extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Defect() {
		super();
	}

	public Defect(String message, Throwable cause) {
		super(message, cause);
	}

	public Defect(String message) {
		super(message);
	}

	public Defect(Throwable cause) {
		super(cause);
	}
}
