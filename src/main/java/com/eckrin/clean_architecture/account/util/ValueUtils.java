package com.eckrin.clean_architecture.account.util;

public class ValueUtils {
    
    public static void requireNonNull(Object obj) {
        if(obj==null) throw new RuntimeException("Require Nonnull Value");
    }
    
    public static void requireGreaterOrEqualThan(Long target, Long std) {
        if(target<std) throw new RuntimeException("Target value is smaller than std");
    }
}
