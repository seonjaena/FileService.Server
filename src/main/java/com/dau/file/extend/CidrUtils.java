package com.dau.file.extend;

import org.apache.commons.net.util.SubnetUtils;

public class CidrUtils {

    public static boolean isInRange(String cidr, String ipAddress) {
        SubnetUtils utils = new SubnetUtils(cidr);
        utils.setInclusiveHostCount(true);
        return utils.getInfo().isInRange(ipAddress);
    }

}
