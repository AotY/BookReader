package com.koolearn.android.util;

public class LogInfo {
    static boolean flag = false;

    public static void I(String info) {
        if (flag) {
            final StackTraceElement[] stack = new Throwable().getStackTrace();
            final int i = 1;
//            for (int id = 0; id < stack.length; id++) {
            final StackTraceElement ste = stack[i];
            android.util.Log.i("yul1_log_", String.format("[%s][%s]%s[%s]",
                    ste.getFileName(), ste.getMethodName(), ste.getLineNumber(), info));
//            }
        }
    }

    public static void I1(String info) {
        if (flag) {
            final StackTraceElement[] stack = new Throwable().getStackTrace();
            final int i = 1;
//            for (int id = 0; id < stack.length; id++) {
            final StackTraceElement ste = stack[i];
            android.util.Log.i("yul2_log_", String.format("[%s][%s]%s[%s]",
                    ste.getFileName(), ste.getMethodName(), ste.getLineNumber(), info));
//            }
        }
    }

    public static void I3(String info) {
        if (flag) {
            final StackTraceElement[] stack = new Throwable().getStackTrace();
            final int i = 1;
//            for (int id = 0; id < stack.length; id++) {
            final StackTraceElement ste = stack[i];
            android.util.Log.i("yul2_log_", String.format("[%s][%s]%s[%s]",
                    ste.getFileName(), ste.getMethodName(), ste.getLineNumber(), info));
//            }
        }
    }

    public static void i(String info) {
        if (flag) {
            final StackTraceElement[] stack = new Throwable().getStackTrace();
            final int i = 1;
//            for (int id = 0; id < stack.length; id++) {
            final StackTraceElement ste = stack[i];
            android.util.Log.i("yul2_log_", String.format("[%s][%s]%s[%s]",
                    ste.getFileName(), ste.getMethodName(), ste.getLineNumber(), info));
//            }
        }
    }
}
