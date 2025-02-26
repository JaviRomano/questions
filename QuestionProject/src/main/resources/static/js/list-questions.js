import { loadHeader } from "/js/header.js";
import { createNavbar } from "/js/navbar.js";

loadHeader();

document.addEventListener("DOMContentLoaded", async function () {
	const body = document.body;
	
	
    const questionsTableBody = document.querySelector("#questionsTableBody");
	
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
	
	const backgroundContainer = document.createElement("div");
		backgroundContainer.classList.add("background-container");
		backgroundContainer.innerHTML = `
		<img src="/img/riddler2.jpg" alt="Fondo">
		<h1 class="title-overlay">Bienvenido a la Aplicación de Preguntas</h1>
		`;
		body.appendChild(backgroundContainer);

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
                <td>${question.category}</td>
                <td>${question.typeOfQuestion.replace(/_/g, " ")}</td>
                <td>
                    <a href="/question/${question.id}" class="btn btn-info btn-sm">Ver</a>
                    <a href="/question/edit/${question.id}" class="btn btn-warning btn-sm">Editar</a>
                    <button class="btn btn-danger btn-sm delete-btn" data-id="${question.id}">Eliminar</button>
                </td>
            </tr>
        `).join("");

        document.querySelectorAll(".delete-btn").forEach(button => {
            button.addEventListener("click", async function () {
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