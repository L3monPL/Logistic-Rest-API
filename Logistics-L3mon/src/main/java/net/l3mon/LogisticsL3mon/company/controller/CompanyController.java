package net.l3mon.LogisticsL3mon.company.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.l3mon.LogisticsL3mon.Server.ErrorResponse;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.company.dto.CompanyDTO;
import net.l3mon.LogisticsL3mon.company.entity.Company;
import net.l3mon.LogisticsL3mon.company.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyService companyService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<?>  addNewCompany(@RequestBody CompanyDTO company){
        try {
            return ResponseEntity.ok(companyService.create(company));
        } catch (GlobalExceptionMessage ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @RequestMapping(path = "/companies", method = RequestMethod.GET)
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        if (companies.isEmpty()) {
            return ResponseEntity.noContent().build(); // Brak firm do wyświetlenia
        } else {
            return ResponseEntity.ok(companies); // Zwróć listę firm
        }
    }

//    @RequestMapping(path = "/company/list", method = RequestMethod.GET)
//    public ResponseEntity<List<Company>> getAllUserCompany() {
//        List<Company> companies = companyService.getAllCompanies();
//        if (companies.isEmpty()) {
//            return ResponseEntity.noContent().build(); // Brak firm do wyświetlenia
//        } else {
//            return ResponseEntity.ok(companies); // Zwróć listę firm
//        }
//    }

}
