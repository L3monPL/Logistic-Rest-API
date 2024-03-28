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
import net.l3mon.LogisticsL3mon.room.entity.Room;
import net.l3mon.LogisticsL3mon.room.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final RoomRepository roomRepository;

    private Company saveCompany(Company company){
        return companyRepository.saveAndFlush(company);
    }

    public Company create(CompanyDTO companyDTO) throws Exception {

        Company savedCompany;
        try {
            Company company = new Company();
            company.setName(companyDTO.getName());
            company.setShortName(companyDTO.getShortName());
            company.setEnable(true);
            company.setCreatedAt(String.valueOf(LocalDateTime.now()));

            savedCompany = companyRepository.save(company);
        } catch (Exception ex) {
            throw new Exception("Nie udało się utworzyć firmy: " + ex.getMessage());
        }
        try {
            Room room = new Room();
            room.setCompanyId(savedCompany.getId());
            room.setName("Town Square");
            room.setPermissionForAll(true);
            room.setCreatedAt(String.valueOf(LocalDateTime.now()));

            roomRepository.save(room);
        } catch (Exception ex) {
            companyRepository.delete(savedCompany);
            throw new Exception("Nie udało się utworzyć firmy: " + ex.getMessage());
        }

        return savedCompany;
    }

    public List<Company> getAllCompanies(){
        System.out.println(generateUniqueCode());
        return companyRepository.findAll();
    }

    private String generateUniqueCode() {
        StringBuilder codeBuilder = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < 24; i++) {
            if (i > 0 && i % 6 == 0) {
                codeBuilder.append("-");
            }
            codeBuilder.append((char)('a' + random.nextInt(26)));
        }
        return codeBuilder.toString();
    }
}
