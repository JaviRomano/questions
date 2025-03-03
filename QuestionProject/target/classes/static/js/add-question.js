import { loadHeader } from "/js/header.js";
import { createNavbar } from "/js/navbar.js";

loadHeader();

function upperFirstLetter(string) {
	return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}

document.addEventListener("DOMContentLoaded", async function() {
	const firstStageForm = document.getElementById("firstStageForm");
	const secondStageForm = document.getElementById("secondStageForm");
	const questionTypeSelect = document.getElementById("questionType");
	const categorySelect = document.getElementById("category");
	const newCategoryContainer = document.getElementById("newCategoryContainer");
	const newCategoryInput = document.getElementById("newCategory");
	const multipleChoiceOptions = document.getElementById("multipleChoiceOptions");
	const trueFalseOption = document.getElementById("trueFalseOption");
	const questionText = document.getElementById("questionText");
	const correctAnswers = document.getElementById("correctAnswers");
	const failAnswers = document.getElementById("failAnswers");
	const answerSelect = document.getElementById("answer");
	const messageDiv = document.getElementById("message");

	document.body.prepend(createNavbar());

	const backgroundContainer = document.createElement("div");
	backgroundContainer.classList.add("background-container");
	backgroundContainer.style.cssText = `
		        background-image: url('/img/riddler1.jpg');
		        background-size: cover;
		        background-position: center;
				width: 50%;
				filter: saturate(30%);
				height: 100vh;
		        position: fixed;
		        top: 0;
		        left: 25%;
		        z-index: -1;
		   	`;
	document.body.appendChild(backgroundContainer);

	try {
		const response = await fetch("/api/question/categories");
		if (!response.ok) throw new Error("Error al cargar categorías.");

		let categories = await response.json();

		categories = [...new Set(categories.map(upperFirstLetter))];

		categorySelect.innerHTML = `<option value="" selected disabled>Selecciona la categoría</option>`;
		categories.forEach(category => {
			const option = document.createElement("option");
			option.value = category;
			option.textContent = category;
			categorySelect.appendChild(option);
		});

		const otherOption = document.createElement("option");
		otherOption.value = "other";
		otherOption.textContent = "Otra (especificar)";
		categorySelect.appendChild(otherOption);
	} catch (error) {
		console.error("Error cargando categorías:", error);
	}

	categorySelect.addEventListener("change", function() {
		if (categorySelect.value === "other") {
			newCategoryContainer.classList.remove("d-none");
			newCategoryInput.setAttribute("required", "true");
		} else {
			newCategoryContainer.classList.add("d-none");
			newCategoryInput.removeAttribute("required");
		}
	});

	document.getElementById("nextStep").addEventListener("click", function() {
		if (!questionTypeSelect.value || !categorySelect.value) {
			messageDiv.innerHTML = `<div class="alert alert-warning">Selecciona el tipo de pregunta y la categoría.</div>`;
			return;
		}
		document.getElementById("finalQuestionType").value = questionTypeSelect.value;
		document.getElementById("finalCategory").value = categorySelect.value;

		firstStageForm.classList.add("d-none");
		secondStageForm.classList.remove("d-none");

		if (questionTypeSelect.value === "MULTIPLE_CHOICE_QUESTION") {
			multipleChoiceOptions.classList.remove("d-none");
			trueFalseOption.classList.add("d-none");
		} else {
			multipleChoiceOptions.classList.add("d-none");
			trueFalseOption.classList.remove("d-none");
		}
	});

	document.getElementById("backStep").addEventListener("click", function() {
		firstStageForm.classList.remove("d-none");
		secondStageForm.classList.add("d-none");
	});

	document.getElementById("saveQuestion").addEventListener("click", async function() {
		const type = document.getElementById("finalQuestionType").value;
		const text = questionText.value.trim();
		let category = document.getElementById("finalCategory").value.trim();
		

		if (!text) {
			messageDiv.innerHTML = `<div class="alert alert-warning">La pregunta no puede estar vacía.</div>`;
			return;
		}

		if (category === "other") {
			category = newCategoryInput.value.trim();
			if (!category) {
				messageDiv.innerHTML = `<div class="alert alert-warning">Debe especificar una categoría.</div>`;
				return;
			}
		}
		
		console.log("Valores antes de crear questionData:");
		    console.log("Tipo:", type);
		    console.log("Texto:", text);
		    console.log("Categoría:", category);

		let questionData = {
			typeOfQuestion: type, 
		    text: text,
		    category: category
		};

		if (type === "MULTIPLE_CHOICE_QUESTION") {
			questionData.correctAnswers = correctAnswers.value.split(",").map(a => a.trim());
			questionData.failAnswers = failAnswers.value.split(",").map(a => a.trim());
		} else if (type === "TRUE_FALSE_QUESTION") {
			questionData.answer = answerSelect.value === "true";
		}
		
		console.log("JSON a enviar:", JSON.stringify(questionData, null, 2));

		try {
			const response = await fetch("/api/question/add", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(questionData),
			});

			if (response.ok) {
				messageDiv.innerHTML = `<div class="alert alert-success">Pregunta creada con éxito.</div>`;
				setTimeout(() => window.location.href = "/question/all", 2000);
			} else {
				messageDiv.innerHTML = `<div class="alert alert-danger">Error al guardar la pregunta.</div>`;
			}
		} catch (error) {
			messageDiv.innerHTML = `<div class="alert alert-danger">Error de conexión con el servidor.</div>`;
		}
	});
});

