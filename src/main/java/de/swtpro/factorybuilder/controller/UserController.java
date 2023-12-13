package de.swtpro.factorybuilder.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.swtpro.factorybuilder.entity.User;
import de.swtpro.factorybuilder.service.user.UserFormular;
import de.swtpro.factorybuilder.service.user.UserServiceImpl;
import jakarta.validation.Valid;

@Controller
@SessionAttributes(names={"benutzerFormular"})
public class UserController {
    
    @Autowired UserServiceImpl userServiceImpl;

    @ModelAttribute("/signup")
     public void initUserFormular(Model m){

        if(!m.containsAttribute("benutzerFormular")){
            m.addAttribute("benutzerFormular", new UserFormular());
        }
    }

    @GetMapping("/signup")
    public String getRequeString(Locale locale, Model m){

        UserFormular currentUserFormular = new UserFormular();

        m.addAttribute("userFormular", currentUserFormular);
        m.addAttribute("user", new User());

        m.addAttribute("language", locale.getDisplayLanguage());
        return "usersignup";

    }

    @PostMapping("/signup")
    public String postAdress(Model m, @Valid @ModelAttribute("userFormular") UserFormular uf, BindingResult userFormularError, RedirectAttributes redirectAttributes){
        
        if(userFormularError.hasErrors()){
            // Logger.error("postAdress{}", userFormularError.toString());
        }else{
            User currentUser = new User();
            uf.toUser(currentUser);
            m.addAttribute("user", currentUser);
            boolean nameTaken = userServiceImpl.checkUsername(currentUser.getUsername());

            //check, ob Name bereits vergeben ist
            if (nameTaken) {
                redirectAttributes.addFlashAttribute("error", "Username is taken.");
                return "redirect:/signup";
            }

            //check, ob Losung zweimal korrekt eingegeben wurde
            if (!uf.getPassword().equals(uf.getPasswordCheck())) {
                redirectAttributes.addFlashAttribute("passwordMismatchError", "The passwords are not the same.");
                return "redirect:/signup";
            }


            currentUser = userServiceImpl.createUser(currentUser);
            
            return "redirect:/home";
        }
        m.addAttribute("benutzerFormularError", userFormularError);
        return "usersignup";
    }
}





