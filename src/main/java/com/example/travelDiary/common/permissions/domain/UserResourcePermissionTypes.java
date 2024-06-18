package com.example.travelDiary.common.permissions.domain;

import lombok.Getter;

@Getter
public enum UserResourcePermissionTypes {
    VIEW(1),
    CLONE(2),
    EDIT(3),
    OWNER(4);

    private final int level;

    UserResourcePermissionTypes(int level) {
        this.level = level;
    }

    public boolean hasHigherOrEqualPermission(UserResourcePermissionTypes other) {
        return this.level >= other.level;
    }
}

