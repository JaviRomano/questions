import { loadHeader } from "/js/header.js";
import { createNavbar } from "/js/navbar.js";

loadHeader();

document.addEventListener("DOMContentLoaded", function() {		
	const body = document.body;
	  
	body.prepend(createNavbar());	

	const title = document.createElement("h1");
	title.classList.add("title-overlay");
	title.textContent = "Bienvenido a la Aplicación de Preguntas";
	
	const backgroundContainer = document.createElement("div");
	backgroundContainer.classList.add("background-container");
	backgroundContainer.innerHTML = `
	<img src="/img/riddler2.jpg" alt="Fondo">
	`;

	const apiButtonDiv = document.createElement("div");
	apiButtonDiv.classList.add("position-fixed", "bottom-0", "start-0", "m-3");
	apiButtonDiv.innerHTML = `<a href="http://localhost:8080/swagger-ui/index.html" class="btn btn-success">Documentación Swagger</a>`;

	body.appendChild(title);
	body.appendChild(backgroundContainer);
	body.appendChild(apiButtonDiv);
});
