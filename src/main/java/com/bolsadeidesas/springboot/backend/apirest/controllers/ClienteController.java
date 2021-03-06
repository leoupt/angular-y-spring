package com.bolsadeidesas.springboot.backend.apirest.controllers;



import java.io.IOException;
import java.net.MalformedURLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeidesas.springboot.backend.apirest.model.entity.Cliente;
import com.bolsadeidesas.springboot.backend.apirest.model.entity.Region;
import com.bolsadeidesas.springboot.backend.apirest.model.services.ClienteService;
import com.bolsadeidesas.springboot.backend.apirest.model.services.IClienteService;
import com.bolsadeidesas.springboot.backend.apirest.model.services.IUploadFileService;
import com.bolsadeidesas.springboot.backend.apirest.model.services.UploadFileService;


@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	@GetMapping("/clientes")
	public List<Cliente> findAll(){
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> findAll(@PathVariable Integer page){
		Pageable pageable = PageRequest.of(page, 4);
		return clienteService.findAll(pageable);
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Cliente cliente =null;
		Map<String,Object> errores = new HashMap<>();
		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			errores.put("mensaje", "Error de la consulta en la base de datos");
			errores.put("errors", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(errores,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		if (cliente==null) {
			errores.put("mensaje", "El Cliente con ID:".concat(id.toString()).concat(" no existe en la bbdd"));
			return new ResponseEntity<Map<String,Object>>(errores,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	@PostMapping("/clientes")
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
		Cliente clienteNew=null;
		Map<String,Object> errores = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> ers= new ArrayList<>();
			for (FieldError err : result.getFieldErrors()) {
				ers.add("El campo <" + err.getField() +"> " +err.getDefaultMessage());
				
			}
			errores.put("erroresv", ers);
			return new ResponseEntity<Map<String,Object>>(errores,HttpStatus.BAD_REQUEST);
		}
		try {
			clienteNew = clienteService.save(cliente);
		} catch (DataAccessException e) {
			errores.put("mensaje", "Error al insertar en la base de datos");
			errores.put("errors", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(errores,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		errores.put("mensaje","Cliente creado con exito");
		errores.put("cliente", clienteNew);
		return new ResponseEntity<Map<String,Object>>(errores,HttpStatus.CREATED);
	}
	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result,@PathVariable Long id) {
		
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> ers= new ArrayList<>();
			for (FieldError err : result.getFieldErrors()) {
				ers.add("El campo <" + err.getField() +"> " +err.getDefaultMessage());
				
			}
			response.put("erroresv", ers);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		Cliente clienteActual = clienteService.findById(id);
		Cliente clienteActualizado=null;
		if (clienteActual==null) {
			response.put("errors", "el usuario con ID:".concat(id.toString()).concat(" no existe en la base dde datos"));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setRegion(cliente.getRegion());
			
			
			clienteActualizado = clienteService.save(clienteActual);
			
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar en la base de datos");
			response.put("errors", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje","Cliente actualizado con exito");
		response.put("cliente", clienteActualizado);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String,Object> response = new HashMap<>();
		 try {
			 Cliente cliente = clienteService.findById(id);
			 String fotoAnterior = cliente.getFoto();
				
			 uploadFileService.eliminar(fotoAnterior);
			 
			 clienteService.Delete(id);
			 
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al eliminar en la base de datos");
				response.put("errors", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		 response.put("mensaje", "cliente eliminado con exito");
		 return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
	
	@PostMapping("clientes/upload")
	public ResponseEntity<?> upload(@RequestParam MultipartFile archivo, @RequestParam Long id){
		Map<String,Object> response = new HashMap<>();
		
		Cliente cliente = clienteService.findById(id);
		
		if(!archivo.isEmpty()) {
			
			String nombreArchivo=null;
			try {
			nombreArchivo = uploadFileService.copiar(archivo);
			}catch (IOException e) {
				response.put("mensaje", "Error al subir imagen en la base de datos");
				response.put("errors", e.getMessage().concat(":").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			String fotoAnterior = cliente.getFoto();
			uploadFileService.eliminar(fotoAnterior);
			
			cliente.setFoto(nombreArchivo);
			clienteService.save(cliente);

			response.put("mensaje", "El archivo ha sido subido correctamente");
			response.put("cliente",cliente);

		}
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	
	@GetMapping("upload/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){
		
		Resource recurso = null;
		
		try {
			recurso = uploadFileService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + recurso.getFilename() + "\"");
		
		return new ResponseEntity<Resource>(recurso,cabecera, HttpStatus.OK);
	}
	
	@GetMapping("clientes/regiones")
	public List<Region> listarRegiones(){
		return clienteService.finAllRegion();
	}
}
