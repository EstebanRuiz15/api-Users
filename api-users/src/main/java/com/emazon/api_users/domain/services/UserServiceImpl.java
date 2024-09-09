package com.emazon.api_users.domain.services;

import java.time.LocalDate;
import java.time.Period;
import com.emazon.api_users.domain.exceptions.ErrorExceptionParam;
import com.emazon.api_users.domain.interfaces.IUserRepositoryPort;
import com.emazon.api_users.domain.interfaces.IUserService;
import com.emazon.api_users.domain.model.RoleEnum;
import com.emazon.api_users.domain.model.User;
import com.emazon.api_users.domain.util.ConstantsDomain;

public class UserServiceImpl  implements IUserService{
    private final IUserRepositoryPort repositoryPort;

    public UserServiceImpl(IUserRepositoryPort repositoryPort){
        this.repositoryPort=repositoryPort;
    }

    @Override
    public void createUserAuxBod(User user) {

        if(repositoryPort.findByEmail(user.getEmail()).isPresent()){
            throw new ErrorExceptionParam(ConstantsDomain.EMAIL_AL_READY_EXIST_ERROR_MESSAGE);
        }
    
    if(Period.between(user.getDateOfBirth(), LocalDate.now()).getYears() < ConstantsDomain.AGE_MAY){
    throw new ErrorExceptionParam(ConstantsDomain.AGE_MAY_ERROR);
    }

    if (!isValidRole(user.getRole())) {
        throw new ErrorExceptionParam(ConstantsDomain.ROLE_ERROR_MESSAGE);
    }

    repositoryPort.saveUser(user);
}

 private boolean isValidRole(RoleEnum role) {
        // Validar si el rol está entre los permitidos
        return role == RoleEnum.ADMIN || role == RoleEnum.USER || role == RoleEnum.AUX_BODEGA;
    }

}
