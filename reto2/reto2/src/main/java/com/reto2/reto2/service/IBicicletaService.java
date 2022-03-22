package com.reto2.reto2.service;

import java.util.List;


import com.reto2.reto2.model.entity.Bicicleta;


public interface IBicicletaService {
	
	public List<Bicicleta> findAll();
	
	public Bicicleta findById(Long id);
	
	public Bicicleta save(Bicicleta bicicleta);
	
	public void delete(Long id);

}
