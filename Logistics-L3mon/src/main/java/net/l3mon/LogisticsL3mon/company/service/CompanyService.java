package net.l3mon.LogisticsL3mon.company.service;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.Server.ResponseMessage;
import net.l3mon.LogisticsL3mon.UserAuth.dto.UserToListDTO;
import net.l3mon.LogisticsL3mon.UserAuth.entity.User;
import net.l3mon.LogisticsL3mon.UserAuth.repository.UserRepository;
import net.l3mon.LogisticsL3mon.company.dto.CompanyDTO;
import net.l3mon.LogisticsL3mon.company.entity.Company;
import net.l3mon.LogisticsL3mon.company.entity.CompanyInviteLink;
import net.l3mon.LogisticsL3mon.company.entity.CompanyUser;
import net.l3mon.LogisticsL3mon.company.entity.CompanyUserWaitingToJoin;
import net.l3mon.LogisticsL3mon.company.repository.CompanyInviteLinkRepository;
import net.l3mon.LogisticsL3mon.company.repository.CompanyRepository;
import net.l3mon.LogisticsL3mon.company.repository.CompanyUserRepository;
import net.l3mon.LogisticsL3mon.company.repository.CompanyUserWaitingToJoinRepository;
import net.l3mon.LogisticsL3mon.room.entity.Room;
import net.l3mon.LogisticsL3mon.room.repository.RoomRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final RoomRepository roomRepository;
    private final CompanyInviteLinkRepository companyInviteLinkRepository;
    private final CompanyUserRepository companyUserRepository;
    private final UserRepository userRepository;
    private final CompanyUserWaitingToJoinRepository companyUserWaitingToJoinRepository;

    //////////////////////////////////////////////////////////////////////////////////
    private boolean isNullOrEmpty(String str) {
    return str == null || str.isEmpty();
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
    //////////////////////////////////////////////////////////////////////////////////

    public Company create(CompanyDTO companyDTO) throws GlobalExceptionMessage {

        if (isNullOrEmpty(companyDTO.getName())) {
            throw new GlobalExceptionMessage("Nazwa firmy nie może być pusta");
        }
        if (isNullOrEmpty(companyDTO.getShortName())) {
            throw new GlobalExceptionMessage("Skrócona nazwa firmy nie może być pusta");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
//        String username = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        String username = authentication.getName();
        System.out.println(username);

        User user = userRepository.findUserByLogin(username).orElse(null);
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        Company savedCompany;
        try {
            Company company = new Company();
            company.setName(companyDTO.getName());
            company.setShortName(companyDTO.getShortName());
            company.setEnable(true);
            company.setCreatedAt(String.valueOf(LocalDateTime.now()));

            savedCompany = companyRepository.save(company);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Nie udało się utworzyć firmy: " + ex.getMessage());
        }
        Room room;
        try {
            room = new Room();
            room.setCompanyId(savedCompany.getId());
            room.setName("Town Square");
            room.setPermissionForAll(true);
            room.setCreatedAt(String.valueOf(LocalDateTime.now()));

            roomRepository.save(room);
        } catch (GlobalExceptionMessage ex) {
            companyRepository.delete(savedCompany);
            throw new GlobalExceptionMessage("Nie udało się utworzyć firmy: " + ex.getMessage());
        }
        CompanyInviteLink companyInviteLink;
        try {
            companyInviteLink = new CompanyInviteLink();
            companyInviteLink.setCompanyId(savedCompany.getId());
            companyInviteLink.setCode(generateUniqueCode());
            companyInviteLink.setRequiresAcceptance(true);
            companyInviteLink.setCreatedAt(String.valueOf(LocalDateTime.now()));

            companyInviteLinkRepository.save(companyInviteLink);
        } catch (GlobalExceptionMessage ex) {
            companyRepository.delete(savedCompany);
            roomRepository.delete(room);
            throw new GlobalExceptionMessage("Nie udało się utworzyć firmy: " + ex.getMessage());
        }
        CompanyUser companyUser;
        try {
            companyUser = new CompanyUser();
            companyUser.setCompanyId(savedCompany.getId());
            companyUser.setUserId(user.getId());
            companyUser.setRole("ADMIN");
            companyUser.setCreatedAt(String.valueOf(LocalDateTime.now()));

            companyUserRepository.save(companyUser);

        } catch (GlobalExceptionMessage ex){
            companyRepository.delete(savedCompany);
            roomRepository.delete(room);
            companyInviteLinkRepository.delete(companyInviteLink);
            throw new GlobalExceptionMessage("Nie udało się utworzyć firmy: " + ex.getMessage());
        }

        return savedCompany;
    }

    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }

    public List<?> getAllUserCompany() throws GlobalExceptionMessage {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findUserByLogin(username).orElse(null);
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        List<Company> allUserCompany = new ArrayList<>();

        List<CompanyUser> companyUsers = companyUserRepository.findByUserId(user.getId());
        if (companyUsers == null) {
            return allUserCompany;
        }

        for (CompanyUser companyUser : companyUsers) {
            Long companyId = companyUser.getCompanyId();
            Company company = companyRepository.findById(companyId).orElse(null);
            if (company == null) {
                throw new GlobalExceptionMessage("Błąd serwera");
            }
            allUserCompany.add(company);
        }

        System.out.println(allUserCompany);

        return allUserCompany;
    }

    public CompanyInviteLink getCompanyInviteCode(Long companyId) throws GlobalExceptionMessage{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findUserByLogin(username).orElse(null);
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        CompanyUser companyUser = companyUserRepository.findByUserIdAndCompanyId(user.getId(), companyId).orElse(null);
        if (companyUser == null) {
            throw new GlobalExceptionMessage("Permission fail");
        }

        CompanyInviteLink companyInviteLink = companyInviteLinkRepository.findByCompanyId(companyId).orElse(null);
        if (companyInviteLink == null) {
            throw new GlobalExceptionMessage("Invite code doesn't exist");
        }

        return companyInviteLink;
    }

    public ResponseMessage joinCompanyByCode(String code) throws GlobalExceptionMessage{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findUserByLogin(username).orElse(null);
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        CompanyInviteLink companyInviteLink = companyInviteLinkRepository.findByCode(code).orElse(null);
        if (companyInviteLink == null) {
            throw new GlobalExceptionMessage("Code doesn't exist");
        }

        CompanyUser companyUser = companyUserRepository.findByUserIdAndCompanyId(user.getId(), companyInviteLink.getCompanyId()).orElse(null);
        if (companyUser != null) {
            throw new GlobalExceptionMessage("The user has already been added");
        }

        CompanyUserWaitingToJoin companyUserWaitingToJoin = companyUserWaitingToJoinRepository.findByUserIdAndCompanyId(user.getId(), companyInviteLink.getCompanyId()).orElse(null);

        if (companyInviteLink.isRequiresAcceptance()){
            if (companyUserWaitingToJoin != null) {
//                throw new GlobalExceptionMessage("The user waits for confirmation from the administrator");
                return new ResponseMessage("The user waits for confirmation from the administrator");
            }
//            if (companyUserWaitingToJoin == null){
                CompanyUserWaitingToJoin companyUserWaitingToJoinCreate;
                try{
                    companyUserWaitingToJoinCreate = new CompanyUserWaitingToJoin();
                    companyUserWaitingToJoinCreate.setCompanyId(companyInviteLink.getCompanyId());
                    companyUserWaitingToJoinCreate.setUserId(user.getId());
                    companyUserWaitingToJoinCreate.setCreatedAt(String.valueOf(LocalDateTime.now()));
                } catch (GlobalExceptionMessage ex){
                    throw new GlobalExceptionMessage("Server error: companyUserWaitingToJoinCreate");
                }
                companyUserWaitingToJoinRepository.save(companyUserWaitingToJoinCreate);
                return new ResponseMessage("Waiting for confirmation from the administrator");
//            }
        } else {
            if (companyUserWaitingToJoin != null) {
                companyUserWaitingToJoinRepository.delete(companyUserWaitingToJoin);
            }

            CompanyUser companyUserCreate = new CompanyUser();
            companyUserCreate.setUserId(user.getId());
            companyUserCreate.setCompanyId(companyInviteLink.getCompanyId());
            companyUserCreate.setRole("MEMBER");
            companyUserCreate.setCreatedAt(String.valueOf(LocalDateTime.now()));

            try {
                companyUserRepository.save(companyUserCreate);
            } catch (GlobalExceptionMessage ex) {
                throw new GlobalExceptionMessage("Error: " + ex.getMessage());
            }
        }

        return new ResponseMessage("User added successfully");
    }

    public List<?> getAllUsersCompanyById(Long companyId) throws GlobalExceptionMessage{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user;
        try {
            user = userRepository.findUserByLogin(username).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("error: " + ex.getMessage());
        }
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        CompanyUser companyUser;
        try {
            companyUser = companyUserRepository.findByUserIdAndCompanyId(user.getId(), companyId).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }
        if (companyUser == null) {
            throw new GlobalExceptionMessage("Don't have permission");
        }

        List<CompanyUser> companyUsers;
        try {
            companyUsers = companyUserRepository.findAllByCompanyId(companyId);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }

        List<UserToListDTO> allUserCompany = new ArrayList<>();

        for (CompanyUser companyUserCurrent : companyUsers) {
            Long userId = companyUserCurrent.getUserId();
            String companyRole = companyUserCurrent.getRole();

            User userCurrent;
            try {
                userCurrent = userRepository.findById(userId).orElse(null);
            } catch (GlobalExceptionMessage ex) {
                throw new GlobalExceptionMessage("Error: " + ex.getMessage());
            }
            if (userCurrent == null) {
                throw new GlobalExceptionMessage("Error");
            }

            UserToListDTO userToListDTO = new UserToListDTO();
            userToListDTO.setId(userCurrent.getId());
            userToListDTO.setUsername(userCurrent.getUsername());
            userToListDTO.setEmail(userCurrent.getEmail());
            userToListDTO.setCompany_role(companyRole);
            userToListDTO.setPhone(userCurrent.getPhone());

            allUserCompany.add(userToListDTO);
        }

        return allUserCompany;
    }

    public List<?> getAllUsersWaitingToJoinCompany(Long companyId) throws GlobalExceptionMessage{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user;
        try {
            user = userRepository.findUserByLogin(username).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("error: " + ex.getMessage());
        }
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        CompanyUser companyUser;
        try {
            companyUser = companyUserRepository.findByUserIdAndCompanyId(user.getId(), companyId).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }
        if (companyUser == null) {
            throw new GlobalExceptionMessage("Don't have permission");
        }

        List<CompanyUserWaitingToJoin> companyUserWaitingToJoins;
        try {
            companyUserWaitingToJoins = companyUserWaitingToJoinRepository.findAllByCompanyId(companyId);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }

        List<UserToListDTO> allUserCompany = new ArrayList<>();

        for (CompanyUserWaitingToJoin CompanyUserWaitingToJoinCurrent : companyUserWaitingToJoins) {
            Long userId = CompanyUserWaitingToJoinCurrent.getUserId();

            User userCurrent;
            try {
                userCurrent = userRepository.findById(userId).orElse(null);
            } catch (GlobalExceptionMessage ex) {
                throw new GlobalExceptionMessage("Error: " + ex.getMessage());
            }
            if (userCurrent == null) {
                throw new GlobalExceptionMessage("Error");
            }

            UserToListDTO userToListDTO = new UserToListDTO();
            userToListDTO.setId(userCurrent.getId());
            userToListDTO.setUsername(userCurrent.getUsername());
            userToListDTO.setEmail(userCurrent.getEmail());
            userToListDTO.setCompany_role("WAITING");
            userToListDTO.setPhone(userCurrent.getPhone());

            allUserCompany.add(userToListDTO);
        }


        return allUserCompany;
    }

    public ResponseMessage acceptUserJoinToCompany(Long companyId, Long userId) throws GlobalExceptionMessage{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user;
        try {
            user = userRepository.findUserByLogin(username).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("error: " + ex.getMessage());
        }
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        CompanyUser companyUser;
        try {
            companyUser = companyUserRepository.findByUserIdAndCompanyId(user.getId(), companyId).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }
        if (companyUser == null) {
            throw new GlobalExceptionMessage("Don't have permission");
        }
        if (!Objects.equals(companyUser.getRole(), "ADMIN")){
            throw new GlobalExceptionMessage("Don't have permission");
        }

        CompanyUser companyUserSelectedUser;
        try {
            companyUserSelectedUser = companyUserRepository.findByUserIdAndCompanyId(userId, companyId).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }
        if (companyUserSelectedUser != null) {
            throw new GlobalExceptionMessage("The user has already been added");
        }

        CompanyUserWaitingToJoin companyUserWaitingToJoins;
        try {
            companyUserWaitingToJoins = companyUserWaitingToJoinRepository.findByCompanyId(companyId).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }
        if (companyUserWaitingToJoins == null) {
            throw new GlobalExceptionMessage("No user on the waiting list");
        }

        CompanyUser companyUserCreate = new CompanyUser();
        companyUserCreate.setUserId(userId);
        companyUserCreate.setCompanyId(companyUserWaitingToJoins.getCompanyId());
        companyUserCreate.setRole("MEMBER");
        companyUserCreate.setCreatedAt(String.valueOf(LocalDateTime.now()));

        try {
            companyUserRepository.save(companyUserCreate);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }
        try {
            companyUserWaitingToJoinRepository.delete(companyUserWaitingToJoins);
        } catch (GlobalExceptionMessage ex) {
            companyUserRepository.delete(companyUserCreate);
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }

        return new ResponseMessage("User added successfully");
    }

    public Object removeUserFromCompany(Long companyId, Long userId) throws GlobalExceptionMessage{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user;
        try {
            user = userRepository.findUserByLogin(username).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("error: " + ex.getMessage());
        }
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        CompanyUser companyUser;
        try {
            companyUser = companyUserRepository.findByUserIdAndCompanyId(user.getId(), companyId).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }
        if (companyUser == null) {
            throw new GlobalExceptionMessage("Don't have permission");
        }
        if (!Objects.equals(companyUser.getRole(), "ADMIN")){
            throw new GlobalExceptionMessage("Don't have permission");
        }

        CompanyUser companyUserToDelete;
        try {
            companyUserToDelete = companyUserRepository.findByUserIdAndCompanyId(userId, companyId).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }
        if (companyUserToDelete == null){
            throw new GlobalExceptionMessage("No user to delete");
        }

        try {
            companyUserRepository.delete(companyUserToDelete);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }


        return new ResponseMessage("User removed successfully");
    }
}
