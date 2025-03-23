package ch.hftm.blogproject.util;

import ch.hftm.blogproject.model.exception.ExceptionDTO;

public class ExceptionMapper {

    public ExceptionDTO toExceptionDTO(Throwable exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO();
        exceptionDTO.setException(exception.getClass().getSimpleName());
        exceptionDTO.setMessage(exception.getMessage());
        return exceptionDTO;
    }
}
