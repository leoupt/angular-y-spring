package com.bolsadeidesas.springboot.backend.apirest.model.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeidesas.springboot.backend.apirest.model.entity.Cliente;
import com.bolsadeidesas.springboot.backend.apirest.model.entity.Region;

public interface IClienteService {

	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable);
	
	public Cliente findById(Long id);
	
	public Cliente save(Cliente cliente);
	
	public void Delete(Long id);
	
	public List<Region> finAllRegion();
}
