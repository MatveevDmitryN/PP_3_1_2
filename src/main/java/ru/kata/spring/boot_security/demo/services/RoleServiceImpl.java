package ru.kata.spring.boot_security.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> getRoleById(Long id) {
        logger.info("Attempting to find role with ID {}", id);
        return roleRepository.findById(id);
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        logger.info("Saving role: {}", role.getName());
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteAllRoles() {
        logger.info("Deleting all roles");
        roleRepository.deleteAll();
    }
}
