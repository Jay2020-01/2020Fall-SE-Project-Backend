package com.backend.server.controller;

import com.backend.server.entity.pojo.*;
import com.backend.server.service.AuthorService;
import com.backend.server.service.PaperService;
import com.backend.server.service.PortalService;
import com.backend.server.utils.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/portal")
@RestController
public class PortalController {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private PortalService portalService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/test")
    public Result test() {
        return Result.create(200, "success");
    }

    /**
     * 查看作者信息
     * @param authorId
     * @return
     */
    @PostMapping("/personal_center/academic_homepage/view")
    public Result checkPortal(String authorId) {
        Map authors = authorService.queryAuthor(authorId);
        List<Map> papers = paperService.queryPaperByAuthorId((String) authors.get("id"));
        List<Map> paperReturn = new ArrayList<>();
        for (int i = 0; i < 5 && i < papers.size(); ++i) {
            Map paper = papers.get(i);
            paperReturn.add(paper);
        }
        PortalReturn portalReturn = new PortalReturn(authors, paperReturn);
        return Result.create(200, "返回成功", portalReturn);
    }


    /**
     * 查看门户
     * @param id
     * @return
     */
    @PostMapping("/profile/view")
    public Result viewPortal(String id) {
        Map authors = authorService.queryAuthor(id);
        List<Map> papers = paperService.queryPaperByAuthorId((String) authors.get("id"));
        PortalReturn portalReturn = new PortalReturn(authors, papers);
        return Result.create(200, "success", portalReturn);
    }

    /**
     * 修改门户信息
     * @param author
     * @return
     */
    @PostMapping("/profile/modify")
    public Result modifyPortal(@RequestBody Change author) {
        authorService.updateById(author);
        return Result.create(200, "success");
    }


    /**
     * 验证码检验
     * @param certification
     * @return
     */

    @PostMapping("/personal_center/academic_homepage/check")
    public Result certificationPortal(@RequestBody Certification certification) {
        //查看验证码是否正确
        boolean isTrue = portalService.checkMailCode(certification.getEmail(), certification.getCode());
        if (!isTrue) return Result.create(StatusCode.CODE_ERROR, "验证码错误");
        //成功关联用户与门户
        authorService.bindUser(certification);

        return Result.create(200, "success");
    }

    /**
     * 发送邮箱验证
     * @param mail
     * @return
     */
    @PostMapping("/personal_center/academic_homepage/bind")
    public Result sendmail(String mail){
        if (!FormatUtil.checkMail(mail)) return Result.create(StatusCode.INFORMATION_ERROR, "邮箱格式错误");
        String redisMailCode = redisTemplate.opsForValue().get("MAIL_" + mail);
        if(redisMailCode!=null){
            return Result.create(200,"请稍后再发送");
        }else{
            portalService.sendMail(mail);
            return Result.create(200,"发送成功");
        }
    }
}
