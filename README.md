# 📚 Question API - Sistema de Preguntas 📊 (EN DESARROLLO)

Bienvenido a **Question API**, una aplicación desarrollada en **Spring Boot** para la gestión de preguntas de opción múltiple y verdadero/falso. Con esta API puedes agregar, actualizar, eliminar e importar preguntas desde archivos JSON. 🚀

---

## 🛠️ Tecnologías Utilizadas

- **Spring Boot 3** 🌱
- **Spring Data JPA** 🗃️
- **H2 Database** (Modo en memoria para pruebas) 🏛️
- **Lombok** 🛠️
- **Postman** (Para pruebas de API) 🔥
- **Bootstrap** (Para mejorar la UI) 🎨
- **Thymeleaf** (Para vistas dinámicas) 🖥️
- **Swagger OpenAPI** (Documentación de API) 📄

---

## 📌 Características Principales

✔️ CRUD completo de preguntas (Crear, Leer, Actualizar, Eliminar).  
✔️ Importación de preguntas desde archivos JSON.  
✔️ Soporte para preguntas de **opción múltiple** y **verdadero/falso**.  
✔️ API REST para gestionar preguntas.  
✔️ Documentación interactiva con **Swagger UI**.  

---

## 🚀 Instalación y Configuración

### 🔹 1. Clonar el Repositorio  
~~~bash
git clone https://github.com/tu-usuario/question-api.git
cd question-api
~~~

### 🔹 2. Configurar Base de Datos (Opcional)

Por defecto, la aplicación usa H2 Database en memoria.
Si deseas usar MySQL, edita application.properties:
~~~
spring.datasource.url=jdbc:mysql://localhost:3306/question_db
spring.datasource.username=root
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
~~~
