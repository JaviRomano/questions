import { loadHeader } from "/js/header.js";
import { createNavbar } from "/js/navbar.js";

loadHeader();

document.addEventListener("DOMContentLoaded", async function() {
	const body = document.body;
	body.prepend(createNavbar());

	const urlParams = new URLSearchParams(window.location.search);
	const questionId = urlParams.get("id") || window.location.pathname.split("/").pop();

	if (!questionId) {
		document.getElementById("questionDetails").innerHTML = `<div class="alert alert-danger">Error: No se ha proporcionado un ID de pregunta.</div>`;
		return;
	}

	try {
		const response = await fetch(`/api/question/${questionId}`);
		if (!response.ok) throw new Error("No se encontró la pregunta.");

		const question = await response.json();
		console.log("JSON recibido:", question);

		document.getElementById("questionText").textContent = question.text;
		document.getElementById("questionCategory").textContent =
			question.category.charAt(0).toUpperCase() + question.category.slice(1).toLowerCase();

		let formattedType;
		if (question.typeOfQuestion === "MULTIPLE_ANSWER") {
			formattedType = "Respuesta Múltiple";
		} else if (question.typeOfQuestion === "TRUE_FALSE") {
			formattedType = "Verdadero o Falso";
		} else {
			formattedType = question.typeOfQuestion.replace(/_/g, " ");
		}
		document.getElementById("questionType").textContent = formattedType;

		const multipleChoiceContainer = document.getElementById("multipleChoiceAnswers");
		const trueFalseContainer = document.getElementById("trueFalseAnswer");

		const correctAnswersList = document.getElementById("correctAnswersList");
		const failAnswersList = document.getElementById("failAnswersList");
		const trueFalseValue = document.getElementById("trueFalseValue");

		if (!correctAnswersList || !failAnswersList || !trueFalseValue) {
			console.error("Error: Algunos elementos no existen en el DOM.");
			return;
		}
		
		multipleChoiceContainer.style.display = "none";
		trueFalseContainer.style.display = "none";

		correctAnswersList.innerHTML = "";
		failAnswersList.innerHTML = "";
		trueFalseValue.textContent = "";

		if (question.typeOfQuestion === "MULTIPLE_ANSWER") {
			multipleChoiceContainer.style.display = "block";
			
			console.log("Respuestas correctas:", question.correctAnswers);
			console.log("Respuestas incorrectas:", question.failAnswers);

			if (Array.isArray(question.correctAnswers) && question.correctAnswers.length > 0) {
				question.correctAnswers.forEach(answer => {
					let row = document.createElement("tr");
					row.innerHTML = `<td>${answer}</td>`;
					correctAnswersList.appendChild(row);
				});
			} else {
				console.warn("⚠️ No hay respuestas correctas registradas.");
				correctAnswersList.innerHTML = `<tr><td class="text-muted">No hay respuestas correctas registradas.</td></tr>`;
			}

			if (Array.isArray(question.failAnswers) && question.failAnswers.length > 0) {
				question.failAnswers.forEach(answer => {
					let row = document.createElement("tr");
					row.innerHTML = `<td>${answer}</td>`;
					failAnswersList.appendChild(row);
				});
			} else {
				console.warn("⚠️ No hay respuestas incorrectas registradas.");
				failAnswersList.innerHTML = `<tr><td class="text-muted">No hay respuestas incorrectas registradas.</td></tr>`;
			}

		} else if (question.typeOfQuestion === "TRUE_FALSE") {
			trueFalseContainer.style.display = "block";

			console.log("Respuesta (true/false):", question.answer);
			
			let answerValue = String(question.answer).trim().toLowerCase();
			if (answerValue === "true" || answerValue === "Verdadero" || answerValue === true || answerValue === "1") {
				trueFalseValue.textContent = "Verdadero";
			} else if (answerValue === false  || answerValue === "Falso" || answerValue === "false" || answerValue === "0") {			    
				trueFalseValue.textContent = "Falso";
			}else {
				trueFalseValue.textContent = "No disponible";
			}
		}
	} catch (error) {
		document.getElementById("questionDetails").innerHTML = `<div class="alert alert-danger">Error al cargar la pregunta: ${error.message}</div>`;
	}
});