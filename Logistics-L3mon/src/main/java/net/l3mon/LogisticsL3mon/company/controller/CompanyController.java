package net.l3mon.LogisticsL3mon.company.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.l3mon.LogisticsL3mon.Server.ErrorResponse;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.company.dto.CompanyDTO;
import net.l3mon.LogisticsL3mon.company.entity.Company;
import net.l3mon.LogisticsL3mon.company.entity.CompanyInviteLink;
import net.l3mon.LogisticsL3mon.company.entity.CompanyUser;
import net.l3mon.LogisticsL3mon.company.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //to change - przenieść na admina
    @RequestMapping(path = "/companies", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCompanies() {
        List<?> companies;
        try {
            companies = companyService.getAllCompanies();
        } catch (GlobalExceptionMessage ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(companies);
    }

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUserCompany() {
        List<?> companies;
        try {
            companies = companyService.getAllUserCompany();
        } catch (GlobalExceptionMessage ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(companies);
    }

    @RequestMapping(path = "/{companyId}/invite/code", method = RequestMethod.GET)
    public ResponseEntity<?> getCompanyInviteCodeById(@PathVariable Long companyId) {
        CompanyInviteLink companyInviteLink;
        try {
            companyInviteLink = companyService.getCompanyInviteCode(companyId);
        } catch (GlobalExceptionMessage ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(companyInviteLink);
    }

    @RequestMapping(path = "/join/{code}", method = RequestMethod.POST)
    public ResponseEntity<?>  joinToCompany(@PathVariable String code){
        try {
            return ResponseEntity.ok(companyService.joinCompanyByCode(code));
        } catch (GlobalExceptionMessage ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @RequestMapping(path = "/{companyId}/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsersCompanyById(@PathVariable Long companyId) {
        List<?> users;
        try {
            users = companyService.getAllUsersCompanyById(companyId);
        } catch (GlobalExceptionMessage ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(users);
    }

}
