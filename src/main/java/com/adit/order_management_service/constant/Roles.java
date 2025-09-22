package com.adit.order_management_service.constant;

import lombok.Getter;

@Getter
public enum Roles {

    ADMIN(0, "ADMIN"),
    USER(1, "USER"),
    CLIENT(2, "CLIENT"),
    DELIVERY_AGENT(3, "DELIVERY_AGENT");

    private final Integer code;
    private final String desc;

    Roles(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getSpecificRoles(Integer code) {
        for (Roles roles : Roles.values()) {
            if (code == roles.ordinal()) {
                return roles.getDesc();
            }
        }
        return "Code not found!";
    }

}
