package com.tender.validator;

import com.tender.constants.TenderConstants;
import com.tender.entity.User;
import com.tender.exception.InvalidRequestException;

import java.util.Objects;

public class RequestValidator {

    public static void validateAddUserRequest(User user) {
        if(Objects.isNull(user)) {
            throw new InvalidRequestException(TenderConstants.EMPTY_USER_EXCEPTION_MSG);
        }
    }


}
