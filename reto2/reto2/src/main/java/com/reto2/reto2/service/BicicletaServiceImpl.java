package com.reto2.reto2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reto2.reto2.model.dao.IBicicletaDao;
import com.reto2.reto2.model.entity.Bicicleta;


@Service
public class BicicletaServiceImpl implements IBicicletaService{
	
	@Autowired
	private IBicicletaDao bicicletaDao;

	@Override
	@Transactional(readOnly = true)
	public List<Bicicleta> findAll() {
		return (List<Bicicleta>) bicicletaDao.findAll();
	}

	
	@Override
	@Transactional(readOnly = true)
	public Bicicleta findById(Long id) {
		return bicicletaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Bicicleta save(Bicicleta bicicleta) {
		return bicicletaDao.save(bicicleta);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		bicicletaDao.deleteById(id);
	}
}
