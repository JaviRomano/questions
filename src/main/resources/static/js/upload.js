document.addEventListener("DOMContentLoaded", function() {
	const body = document.body;

	const mainContainer = document.createElement("main");
	mainContainer.classList.add("container", "mt-5", "text-center");

	const title = document.createElement("h1");
	title.classList.add("mb-5");
	title.style.color = "#000000";
	title.textContent = "Cargar Preguntas desde JSON";

	const form = document.createElement("form");
	form.method = "post";
	form.action = "/question/upload";
	form.enctype = "multipart/form-data";
	form.id = "uploadForm";

	const inputDiv = document.createElement("div");
	const fileInput = document.createElement("input");
	fileInput.classList.add("btn", "btn-light");
	fileInput.type = "file";
	fileInput.id = "fileInput";
	fileInput.name = "file";
	fileInput.accept = ".json";
	fileInput.required = true;
	inputDiv.appendChild(fileInput);

	const submitButton = document.createElement("button");
	submitButton.classList.add("mt-5", "btn", "btn-success");
	submitButton.type = "submit";
	submitButton.textContent = "Cargar JSON";

	const homeButton = document.createElement("a");
	homeButton.href = "/";
	homeButton.classList.add("btn", "btn-success", "mt-3");
	homeButton.textContent = "Inicio";

	const messageDiv = document.createElement("div");
	messageDiv.id = "message";
	messageDiv.classList.add("mt-3");

	form.appendChild(inputDiv);
	form.appendChild(submitButton);
	mainContainer.appendChild(title);
	mainContainer.appendChild(form);
	mainContainer.appendChild(messageDiv);
	mainContainer.appendChild(homeButton);
	body.appendChild(mainContainer);

	const backgroundContainer = document.createElement("div");
	backgroundContainer.classList.add("background-container");
	backgroundContainer	.style.cssText = `
	        background-image: url('/img/json.jpg');
	        background-size: cover;
	        background-position: center;
	        position: absolute;
	        top: 0;
	        left: 0;
	        width: 100%;
	        height: 100%;
	        z-index: -1;
	   	`;
	body.appendChild(backgroundContainer);

	form.addEventListener("submit", async function(event) {
		event.preventDefault();

		const file = fileInput.files[0];

		if (!file) {
			messageDiv.innerHTML = `<div class="alert alert-warning">Por favor, selecciona un archivo JSON.</div>`;
			return;
		}

		const formData = new FormData();
		formData.append("file", file);

		try {
			const response = await fetch("/api/question/upload", {
				method: "POST",
				body: formData
			});

			if (response.ok) {
				messageDiv.innerHTML = `<div class="alert alert-success">Archivo subido correctamente.</div>`;
			} else {
				const errorMessage = await response.text();
				messageDiv.innerHTML = `<div class="alert alert-danger">Error: ${errorMessage}</div>`;
			}
		} catch (error) {
			messageDiv.innerHTML = `<div class="alert alert-danger">Error de conexión con el servidor.</div>`;
		}
	});
});
