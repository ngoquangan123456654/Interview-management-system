package fa.training.fjb04.ims.service.common;


import fa.training.fjb04.ims.entity.common.Roles;

import java.util.List;

public interface RoleService {
    List<Roles> findAll();

    Roles findByRoleName(String roleName);
}
