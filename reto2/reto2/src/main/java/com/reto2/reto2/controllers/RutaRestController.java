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

import com.reto2.reto2.model.entity.Ruta;
import com.reto2.reto2.service.IRutaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class RutaRestController {
	
	@Autowired
	private IRutaService rutaService;
	
	@GetMapping("/rutas")
	public List<Ruta> index(){
		return rutaService.findAll();
	}
	
	@GetMapping("/rutas/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Ruta ruta = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			ruta = rutaService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(ruta == null) {
			response.put("mensaje", "La ruta ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Ruta>(ruta, HttpStatus.OK);
	}
	
	@PostMapping("/rutas")
	public ResponseEntity<?> create(@Valid @RequestBody Ruta ruta, BindingResult result) {
		
		Ruta rutaNew = null;
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
			rutaNew = rutaService.save(ruta);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La ruta ha sido creado con éxito!");
		response.put("ruta", rutaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/rutas/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Ruta ruta, BindingResult result, @PathVariable Long id) {

		Ruta rutaActual = rutaService.findById(id);

		Ruta rutaUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (rutaActual == null) {
			response.put("mensaje", "Error: no se pudo editar, la ruta ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			rutaActual.setNombre(ruta.getNombre());
			rutaActual.setDescripcion(ruta.getDescripcion());
			rutaActual.setFecha(ruta.getFecha());
			rutaActual.setLugar(ruta.getLugar());
			

			rutaActual = rutaService.save(rutaActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la ruta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La ruta ha sido actualizado con éxito!");
		response.put("ruta", rutaUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/rutas/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			rutaService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la ruta de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La ruta eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
