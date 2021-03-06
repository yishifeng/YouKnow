package com.ysf.web;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ysf.entity.login.Sys_login;
import com.ysf.entity.role.Sys_roleTree;
import com.ysf.service.login.Sys_loginService;
import com.ysf.service.role.Sys_roleService;
import com.ysf.standard.Util;
import com.ysf.util.MD5;
import com.ysf.utils.ValidataUtil;
import com.ysf.utils.VerifyCodeUtil;

@Controller
@RequestMapping("/login")
public class UserLogin {

    @Resource
    Sys_loginService userloginService;

    @Resource
    Sys_roleService roleService;

    @Autowired
    private Util  ut;

    @RequestMapping("/logout")
    public ModelAndView logout() {
        ModelAndView mav = new ModelAndView("redirect:/login.jsp");

        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout(); // session ???????????????SessionListener??????session???????????????????????????
        }
        return mav;
    }

    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String,String> login (HttpServletRequest request,String username,String password,String verifyCode){
        Map<String,String> result = new HashMap<String, String>();
        HttpSession session = request.getSession();
        String SessionverifyCode = (String)session.getAttribute("verifyCode");
        if (SessionverifyCode!=null&&SessionverifyCode.equals(verifyCode)){
            session.setAttribute("verifyCode", MD5.md5(Math.random()+""));
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            Subject subject = SecurityUtils.getSubject();
            String msg = "";
            String success = "";
            try{
                subject.login(token);  //????????????
                request.getSession().setAttribute("username", username);
                result.put("success", "OK");
                request.setAttribute("success", "OK");
            }catch(IncorrectCredentialsException e) {
                msg = "??????????????????. Password for account " + token.getPrincipal() + " was incorrect.";
            } catch (ExcessiveAttemptsException e) {
                msg = "????????????????????????";
            } catch (LockedAccountException e) {
                msg = "??????????????????. The account for username " + token.getPrincipal() + " was locked.";
            } catch (DisabledAccountException e) {
                msg = "??????????????????. The account for username " + token.getPrincipal() + " was disabled.";
            } catch (ExpiredCredentialsException e) {
                msg = "???????????????. the account for username " + token.getPrincipal() + "  was expired.";
            } catch (UnknownAccountException e) {
                msg = "???????????????. There is no user with username of " + token.getPrincipal();
            } catch (UnauthorizedException e) {
                msg = "?????????????????????????????????" + e.getMessage();
            }
            result.put("msg", msg);
        }

        return result;
    }


    @RequestMapping("/getVerifyCodeImage.do")
    public void getVerifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //?????????????????????
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_NUM_ONLY, 4, null);
        //??????????????????HttpSession??????
        request.getSession().setAttribute("verifyCode", verifyCode);
        System.out.println("???????????????????????????[" + verifyCode + "],????????????HttpSession???");
        //?????????????????????????????????JPEG??????
        response.setContentType("image/jpeg");
        BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode, 90, 30, 3, true, Color.WHITE, Color.BLACK, null);
        //???????????????
        ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
    }

    @RequestMapping(value="/register.do")
    public ModelAndView register(HttpServletRequest request,String username , String password ,String repassword , String email , String wxname) {
        ModelAndView mav = new ModelAndView("redirect:/login.jsp");

        if (username!=null&&!"".equals(username.trim())&&username.length()<20&&password!=null&&!"".equals(password.trim())&&(password.equals(repassword))){

            Long count = userloginService.selectByUsernameCount(username).get("count");
            if (0==count){
                Sys_login entity = new Sys_login();
                entity.setUsername(username);
                entity.setPassword(MD5.md5(password));
                if (email!=null&&!"".equals(email.trim())&&ValidataUtil.isEmail(email)){
                    entity.setEmail(email);
                }

                if (wxname!=null&&!"".equals(wxname.trim())){
                    entity.setWxname(wxname);
                }

                if(userloginService.insertSelective(entity)>0){
                    mav.addObject("msg", "????????????");
                }
            }else{
                mav.addObject("msg", "????????????");
            }
        }

        return mav;
    }

    /***
     * ????????????
     * @param request
     * @param username
     * @param password
     * @param verifyCode
     * @param model
     * @return
     */
    @RequestMapping(value="/verification.do")
    public ModelAndView login(HttpServletRequest request,String username , String password ,@RequestParam(defaultValue="0000") String verifyCode, Model model) {
        ModelAndView mav = new ModelAndView();
        String msg = "";
        HttpSession session = request.getSession();
        String SessionverifyCode = "0000";//(String)session.getAttribute("verifyCode");
        if (SessionverifyCode!=null&&SessionverifyCode.equals(verifyCode)){
            session.setAttribute("verifyCode", MD5.md5(Math.random()+""));
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            //token.setRememberMe(true);
            Subject subject = SecurityUtils.getSubject();
            try{
                subject.login(token);
                Sys_login loginEntity = userloginService.selectByUsername(username);
                session.setAttribute("UserName", username);
                session.setAttribute("loginEntity", loginEntity);
                mav.setViewName("redirect:/index");
            }catch(IncorrectCredentialsException e) {
                msg = "??????????????????. Password for account " + token.getPrincipal() + " was incorrect.";
                model.addAttribute("message", msg);
                System.out.println(msg);
                mav.setViewName("redirect:/login.jsp");
            } catch (ExcessiveAttemptsException e) {
                msg = "????????????????????????";
                model.addAttribute("message", msg);
                System.out.println(msg);
                mav.setViewName("redirect:/login.jsp");
            } catch (LockedAccountException e) {
                msg = "??????????????????. The account for username " + token.getPrincipal() + " was locked.";
                model.addAttribute("message", msg);
                System.out.println(msg);
                mav.setViewName("redirect:/login.jsp");
            } catch (DisabledAccountException e) {
                msg = "??????????????????. The account for username " + token.getPrincipal() + " was disabled.";
                model.addAttribute("message", msg);
                System.out.println(msg);
                mav.setViewName("redirect:/login.jsp");
            } catch (ExpiredCredentialsException e) {
                msg = "???????????????. the account for username " + token.getPrincipal() + "  was expired.";
                model.addAttribute("message", msg);
                System.out.println(msg);
                mav.setViewName("redirect:/login.jsp");
            } catch (UnknownAccountException e) {
                msg = "???????????????. There is no user with username of " + token.getPrincipal();
                model.addAttribute("message", msg);
                System.out.println(msg);
                mav.setViewName("redirect:/login.jsp");
            } catch (UnauthorizedException e) {
                msg = "?????????????????????????????????" + e.getMessage();
                model.addAttribute("message", msg);
                System.out.println(msg);
                mav.setViewName("redirect:/login.jsp");
            }
        }else{
            mav.addObject("msg", "??????????????????");
            mav.setViewName("redirect:/login.jsp");
        }
        return mav;
    }

    @RequestMapping("/index")
    public ModelAndView getMainView(HttpServletRequest request) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("index_easyui");
        return mav;
    }

    @RequestMapping("/registerView")
    public ModelAndView getRegisterView(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/zcyh.jsp");
        return mav;
    }

    @RequestMapping("/save")
