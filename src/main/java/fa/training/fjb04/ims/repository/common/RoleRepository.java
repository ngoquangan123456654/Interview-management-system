package fa.training.fjb04.ims.repository.common;

import fa.training.fjb04.ims.entity.common.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Roles findByRoleName(String roleName);


}
