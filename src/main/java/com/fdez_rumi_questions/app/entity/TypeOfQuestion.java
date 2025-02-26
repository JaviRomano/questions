package com.fdez_rumi_questions.app.entity;

public enum TypeOfQuestion {
	TRUE_FALSE("Verdadero o falso"),
	MULTIPLE_ANSWER("Varias respuestas");
	
	private String translate;
	
	private TypeOfQuestion(String translate) {
		this.translate = translate;
	}

	public String getTranslate() {
		return translate;
	}
}