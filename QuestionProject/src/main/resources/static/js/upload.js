document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("uploadForm");
    const fileInput = document.getElementById("fileInput");
    const messageDiv = document.getElementById("message");

    form.addEventListener("submit", async function (event) {
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
