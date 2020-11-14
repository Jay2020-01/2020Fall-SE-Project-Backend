package com.backend.portal.service;

import com.backend.portal.entity.Certification;
import com.backend.portal.entity.Portal;
import com.backend.portal.mapper.PortalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PortalService {
    @Autowired
    private PortalMapper portalMapper;


    public Portal selectById(Integer portalId) {
        Portal portal = portalMapper.selectById(portalId);
        return portal;
    }

    public int updateById(Portal portal) {
        int result = portalMapper.updateById(portal);
        return result;
    }

    public boolean checkInformation(Certification certification) {
        Portal portal = portalMapper.selectById(certification.getPortalId());
        if (portal.getExpertName().equals(certification.getExpertName())) {
            //检查邮箱格式是否正确


            String[] cer = certification.getEmail().split("@");
            String[] ori = portal.getEmail().split("@");
            if (cer[1].equals(ori[1])) return true;
        }
        return false;
    }

    public void relateUserToPortal(Certification certification) {
        Portal portal = portalMapper.selectById(certification.getPortalId());
        portal.setUserId(certification.getUserId());
        portalMapper.updateById(portal);
    }
}
