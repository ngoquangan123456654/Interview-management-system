package fa.training.fjb04.ims.util.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExceptionController {
    @GetMapping("/no-permission")
    public String getError403() {
        return "error/403";
    }
}
