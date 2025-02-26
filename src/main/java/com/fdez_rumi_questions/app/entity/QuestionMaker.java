package com.fdez_rumi_questions.app.entity;

import java.util.List;

public class QuestionMaker {
	
	@SuppressWarnings("unchecked")
	public static Question makeQuestion(TypeOfQuestion typeOfQuestion, String text, String category, Object... extraParams) {
		switch (typeOfQuestion) {
		case TRUE_FALSE:
			if (extraParams.length != 1 || !(extraParams[0] instanceof Boolean)) {
				throw new IllegalArgumentException("Valor no soportado para pregunta tipo verdadero/falso.");
			}
			return new TrueFalseQuestion(text, category, (Boolean) extraParams[0]);
					
		case MULTIPLE_ANSWER:
			if (extraParams.length != 2 || !(extraParams[0] instanceof List<?>) || !(extraParams[1] instanceof List<?>)) {
				throw new IllegalArgumentException("Se necesita lista de respuestas correctas y lista de respuestas incorrectas.");
			}
			return new MultipleChoiceQuestion(text, category, (List<String>) extraParams[0], (List<String>) extraParams[1]);		
		default:
			throw new IllegalArgumentException("Valor no soportado: " + typeOfQuestion);
		}
	}
}