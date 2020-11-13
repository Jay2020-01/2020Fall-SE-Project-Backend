package com.backend.portal.service;

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

    public Portal testController() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("portal_id", 1);
        List<Portal> portals = portalMapper.selectByMap(columnMap);
        return portals.get(0);
    }
}
