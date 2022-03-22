package com.reto2.reto2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reto2.reto2.model.dao.IEventoDao;
import com.reto2.reto2.model.entity.Evento;

@Service
public class EventoServiceImpl implements IEventoService{
	
	@Autowired
	private IEventoDao eventoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Evento> findAll() {
		return (List<Evento>) eventoDao.findAll();
	}

	
	@Override
	@Transactional(readOnly = true)
	public Evento findById(Long id) {
		return eventoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Evento save(Evento evento) {
		return eventoDao.save(evento);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		eventoDao.deleteById(id);
	}
}
