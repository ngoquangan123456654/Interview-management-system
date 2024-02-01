package fa.training.fjb04.ims.service.impl;

import fa.training.fjb04.ims.entity.common.Roles;
import fa.training.fjb04.ims.repository.common.RoleRepository;
import fa.training.fjb04.ims.service.common.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;


    @Override
    public List<fa.training.fjb04.ims.entity.common.Roles> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Roles findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
