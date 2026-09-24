package gym_management_backend.gymmanagement.security;


import gym_management_backend.gymmanagement.entity.AuditAction;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {

    AuditAction action();

    String entityType();
}