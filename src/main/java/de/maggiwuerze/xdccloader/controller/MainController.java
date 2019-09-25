package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import de.maggiwuerze.xdccloader.model.forms.UserForm;
import de.maggiwuerze.xdccloader.persistency.*;
import de.maggiwuerze.xdccloader.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
class MainController{


    Logger logger = Logger.getLogger("Class MainController");

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserSettingsRepository userSettingsRepository;

    @Autowired
    private SimpMessagingTemplate websocket;
    @Autowired
    private EntityLinks entityLinks;

    final String MESSAGE_PREFIX = "/topic";

    @RequestMapping("/")
    public String index(){

        return "index";

    }

    @RequestMapping("/register")
    public String register(Model model){

        model.addAttribute("user", new UserForm());
        model.addAttribute("noLogin", userRepository.count() <= 0);
        return "register";

    }

    @RequestMapping("/login")
    public String login(Model model){

        if(userRepository.count() <= 0){

            model.addAttribute("user", new UserForm());
            model.addAttribute("noLogin", true);
            return "register";

        }

        return "login";

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUserAccount(
            @ModelAttribute("user") @Valid UserForm userForm,
            BindingResult result, HttpServletRequest request, Errors errors, ModelAndView modelAndView) {

            Map<String, Object> model = new HashMap<>();
            model.put("user", userForm);

            if(errors.hasErrors()){

                model.put("noUser", true);
                return new ModelAndView("register", model);

            }else if(userRepository.findUserByName(userForm.getUsername()).isPresent()){

                result.rejectValue("username", "username already exists");
                model.put("noUser", true);
                return new ModelAndView("register", model);

            }else {


//                channelRepository.save(new Channel( "TestChannel"));

                String username = userForm.getUsername();
                String password = new BCryptPasswordEncoder(11).encode(userForm.getPassword());
                UserSettings userSettings = userSettingsRepository.save(new UserSettings());
                this.userRepository.save(new User(username, password, UserRole.USER.getExternalString(), true, userSettings));
                model.put("noUser", false);

                try {
                    request.login(username, userForm.getPassword());

                } catch (ServletException e) {
                    logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                }

                return new ModelAndView(index());
            }


    }

    private String getPath(Download download){
        return this.entityLinks.linkForSingleResource(download.getClass(),
                download.getId()).toUri().getPath();
    }

}