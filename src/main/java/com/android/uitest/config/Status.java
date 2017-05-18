package com.android.uitest.config;

public enum Status {

	/**
	 * test method status
	 */
	PASS("pass"), FAIL("fail"), SKIP("skip"), DONE("done"),

	/**
	 * TEST SUITE status
	 */
	FINISH("finish");

	private final String args;

	Status(String args) {
		this.args = args;
	}

	public String getArgument() {
		return this.args;
	}
}