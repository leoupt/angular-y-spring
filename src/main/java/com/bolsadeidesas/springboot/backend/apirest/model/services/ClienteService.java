package com.bolsadeidesas.springboot.backend.apirest.model.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeidesas.springboot.backend.apirest.model.entity.Cliente;
import com.bolsadeidesas.springboot.backend.apirest.model.entity.Region;
import com.bolsadeidesas.springboot.backend.apirest.model.repository.IClienteRepository;

@Service
public class ClienteService implements IClienteService {

	@Autowired
	private IClienteRepository clienteRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return (List<Cliente>) clienteRepository.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return clienteRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		// TODO Auto-generated method stub
		return clienteRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		
		return clienteRepository.save(cliente);
	}

	@Override
	@Transactional
	public void Delete(Long id) {
		// TODO Auto-generated method stub
		clienteRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Region> finAllRegion() {
	
		return clienteRepository.finAllRegion();
	}

	

	
}
