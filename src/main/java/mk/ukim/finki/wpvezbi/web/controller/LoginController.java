package mk.ukim.finki.wpvezbi.web.controller;

import mk.ukim.finki.wpvezbi.model.User;
import mk.ukim.finki.wpvezbi.model.exceptions.InvalidUserCredentialsException;
import mk.ukim.finki.wpvezbi.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    //@RequestMapping(method = RequestMethod.GET,value = "")
    public String getLoginPage(){
        return "login";
    }

    @PostMapping
    public String login(HttpServletRequest request, Model model){
        User user=null;
        try{
            user=this.authService.login(request.getParameter("username"),request.getParameter("password"));
            request.getSession().setAttribute("user",user);
            return "redirect:/home";
        }catch (InvalidUserCredentialsException e){
            model.addAttribute("hasError",true);
            model.addAttribute("error",e.getMessage());
            return "login";
        }
    }
}
