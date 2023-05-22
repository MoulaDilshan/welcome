package amazon10.sys.action;

import amazon10.common.action.MagicAction;
import amazon10.sys.domain.Dict;
import amazon10.sys.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("serial")
@Controller
@RequestMapping("/sys/dict")
public class DictAction extends MagicAction<Dict,DictService> {


}
