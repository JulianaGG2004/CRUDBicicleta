package com.reto2.reto2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reto2.reto2.model.dao.ICompetenciaDao;
import com.reto2.reto2.model.entity.Competencia;

@Service
public class CompetenciaServiceImpl implements ICompetenciaService{
	
	@Autowired
	private ICompetenciaDao competenciaDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Competencia> findAll() {
		return (List<Competencia>) competenciaDao.findAll();
	}

	
	@Override
	@Transactional(readOnly = true)
	public Competencia findById(Long id) {
		return competenciaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Competencia save(Competencia competencia) {
		return competenciaDao.save(competencia);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		competenciaDao.deleteById(id);
	}

}
