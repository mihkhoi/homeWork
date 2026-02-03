package fpoly.bai4;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller public class OrganizationUnitController {

  private
    final OrganizationUnitRepo repo;

  public
    OrganizationUnitController(OrganizationUnitRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/") public String home() {
        return "redirect:/org-unit/add";
    }

    @GetMapping("/org-unit/add") public String showForm(Model model) {
        model.addAttribute("orgUnit", new OrganizationUnit());
        return "add_org_unit";
    }

    @PostMapping("/org-unit/save") public String save(@Valid @ModelAttribute("orgUnit") OrganizationUnit orgUnit,
                                                      BindingResult result,
                                                      Model model) {

        // check unique UnitId
        if (!result.hasFieldErrors("unitId") && repo.existsByUnitId(orgUnit.getUnitId())) {
            result.rejectValue("unitId", "duplicate", "Unit Id already exists");
        }

        if (result.hasErrors()) {
            return "add_org_unit";
        }

        repo.save(orgUnit);
        model.addAttribute("msg", "Saved successfully!");
        model.addAttribute("orgUnit", new OrganizationUnit());
        return "add_org_unit";
    }

    @PostMapping("/org-unit/cancel") public String cancel() {
        return "redirect:/org-unit/add";
    }
}
