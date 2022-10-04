package Util;

import Entity.EntityObject;

import java.util.List;
import java.util.stream.Collectors;

public class GeneralUtil {
    public static <T> List<T> castObjectInList(List<EntityObject>list, Class<T>classToBeCasted) {
        return list.stream()
                .map(classToBeCasted::cast)
                .collect(Collectors.toList());
    }
}
