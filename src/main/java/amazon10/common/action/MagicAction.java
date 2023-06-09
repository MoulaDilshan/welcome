package amazon10.common.action;

import net.ryian.commons.BeanUtils;
import net.ryian.commons.GenericsUtils;
import net.ryian.core.domain.BaseEntity;
import net.ryian.core.service.BaseService;
import net.ryian.core.service.support.paging.PageInfo;
import net.ryian.core.service.support.query.Condition;
import net.ryian.core.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 增删改查基类.
 * <p/>
 * 提供了访问路径校验、异常处理机制.
 * <p/>
 * 同时也提供了请求数据组装(组装到Map/EntityBean)、结果数据和消息打印的公共方法.
 *
 * @author: wangcheng2@zjhcsosft.com
 * @date: 2014年9月10日 上午10:27:52
 */
@SuppressWarnings("serial")
public abstract class MagicAction<T extends BaseEntity, K extends BaseService> extends BaseMagicAction {
    protected Logger logger = Logger.getLogger(getClass());

    protected Class<T> entityClass;

    public MagicAction() {
        entityClass = GenericsUtils.getSuperClassGenricType(getClass());
    }

    @Autowired
    protected K entityService;

    protected <T extends BaseEntity> T bindEntity(HttpServletRequest request, Class<T> clazz)
            throws Exception {
        T entity = clazz.newInstance();
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String propertyName = (String) enumeration.nextElement();
            String propertyValue = request.getParameter(propertyName)
                    .trim();
            propertyValue = propertyValue.replace("\'", "\"");
            BeanUtils.setBeanPropertyByName(entity, propertyName, propertyValue);
        }
        return entity;
    }

    /**
     * 分页查询Dictionary列表.
     */
    @RequestMapping(value = "queryPaged")
    public void queryPaged(HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        try {
            Condition condition = bindCondition(request);
            PageInfo<T> page = entityService.queryPaged(condition);
            printJson(response, JsonUtils.bean2Json(page));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存单条Dictionary记录.
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public void save(HttpServletRequest request, HttpServletResponse response) {
        try {
            T o = bindEntity(request, entityClass);
            entityService.saveOrUpdate(o);
            printJson(response, messageSuccuseWrap());
        } catch (Exception e) {
            logger.error("save", e);
            printJson(response, messageFailureWrap("保存失败！"));
        }
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public void delete(HttpServletRequest request,
                       HttpServletResponse response) {
        try {
            String ids = request.getParameter("ids");
            for (String id : ids.split(",")) {
                entityService.logicRemove(Long.parseLong(id));
            }
            printJson(response, messageSuccuseWrap());
        } catch (Exception e) {
            logger.error("delete", e);
            printJson(response, messageFailureWrap("删除失败！"));
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return getNameSpace() + "index";
    }


    @RequestMapping(value = "add")
    public String add(HttpServletRequest request,Model model) {
        beforeAdd(request,model);
        return getNameSpace() + "add";
    }

    protected void beforeAdd(HttpServletRequest request,Model model) {

    }

    @RequestMapping(value = "edit/{id}")
    public String edit(HttpServletRequest request,@PathVariable("id") Long id, Model model)
            throws Exception {
        if (id != null) {
            BaseEntity entity = entityService.get(id);
            model.addAttribute("model", entity);
            beforeEdit(request,model,entity);
        }
        return getNameSpace() + "edit";
    }

    protected void beforeEdit(HttpServletRequest request,Model model, BaseEntity entity) {

    }

    private String getNameSpace() {
        String ns = null;
        RequestMapping r = getClass().getAnnotation(RequestMapping.class);
        ns = r.value()[0];
        if (!ns.endsWith("/"))
            ns += "/";
        return ns;
    }

}
