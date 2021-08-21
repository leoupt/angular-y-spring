package com.bolsadeidesas.springboot.backend.apirest.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeidesas.springboot.backend.apirest.model.entity.Cliente;
import com.bolsadeidesas.springboot.backend.apirest.model.entity.Region;


public interface IClienteRepository extends JpaRepository<Cliente, Long> {

	@Query("from Region")
	public List<Region> finAllRegion();
}
