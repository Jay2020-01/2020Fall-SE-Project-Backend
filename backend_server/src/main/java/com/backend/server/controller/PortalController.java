package com.backend.server.controller;

import com.backend.server.entity.Author;
import com.backend.server.entity.Paper;
import com.backend.server.entity.pojo.*;
import com.backend.server.service.AuthorService;
import com.backend.server.service.PaperService;
import com.backend.server.service.PortalService;
import com.backend.server.utils.FormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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


    /**
     * 查看作者信息
     * @param portal
     * @return
     */
    @PostMapping("/personal_center/academic_homepage/view")
    public Result checkPortal(@RequestBody Common portal) {
        List<Author> authors = authorService.queryAuthor(portal.getCommon());
        List<Paper> papers = paperService.queryPaperByAuthorId(portal.getCommon());
        List<Paper> paperReturn = new ArrayList<>();
        for (int i = 0; i < 5 && i < papers.size(); ++i) {
            Paper paper = papers.get(i);
            paperReturn.add(paper);
        }
        PortalReturn portalReturn = new PortalReturn(authors, paperReturn);
        return Result.create(200, "返回成功", paperReturn);
    }


    /**
     * 查看门户
     * @param common
     * @return
     */
    @PostMapping("/profile/view")
    public Result viewPortal(@RequestBody Common common) {
        List<Author> authors = authorService.queryAuthor(common.getCommon());
        List<Paper> papers = paperService.queryPaperByAuthorId(common.getCommon());
        PortalReturn portalReturn = new PortalReturn(authors, papers);
        return Result.create(200, "success", portalReturn);
    }

    /**
     * 修改门户信息
     * @param author
     * @return
     */
    @PostMapping("/profile/modify")
    public Result modifyPortal(@RequestBody Author author) {
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
    public Result sendmail(@RequestBody Common mail){
        if (!FormatUtil.checkMail(mail.getCommon())) return Result.create(StatusCode.INFORMATION_ERROR, "邮箱格式错误");
        String redisMailCode = redisTemplate.opsForValue().get("MAIL_" + mail.getCommon());
        if(redisMailCode!=null){
            return Result.create(200,"请稍后再发送");
        }else{
            portalService.sendMail(mail.getCommon());
            return Result.create(200,"发送成功");
        }
    }
}
