package todo.app.validation;


import org.apache.commons.lang3.StringUtils;

public class Validation {
    public String formValidate(String stringInput) {
        if (StringUtils.isAllBlank(stringInput) || StringUtils.isEmpty(stringInput)) {
            return "EMPTY_ERROR";
        } else if (stringInput.trim().length() > 50) {
            return "LENGTH_ERROR";
        } else {
            return stringInput.trim();
        }
    }
}
