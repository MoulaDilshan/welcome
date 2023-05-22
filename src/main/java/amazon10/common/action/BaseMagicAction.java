package amazon10.common.action;

import net.ryian.core.action.BaseAction;
import amazon10.sys.service.ShiroDBRealm;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;

/**
 * Action基类，系统中所有基类均继承自该类
 * 
 * @author cheng.wang
 */
@SuppressWarnings("serial")
public abstract class BaseMagicAction extends BaseAction{

	protected Logger logger = Logger.getLogger(this.getClass());

	protected ShiroDBRealm.ShiroUser getCurrentUser() {
		return (ShiroDBRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();

	}

}
