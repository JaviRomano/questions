document.addEventListener("DOMContentLoaded", function () {
    const container = document.createElement("div");
    container.style.cssText = "display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100vh; background-color: #f8f8f8; font-family: Arial, sans-serif; text-align: center;";

    const title = document.createElement("h1");
    title.textContent = "500 - Internal Server Error";
    title.style.cssText = "color: #d9534f; font-size: 4em; margin-bottom: 10px;";

    const subtitle = document.createElement("h2");
    subtitle.textContent = "Ups! Algo salió mal en nuestro servidor.";
    subtitle.style.cssText = "color: #555; font-size: 1.5em; margin-bottom: 20px;";

    const img = document.createElement("img");
    img.src = "/img/error500.png"; // Asegúrate de tener esta imagen en tu carpeta static/img
    img.alt = "Error 500";
    img.style.cssText = "max-width: 300px; margin-bottom: 20px;";

    const message = document.createElement("p");
    message.textContent = "Por favor, intenta nuevamente más tarde o contacta al soporte si el problema persiste.";
    message.style.cssText = "color: #777; font-size: 1.2em; max-width: 600px;";

    const backButton = document.createElement("button");
    backButton.textContent = "Volver al inicio";
    backButton.style.cssText = "background-color: #5bc0de; color: white; border: none; padding: 10px 20px; font-size: 1.2em; cursor: pointer; border-radius: 5px;";
    backButton.addEventListener("click", function () {
        window.location.href = "/";
    });

    container.appendChild(title);
    container.appendChild(subtitle);
    container.appendChild(img);
    container.appendChild(message);
    container.appendChild(backButton);

    document.body.innerHTML = "";
    document.body.appendChild(container);
});