//	@RequiresRoles(value = { "admin" },logical=Logical.OR)
    public ModelAndView save(HttpServletRequest request,String username,String password,@RequestParam(defaultValue="email") String email,@RequestParam(defaultValue="wxname") String wxname) {

        Long count = userloginService.selectByUsernameCount(username).get("count");
        if (0==count){
            Sys_login entity = new Sys_login();
            entity.setUsername(username);
            entity.setPassword(MD5.md5(password));
            if (email!=null&&!"".equals(email.trim())&&ValidataUtil.isEmail(email)){
                entity.setEmail(email);
            }

            if (wxname!=null&&!"".equals(wxname.trim())){
                entity.setWxname(wxname);
            }

            userloginService.insertSelective(entity);
        }

        return view(0,10);
    }

    @RequestMapping(value="/user",method=RequestMethod.GET)
//	@RequiresRoles(value = { "admin" },logical=Logical.OR)
    public ModelAndView view (@RequestParam(name="page",defaultValue="0") Integer page,@RequestParam(name="row",defaultValue="10")Integer row){
        ModelAndView mav = new ModelAndView("sys/user");
        List<Sys_roleTree> roles = roleService.selectTreeAll();
        Map<String,Object> parm = new HashMap<>();
        if (page > 0){
            parm.put("page", (page-1)*row);
        }else{
            parm.put("page", page);
        }
        parm.put("row", row);
        List<Map<String,Object>> mainList = userloginService.selectLoginRole(parm);
        Long count = userloginService.selectCount().get("count");
        Long temp= count%row;
        Long countPage = 0L;
        if (temp==0){
            countPage = count/row;
        }else{
            countPage = count/row+1;
        }
        String currentPage = null;
        if (page >0){
            currentPage = ut.page(page, Integer.valueOf(countPage+""));
        }else{
            currentPage = ut.page(page+1, Integer.valueOf(countPage+""));
        }
        mav.addObject("mainList", mainList);
        mav.addObject("currentPage", currentPage);
        mav.addObject("roles", roles);
        return mav;
    }

    @RequestMapping(value="/updateRole",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> updateRole (Integer roleId,Integer loginId,String username){
        Map<String,Boolean> resultMap = new HashMap<>();

        Map<String,Object> parm = new HashMap<>();
        parm.put("roleId", roleId);
        parm.put("loginId", loginId);
        parm.put("username", username);
        if (userloginService.updateSys_login_roleSelective(parm)==1){
            resultMap.put("success", true);
        }else{
            resultMap.put("success", false);
        };

        return resultMap;
    }

    @RequestMapping(value="/del/{loginId}",method=RequestMethod.DELETE)
    @ResponseBody
    public Map<String,Boolean> delUser (@PathVariable Integer loginId){
        Map<String,Boolean> resultMap = new HashMap<>();

        if (userloginService.deleteByPrimaryKey(loginId)==1){
            resultMap.put("success", true);
        }else{
            resultMap.put("success", false);
        }

        return resultMap;
    }
}
