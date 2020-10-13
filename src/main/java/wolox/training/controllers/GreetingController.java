package wolox.training.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    /**
     * Method that gets a greeting by name
     * @param name: Name to greet (String)
     * @param model: Object to model the view ({@link Model})
     * @return greet (String)
     */
    @GetMapping("/greeting")
    public String greeting(
        @RequestParam(name = "name", required = false, defaultValue = "World") String name,
        Model model) {

        model.addAttribute("name", name);

        return "greeting";

    }

}
