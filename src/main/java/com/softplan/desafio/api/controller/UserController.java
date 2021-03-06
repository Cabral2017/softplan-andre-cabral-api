package com.softplan.desafio.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.softplan.desafio.api.payload.request.UserRequest;
import com.softplan.desafio.api.payload.response.UserResponse;
import com.softplan.desafio.auth.payload.request.SignupRequest;
import com.softplan.desafio.exception.NotFoundException;
import com.softplan.desafio.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping
	@ApiOperation(value = "Listar todos usuários", authorizations = { @Authorization(value = "Bearer Authentication") })
	@ApiResponses({ @ApiResponse(code = 401, message = "Acesso não autorizado."), })
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN') or hasRole('TRIADOR')")
	public List<UserResponse> findAll() {
		return userService.findAll();
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Listar por ID", authorizations = { @Authorization(value = "Bearer Authentication") })
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses({
		@ApiResponse(code = 401, message = "Acesso não autorizado."),
		@ApiResponse(code = 404, message = "Não encontrado")
	})
	@PreAuthorize("hasRole('ADMIN')")
	public UserResponse findById(@ApiParam(required = true) @PathVariable Long id) {
		try {
			return userService.findById(id);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}
	


	@PostMapping("/signup")
	@ApiOperation(value = "Cadastrar usuário", authorizations = { @Authorization(value = "Bearer Authentication") })
	@ApiResponses({
		@ApiResponse(code = 401, message = "Acesso não autorizado."), 
		@ApiResponse(code = 400, message = "Solicitação inválida.")
	})
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> userRegister(@Valid @RequestBody SignupRequest signUpRequest) {
		
		return userService.register(signUpRequest);
		
	}
	
	@PutMapping("/{id}")
	@ApiOperation(value="Atualizar", authorizations = {@Authorization(value="Bearer Authentication")})
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses({
		@ApiResponse(code = 401, message = "Acesso não autorizado."),
		@ApiResponse(code = 404, message = "Não encontrado")
	})
	@PreAuthorize("hasRole('ADMIN')")
	public UserResponse update(
			@ApiParam(required=true) @PathVariable Long id,
			@ApiParam(required=true) @RequestBody UserRequest dto) {
		try {
			return userService.update(id, dto);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation(value="Remover", authorizations = {@Authorization(value="Bearer Authentication")})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiResponses({
		@ApiResponse(code = 401, message = "Acesso não autorizado."),
		@ApiResponse(code = 404, message = "Não encontrado"),
		@ApiResponse(code = 500, message = "Erro interno.")
	})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteById(@ApiParam(required=true) @PathVariable Long id) {
		try {
			return userService.deleteById(id);
		} catch (NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		}
	}
}
