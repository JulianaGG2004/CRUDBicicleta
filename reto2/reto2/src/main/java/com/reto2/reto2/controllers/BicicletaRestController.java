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


import com.reto2.reto2.model.entity.Bicicleta;
import com.reto2.reto2.service.IBicicletaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class BicicletaRestController {

	@Autowired
	private IBicicletaService bicicletaService;
	
	@GetMapping("/bicicletas")
	public List<Bicicleta> index(){
		return bicicletaService.findAll();
	}
	
	
	
	@GetMapping("/bicicletas/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Bicicleta bicicleta = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			bicicleta = bicicletaService.findById(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(bicicleta == null) {
			response.put("mensaje", "La categoria ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Bicicleta>(bicicleta, HttpStatus.OK);
	}
	
	@PostMapping("/bicicletas")
	public ResponseEntity<?> create(@Valid @RequestBody Bicicleta bicicleta, BindingResult result) {
		
		Bicicleta bicicletaNew = null;
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
			bicicletaNew = bicicletaService.save(bicicleta);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La categoria ha sido creado con éxito!");
		response.put("bicicleta", bicicletaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	@PutMapping("/bicicletas/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Bicicleta bicicleta, BindingResult result, @PathVariable Long id) {

		Bicicleta bicicletaActual = bicicletaService.findById(id);

		Bicicleta bicicletaUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (bicicletaActual == null) {
			response.put("mensaje", "Error: no se pudo editar, la bicicleta ID: "
					.concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			bicicletaActual.setMarca(bicicleta.getMarca());
			bicicletaActual.setTipo(bicicleta.getTipo());
			bicicletaActual.setColor(bicicleta.getColor());
			bicicletaActual.setEstado(bicicleta.getEstado());
			bicicletaActual.setUsuario(bicicleta.getUsuario());
			bicicletaActual.setLatitud(bicicleta.getLatitud());
			bicicletaActual.setLongitud(bicicleta.getLongitud());
			

			bicicletaUpdated = bicicletaService.save(bicicletaActual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar la bicicleta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "La bicicleta ha sido actualizado con éxito!");
		response.put("bicicleta", bicicletaUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/bicicletas/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			bicicletaService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la bicicleta de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La bicicleta eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
