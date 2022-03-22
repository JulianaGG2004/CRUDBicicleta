package com.reto2.reto2.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.reto2.reto2.model.entity.Competencia;

import com.reto2.reto2.service.ICompetenciaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class CompetenciaRestController {
	
	@Autowired
	private ICompetenciaService competenciaService;
	
	@GetMapping("/competencias")
	public List<Competencia> index(){
		return competenciaService.findAll();
	}
	
	
	
	@GetMapping("/competencias/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Competencia competencia = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			competencia = competenciaService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(competencia == null) {
			response.put("mensaje", "La competencia ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Competencia>(competencia, HttpStatus.OK);
	}
	
	@PostMapping("/competencias")
	public ResponseEntity<?> create(@Valid @RequestBody Competencia competencia, BindingResult result) {
		
		Competencia competenciaNew = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			competenciaNew = competenciaService.save(competencia);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La competencia ha sido creado con éxito!");
		response.put("competencia", competenciaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/competencias/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Competencia competencia, BindingResult result, @PathVariable Long id) {

		Competencia competenciaActual = competenciaService.findById(id);

		Competencia competenciaUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (competenciaActual == null) {
			response.put("mensaje", "Error: no se pudo editar, la competencia ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			competenciaActual.setNombre(competencia.getNombre());
			competenciaActual.setDescripcion(competencia.getDescripcion());
			competenciaActual.setFecha(competencia.getFecha());
			competenciaActual.setLugar(competencia.getLugar());
			

			competenciaActual = competenciaService.save(competenciaActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la competencia en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La competencia ha sido actualizado con éxito!");
		response.put("competencia", competenciaUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/competencias/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			competenciaService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la competencia de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La competencia eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
