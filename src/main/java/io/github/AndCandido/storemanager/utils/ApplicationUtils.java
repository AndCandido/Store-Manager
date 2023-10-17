package io.github.AndCandido.storemanager.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class ApplicationUtils {

    public static void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullProperties(source));
    }

    public static String[] getNullProperties(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertiesDescriptor = src.getPropertyDescriptors();

        Set<String> emptyProps = new HashSet<>();

        for (PropertyDescriptor property : propertiesDescriptor) {
             Object objSource = src.getPropertyValue(property.getName());
             if(objSource == null) {
                 emptyProps.add(property.getName());
             }
        }
        String[] result = new String[emptyProps.size()];
        return emptyProps.toArray(result);
    }
}
