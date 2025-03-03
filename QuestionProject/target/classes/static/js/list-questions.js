import { loadHeader } from "/js/header.js";
import { createNavbar } from "/js/navbar.js";

loadHeader();

function capitalizeFirstLetter(string) {
	return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}

document.addEventListener("DOMContentLoaded", async function() {
	const body = document.body;

	const questionsTableBody = document.querySelector("#questionsTableBody");

	const questionTypeMapping = {
		"MULTIPLE_ANSWER": "Respuesta múltiple",
		"TRUE_FALSE": "Verdadero o falso"
	};

	if (!questionsTableBody) {
		console.error("Error: No se encontró el tbody en el DOM");
		return;
	}

	const container = document.querySelector(".container");
	if (!container) {
		console.error("Error: no se encontró el contenedor principal.");
		return;
	}

	const messageContainer = document.createElement("div");
	messageContainer.classList.add("mt-3", "text-center");
	container.prepend(messageContainer);

	body.prepend(createNavbar());



	try {
		const response = await fetch("/api/question/all");
		if (!response.ok) throw new Error("Error al cargar las preguntas.");

		const questions = await response.json();
		if (questions.length === 0) {
			messageContainer.innerHTML = `<div class="alert alert-warning">No hay preguntas disponibles.</div>`;
			return;
		}

		questionsTableBody.innerHTML = questions.map(question => `
            <tr>
                <td><a href="/question/${question.id}" class="text-reset">${question.id}</a></td>
                <td>${question.text}</td>
                <td>${capitalizeFirstLetter(question.category)}</td>
                <td>${questionTypeMapping[question.typeOfQuestion] || capitalizeFirstLetter(question)}</td>
                <td>
                    <a href="/question/${question.id}" class="btn btn-success btn-sm">Ver</a>
                    <a href="/question/edit/${question.id}" class="btn btn-success btn-sm">Editar</a>
                    <button class="btn btn-danger btn-sm delete-btn" data-id="${question.id}">Eliminar</button>
                </td>
            </tr>
        `).join("");

		document.querySelectorAll(".delete-btn").forEach(button => {
			button.addEventListener("click", async function() {
				const questionId = this.getAttribute("data-id");
				if (!confirm("¿Seguro que quieres eliminar esta pregunta?")) return;

				try {
					const deleteResponse = await fetch(`/api/question/delete/${questionId}`, { method: "DELETE" });
					if (!deleteResponse.ok) throw new Error("Error al eliminar la pregunta.");

					this.closest("tr").remove();
					messageContainer.innerHTML = `<div class="alert alert-success">Pregunta eliminada correctamente.</div>`;
				} catch (error) {
					messageContainer.innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
				}
			});
		});
	} catch (error) {
		messageContainer.innerHTML = `<div class="alert alert-danger">${error.message}</div>`;
	}
});