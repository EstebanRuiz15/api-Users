package com.emazon.api_users.infraestructure.driving_http.mappers;

import org.mapstruct.Mapper;

import com.emazon.api_users.domain.model.User;
import com.emazon.api_users.infraestructure.driving_http.dtos.UserDtoAddAuxBod;

@Mapper(componentModel = "spring")
public interface UserMapperDtoUser {
    User toUser(UserDtoAddAuxBod userDtoAdd);
}
