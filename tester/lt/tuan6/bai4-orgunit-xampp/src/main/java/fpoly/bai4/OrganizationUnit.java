package fpoly.bai4;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
    @Table(name = "org_units", uniqueConstraints = { @UniqueConstraint(name = "uk_unit_id", columnNames = "unitId") }) public class OrganizationUnit {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @NotBlank(message = "Unit Id is required")
        @Size(max = 20, message = "Unit Id max 20 chars")
            @Column(nullable = false, length = 20) private String unitId;

    @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name max 100 chars")
            @Column(nullable = false, length = 100) private String name;

    // CỐ TÌNH: không validate length để bạn có 1 testcase FAIL + bug report
    @Column(length = 500) private String description;

  public
    Long getId() {
        return id;
    }

  public
    String getUnitId() {
        return unitId;
    }
  public
    void setUnitId(String unitId) {
        this.unitId = unitId;
    }

  public
    String getName() {
        return name;
    }
  public
    void setName(String name) {
        this.name = name;
    }

  public
    String getDescription() {
        return description;
    }
  public
    void setDescription(String description) {
        this.description = description;
    }
}
