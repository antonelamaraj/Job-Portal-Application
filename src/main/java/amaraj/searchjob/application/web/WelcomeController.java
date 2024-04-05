package amaraj.searchjob.application.web;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.SessionAttributes;
//
//@Controller
//@SessionAttributes({"username","id"})
//public class WelcomeController {
//
//    @GetMapping("/")
//    public String goToWelcomePage(ModelMap model){
//        model.put("username", getLoggedInUsernam());
//        return"welcome";
//
//    }
//
//
//    private String getLoggedInUsernam(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication.getName();
//    }
//}
