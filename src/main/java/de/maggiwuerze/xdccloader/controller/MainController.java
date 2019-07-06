package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.model.User;
import de.maggiwuerze.xdccloader.model.forms.UserForm;
import de.maggiwuerze.xdccloader.persistency.ChannelRepository;
import de.maggiwuerze.xdccloader.persistency.DownloadRepository;
import de.maggiwuerze.xdccloader.persistency.ServerRepository;
import de.maggiwuerze.xdccloader.persistency.UserRepository;
import de.maggiwuerze.xdccloader.util.Role;
import de.maggiwuerze.xdccloader.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
class MainController{


    Logger logger = Logger.getLogger("Class MainController");

    @Autowired
    UserRepository userRepository;
    @Autowired
    DownloadRepository downloadRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ServerRepository serverRepository;

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


                String username = userForm.getUsername();
                String password = new BCryptPasswordEncoder(11).encode(userForm.getPassword());
                this.userRepository.save(new User(username, password, Role.USER.getExternalString()));
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