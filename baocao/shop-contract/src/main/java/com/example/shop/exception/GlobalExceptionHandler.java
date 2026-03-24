package com.example.shop.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice public class GlobalExceptionHandler {

    @ExceptionHandler(ContractViolationException.class) public String handleContract(ContractViolationException ex,
                                                                                     HttpServletRequest req,
                                                                                     Model model) {
        model.addAttribute("message", ex.getMessage());
        String referer = req.getHeader("Referer");
        model.addAttribute("backUrl", referer != null ? referer : "/");
        return "contract_error";
    }
}
