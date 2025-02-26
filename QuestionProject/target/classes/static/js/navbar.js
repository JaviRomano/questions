export function createNavbar() {
    const navbarDiv = document.createElement("div");
    navbarDiv.innerHTML = `
        <nav class="navbar navbar-expand-lg navbar-dark bg-success">
            <div class="container">
                <a class="navbar-brand" href="/">Inicio</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item"><a class="nav-link" href="/question/all">Preguntas</a></li>
                        <li class="nav-item"><a class="nav-link" href="api/question/add">Crear nueva pregunta</a></li>
						<li class="nav-item"><a class="nav-link" href="/question/upload">Importar desde Json</a></li>
                    </ul>
                </div>
            </div>
        </nav>
    `;	
	   return navbarDiv;
	}