package com.game.b1ingservice.validator;

import com.game.b1ingservice.payload.commons.ApiResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommonValidator implements Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {

    }

    public static boolean isEmail(String email) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static boolean isNumber(String number) {
        String regex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public static boolean isLengthMax(String str, int size) {
        if (ObjectUtils.isNotEmpty(str)) {
            if (str.length() <= size) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLengthMin(String str, int size) {
        if (ObjectUtils.isNotEmpty(str)) {
            if (str.length() >= size) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLengthEqual(String str, int size) {
        if (ObjectUtils.isNotEmpty(str)) {
            if (str.length() == size) {
                return true;
            }
        }
        return false;
    }


}

