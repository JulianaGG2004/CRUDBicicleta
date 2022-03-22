package com.reto2.reto2.service;

import java.util.List;


import com.reto2.reto2.model.entity.Ruta;

public interface IRutaService {
	
public List<Ruta> findAll();
	
	public Ruta findById(Long id);
	
	public Ruta save(Ruta ruta);
	
	public void delete(Long id);

}
