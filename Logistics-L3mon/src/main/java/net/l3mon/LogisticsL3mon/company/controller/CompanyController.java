package net.l3mon.LogisticsL3mon.company.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.l3mon.LogisticsL3mon.Server.ErrorResponse;
import net.l3mon.LogisticsL3mon.UserAuth.dto.UserRegisterDTO;
import net.l3mon.LogisticsL3mon.UserAuth.entity.AuthResponse;
import net.l3mon.LogisticsL3mon.UserAuth.entity.Code;
import net.l3mon.LogisticsL3mon.UserAuth.exceptions.UserExistingWithMail;
import net.l3mon.LogisticsL3mon.UserAuth.exceptions.UserExistingWithName;
import net.l3mon.LogisticsL3mon.UserAuth.exceptions.UserNullConpanyIdException;
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
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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
}
