package fpoly.bai4;

import org.springframework.data.jpa.repository.JpaRepository;

public
interface OrganizationUnitRepo extends JpaRepository<OrganizationUnit, Long> {
    boolean existsByUnitId(String unitId);
}
