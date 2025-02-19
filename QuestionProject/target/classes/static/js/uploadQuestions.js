import { loadHeader } from "/js/header.js";
import { createNavbar } from "/js/navbar.js";

loadHeader();

document.addEventListener("DOMContentLoaded", function () {
    const body = document.body;
    body.classList.add("bg-dark", "questions");

    body.prepend(createNavbar());

    const mainContainer = document.createElement("main");
    mainContainer.classList.add("container", "mt-5", "text-center");

    const title = document.createElement("h1");
    title.classList.add("mb-5");
    title.textContent = "Cargar archivo JSON de Preguntas";

    const form = document.createElement("form");
    form.method = "post";
    form.action = "/question/upload";
    form.enctype = "multipart/form-data";

    const inputDiv = document.createElement("div");
    const fileInput = document.createElement("input");
    fileInput.classList.add("btn", "btn-light");
    fileInput.type = "file";
    fileInput.id = "file";
    fileInput.name = "file";
    fileInput.accept = ".json";
    fileInput.required = true;
    inputDiv.appendChild(fileInput);

    const submitButton = document.createElement("button");
    submitButton.classList.add("mt-5", "btn", "btn-light");
    submitButton.type = "submit";
    submitButton.textContent = "Subir";

    form.appendChild(inputDiv);
    form.appendChild(submitButton);
    mainContainer.appendChild(title);
    mainContainer.appendChild(form);
    body.appendChild(mainContainer);

    const backgroundDiv = document.createElement("div");
    backgroundDiv.style.cssText = `
        background-image: url('/img/riddler1.jpg');
        background-size: cover;
        background-position: center;
        opacity: 0.5;
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        z-index: -1;
    `;
    body.appendChild(backgroundDiv);

});
