package com.reto2.reto2.service;

import java.util.List;


import com.reto2.reto2.model.entity.Competencia;

public interface ICompetenciaService {
	
public List<Competencia> findAll();
	
	public Competencia findById(Long id);
	
	public Competencia save(Competencia competencia);
	
	public void delete(Long id);

}
