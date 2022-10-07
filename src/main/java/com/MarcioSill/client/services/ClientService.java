package com.MarcioSill.client.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MarcioSill.client.dto.ClientDTO;
import com.MarcioSill.client.entities.Client;
import com.MarcioSill.client.repositories.ClientRepository;
import com.MarcioSill.client.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true) 
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
			Page<Client> list = repository.findAll(pageRequest);		
			return list.map(x -> new ClientDTO(x));	
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not foud"));
		return new ClientDTO(entity);
	}

	@Transactional(readOnly = true)
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		copyDtoEntity(dto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO upDate(Long id, ClientDTO dto) {
		try {
		Client entity = repository.getOne(id);
		copyDtoEntity(dto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}
	}
	
	private void copyDtoEntity(ClientDTO dto, Client entity) {
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
	
	}

}
