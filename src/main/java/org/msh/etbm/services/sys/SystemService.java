package org.msh.etbm.services.sys;

import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 16/8/15.
 */
@Service
public class SystemService {

    public SystemInformation getInformation() {
        SystemInformation inf = new SystemInformation();

        inf.setState(SystemInformation.SystemState.NEW);
        return inf;
    }
}
