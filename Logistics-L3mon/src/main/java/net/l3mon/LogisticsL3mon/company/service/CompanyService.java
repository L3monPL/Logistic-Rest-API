package net.l3mon.LogisticsL3mon.company.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.UserAuth.dto.UserRegisterDTO;
import net.l3mon.LogisticsL3mon.UserAuth.entity.Role;
import net.l3mon.LogisticsL3mon.UserAuth.entity.User;
import net.l3mon.LogisticsL3mon.UserAuth.exceptions.UserExistingWithMail;
import net.l3mon.LogisticsL3mon.UserAuth.exceptions.UserExistingWithName;
import net.l3mon.LogisticsL3mon.UserAuth.exceptions.UserNullConpanyIdException;
import net.l3mon.LogisticsL3mon.company.dto.CompanyDTO;
import net.l3mon.LogisticsL3mon.company.entity.Company;
import net.l3mon.LogisticsL3mon.company.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private Company saveCompany(Company company){
        return companyRepository.saveAndFlush(company);
    }

    public void create(CompanyDTO companyDTO) {
//        userRepository.findUserByLogin(userRegisterDTO.getLogin()).ifPresent(value->{
//            throw new UserExistingWithName("Użytkownik o nazwie juz istnieje");
//        });
//        userRepository.findUserByEmail(userRegisterDTO.getEmail()).ifPresent(value->{
//            throw new UserExistingWithMail("Użytkownik o mailu juz istnieje");
//        });
//
//        if (userRegisterDTO.getCompanyId() == null || userRegisterDTO.getCompanyId() < 0){
//            throw new UserNullConpanyIdException("Nie przypisano użytkownika do firmy");
//        }
        // Pobieranie obiektu Company na podstawie przekazanego companyId
//        Company company = companyRepository.findById(Long.valueOf(userRegisterDTO.getCompanyId()))
//                .orElseThrow(() -> new EntityNotFoundException("Company with id " + userRegisterDTO.getCompanyId() + " not found"));

        Company company = new Company();
        company.setName(companyDTO.getName());
        company.setShortName(companyDTO.getShortName());
        company.setEnable(true);
        company.setCreatedAt("created time");

        saveCompany(company);
    }
}
