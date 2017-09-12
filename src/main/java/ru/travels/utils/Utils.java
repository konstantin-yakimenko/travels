package ru.travels.utils;

import com.google.common.base.Strings;
import org.restexpress.exception.BadRequestException;
import org.restexpress.exception.NotFoundException;

public class Utils {

    public static Long toLong(final Object o) {
        if (o instanceof Long) {
            return (Long) o;
        } else if (o instanceof Integer) {
            return ((Integer)o).longValue();
        } else if (o instanceof String) {
            return Long.parseLong((String)o);
        }
        return (Long)o;
    }

    public static Integer toInt(final Object o) {
        if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof Long) {
            return ((Long)o).intValue();
        } else if (o instanceof String) {
            return Integer.parseInt((String)o);
        }
        return (Integer) o;
    }

    public static Long toLongEx(final String s) {
        try {
            return s == null || s.isEmpty() ? null : Long.parseLong(s);
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }

    public static Integer toIntEx(final String s) {
        try {
            return s == null || s.isEmpty() ? null : Integer.parseInt(s);
        } catch (Exception e) {
            throw new BadRequestException();
        }
    }

    public static Integer castToInt(Object id) throws NotFoundException {
        try {
            return Integer.parseInt((String) id);
        } catch (Throwable e) {
            throw new NotFoundException();
        }
    }

}
