document.addEventListener("DOMContentLoaded", function() {
	const container = document.createElement("div");
	container.style.cssText = "display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100vh; background-color: #f8f8f8; font-family: Arial, sans-serif; text-align: center;";

	const title = document.createElement("h1");
	title.textContent = "404 - Página no encontrada . . .";
	title.style.cssText = "color: #198754; font-size: 4em; margin-bottom: 10px;";

	const subtitle = document.createElement("h2");
	subtitle.textContent = "Vaya, la página que buscas no existe.";
	subtitle.style.cssText = "color: #555; font-size: 1.5em; margin-bottom: 20px;";

	const img = document.createElement("img");
	img.src = "/img/yamcha.jpg";
	img.alt = "Error 404";
	img.style.cssText = "max-width: 400px; margin-bottom: 20px;";

	const message = document.createElement("p");
	message.textContent = "Por favor, verifica la URL o regresa a la página principal.";
	message.style.cssText = "color: #777; font-size: 1.2em; max-width: 600px;";

	const pathMessage = document.createElement("p");
	pathMessage.textContent = `Ruta no encontrada: ${window.location.pathname}`;
	pathMessage.style.cssText = "color: #198754; font-size: 1.2em; padding: 2px; border: 1px solid black; background-color: black; margin-top: 20px;";


	const backButton = document.createElement("button");
	backButton.textContent = "Volver al inicio";
	backButton.style.cssText = "background-color: #198754; color: black; border: 1px solid; padding: 10px 20px; font-size: 1.2em; cursor: pointer; border-radius: 5px;";
	backButton.addEventListener("click", function() {
		window.location.href = "/";
	});

	container.appendChild(title);
	container.appendChild(subtitle);
	container.appendChild(img);
	container.appendChild(message);
	container.appendChild(pathMessage);
	container.appendChild(backButton);

	document.body.innerHTML = "";
	document.body.appendChild(container);
});