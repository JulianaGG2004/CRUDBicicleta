package com.reto2.reto2.service;

import java.util.List;

import com.reto2.reto2.model.entity.Evento;

public interface IEventoService {
	
public List<Evento> findAll();
	
	public Evento findById(Long id);
	
	public Evento save(Evento evento);
	
	public void delete(Long id);


}
