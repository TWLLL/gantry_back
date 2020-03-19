package com.qljt.gantry.common.utils.support;

/**
 * @author : LY
 * @created with IntelliJ IDEA.
 * @date : 2019/4/17
 * @time : 10:33
 * @description :
 */


import com.qljt.gantry.common.Exception.RRException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorUtils {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static void validateEntity(Object object, Class<?>[] groups)
            throws RRException {
        Set constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation constraint = (ConstraintViolation) constraintViolations.iterator().next();
            throw new RRException(constraint.getMessage());
        }
    }
}
