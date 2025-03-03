package com.fdez_rumi_questions.app.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdez_rumi_questions.app.entity.MultipleChoiceQuestion;
import com.fdez_rumi_questions.app.entity.Question;
import com.fdez_rumi_questions.app.entity.QuestionMaker;
import com.fdez_rumi_questions.app.entity.TrueFalseQuestion;
import com.fdez_rumi_questions.app.entity.TypeOfQuestion;
import com.fdez_rumi_questions.app.service.QuestionService;

@RestController
@RequestMapping("/api/question")
@Tag(name = "Gestión de Preguntas", description = "Endpoints para gestionar preguntas")
public class QuestionRestController {

	@Autowired
	private QuestionService questionService;
	
	@Operation(summary = "Obtener todas las preguntas", description = "Devuelve una lista con todas las preguntas registradas.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Lista de preguntas obtenida correctamente"),
	    @ApiResponse(responseCode = "404", description = "No hay preguntas registradas")
	})
	@GetMapping("/all")
	public ResponseEntity<List<Question>> getAllQuestions() {
		List<Question> questions = questionService.getAllQuestions();
		if (questions.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(questions);
	}

	@Operation(summary = "Obtener una pregunta por ID", description = "Devuelve los datos de una pregunta específica según su ID.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Pregunta encontrada"),
	    @ApiResponse(responseCode = "404", description = "Pregunta no encontrada")
	})
	@GetMapping("/{id}")
	public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
		Question question = questionService.getQuestionById(id);
		if (question == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(question);
	}
	
	@Operation(summary = "Agregar una nueva pregunta", description = "Crea y almacena una nueva pregunta en la base de datos.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "201", description = "Pregunta creada exitosamente"),
	    @ApiResponse(responseCode = "400", description = "Datos inválidos o malformados")
	})
	@PostMapping("/add")
	public ResponseEntity<Question> addQuestion(@RequestBody Question newQuestionData) {
		try {
			Question newQuestion = createQuestionFromData(newQuestionData);
			Question savedQuestion = questionService.createQuestion(newQuestion);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista de todas las categorías de preguntas disponibles.")
	@GetMapping("/categories")
	public ResponseEntity<Set<String>> getCategories() {
	    Set<String> categories = questionService.getAllCategories();
	    return ResponseEntity.ok(categories);
	}

	@Operation(summary = "Actualizar una pregunta", description = "Modifica los datos de una pregunta existente según su ID.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Pregunta actualizada correctamente"),
	    @ApiResponse(responseCode = "404", description = "Pregunta no encontrada")
	})
	@PutMapping("/update/{id}")
	public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question updatedQuestion) {
		Question existingQuestion = questionService.getQuestionById(id);

		if (existingQuestion == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		existingQuestion.setCategory(updatedQuestion.getCategory());
		existingQuestion.setText(updatedQuestion.getText());

		if (existingQuestion instanceof MultipleChoiceQuestion && updatedQuestion instanceof MultipleChoiceQuestion) {			
			MultipleChoiceQuestion existingMultipleQuestion = (MultipleChoiceQuestion) existingQuestion;
			MultipleChoiceQuestion updatedMultipleQuestion = (MultipleChoiceQuestion) updatedQuestion;
			existingMultipleQuestion.setCorrectAnswers(updatedMultipleQuestion.getCorrectAnswers());
			existingMultipleQuestion.setFailAnswers(updatedMultipleQuestion.getFailAnswers());
		} else if (existingQuestion instanceof TrueFalseQuestion && updatedQuestion instanceof TrueFalseQuestion) {
			TrueFalseQuestion existingTrueOrFalseQuestion = (TrueFalseQuestion) existingQuestion;
			TrueFalseQuestion updatedTrueOrFalseQuestion = (TrueFalseQuestion) updatedQuestion;
			existingTrueOrFalseQuestion.setAnswer(updatedTrueOrFalseQuestion.getAnswer());
		}
		Question savedQuestion = questionService.createQuestion(existingQuestion);
		return ResponseEntity.ok(savedQuestion);
	}
	
	@Operation(summary = "Eliminar una pregunta", description = "Elimina una pregunta de la base de datos según su ID.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "204", description = "Pregunta eliminada correctamente"),
	    @ApiResponse(responseCode = "404", description = "Pregunta no encontrada")
	})
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
		questionService.deleteQuestion(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Subir preguntas desde un archivo JSON", description = "Permite cargar preguntas en la base de datos mediante un archivo JSON.")
	@ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Archivo procesado correctamente"),
	    @ApiResponse(responseCode = "400", description = "Error al procesar el archivo JSON")
	})
	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadQuestionsFromTheFormFile(@RequestParam("file") MultipartFile file) {
		try {
			String jsonContent = new String(file.getBytes());
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.readTree(jsonContent);

			questionService.proccessQuestionFromJson(jsonContent);
			return ResponseEntity.ok("Archivo JSON importado correctamente");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al procesar JSON: " + e.getMessage());	        
		}
	}
	
	private Question createQuestionFromData(Question newQuestionData) {
		if (newQuestionData instanceof MultipleChoiceQuestion) {
	        return QuestionMaker.makeQuestion(
	                TypeOfQuestion.MULTIPLE_ANSWER,
	                newQuestionData.getCategory(),
	                newQuestionData.getText(),
	                ((MultipleChoiceQuestion) newQuestionData).getCorrectAnswers(),
	                ((MultipleChoiceQuestion) newQuestionData).getFailAnswers()
	        );
	    } else if (newQuestionData instanceof TrueFalseQuestion) {
	        return QuestionMaker.makeQuestion(
	                TypeOfQuestion.TRUE_FALSE,
	                newQuestionData.getCategory(),
	                newQuestionData.getText(),
	                ((TrueFalseQuestion) newQuestionData).getAnswer()
	        );
	    } else {
	        throw new IllegalArgumentException("Tipo de pregunta no soportado");
	    }
	}
}