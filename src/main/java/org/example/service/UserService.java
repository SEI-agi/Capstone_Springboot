package org.example.service;

import org.example.dto.UserCreateRequest;
import org.example.dto.UserResponse;
import org.example.dto.UserUpdateRequest;
import org.example.exception.ResourceNotFoundException;
import org.example.model.User;
import org.example.model.enums.UserRole;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<UserResponse> findAll() {
		return userRepository.findAll().stream().map(DtoMapper::toUserResponse).toList();
	}

	public UserResponse findById(Long id) {
		return DtoMapper.toUserResponse(findEntityById(id));
	}

	public UserResponse create(UserCreateRequest request) {
		User user = new User();
		user.setName(request.name());
		user.setEmail(request.email());
		user.setPassword(request.password());
		user.setPhone(request.phone());
		user.setRole(parseRole(request.role()));
		return DtoMapper.toUserResponse(userRepository.save(user));
	}

	public UserResponse update(Long id, UserUpdateRequest request) {
		User user = findEntityById(id);
		if (request.name() != null) {
			user.setName(request.name());
		}
		if (request.email() != null) {
			user.setEmail(request.email());
		}
		if (request.password() != null) {
			user.setPassword(request.password());
		}
		if (request.phone() != null) {
			user.setPhone(request.phone());
		}
		if (request.role() != null) {
			user.setRole(parseRole(request.role()));
		}
		return DtoMapper.toUserResponse(userRepository.save(user));
	}

	public void delete(Long id) {
		userRepository.delete(findEntityById(id));
	}

	public User findEntityById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));
	}

	private UserRole parseRole(String role) {
		if (role == null || role.isBlank()) {
			return UserRole.PATIENT;
		}
		try {
			return UserRole.valueOf(role.trim().toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Invalid user role: " + role);
		}
	}
}
//package org.example.service;
//
//import org.example.model.User;
//import org.example.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserService {
//    @Autowired
//    UserRepository userRepository;
//
//    public User saveUser(User user){
//        return userRepository.save(user);
//    }
//
//    public List<User> allUsers(){
//        return userRepository.findAll();
//    }
//
//    public Optional<User> userById(Long id){
//        return userRepository.findById(id);
//    }
//
//    public User updateUser(User user){
//        return userRepository.save(user);
//    }
//
//}
