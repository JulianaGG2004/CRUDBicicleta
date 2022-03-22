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

import com.reto2.reto2.model.entity.Evento;
import com.reto2.reto2.service.IEventoService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class EventoRestController {
	
	@Autowired
	private IEventoService eventoService;
	
	@GetMapping("/eventos")
	public List<Evento> index(){
		return eventoService.findAll();
	}
	
	
	
	@GetMapping("/eventos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Evento evento = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			evento = eventoService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(evento == null) {
			response.put("mensaje", "El evento ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Evento>(evento, HttpStatus.OK);
	}
	
	@PostMapping("/eventos")
	public ResponseEntity<?> create(@Valid @RequestBody Evento evento, BindingResult result) {
		
		Evento eventoNew = null;
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
			eventoNew = eventoService.save(evento);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El evento ha sido creado con éxito!");
		response.put("evento", eventoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/eventos/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Evento evento, BindingResult result, @PathVariable Long id) {

		Evento eventoActual = eventoService.findById(id);

		Evento eventoUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (eventoActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el evento ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			eventoActual.setNombre(evento.getNombre());
			eventoActual.setDescripcion(evento.getDescripcion());
			eventoActual.setFecha(evento.getFecha());
			eventoActual.setLugar(evento.getLugar());
			

			eventoActual = eventoService.save(eventoActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el evento en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El evento ha sido actualizado con éxito!");
		response.put("evento", eventoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/eventos/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			eventoService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el evento de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El evento eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
