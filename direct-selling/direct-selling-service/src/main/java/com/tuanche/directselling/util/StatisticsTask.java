package com.tuanche.directselling.util;

import java.lang.reflect.InvocationTargetException;

public interface StatisticsTask {
    void runTask() throws InvocationTargetException, IllegalAccessException, InterruptedException;
}
