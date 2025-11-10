package com.example.homework03_program1;

public class SessionData {
    private static Student activeStudent;

    public static Student getActiveStudent() {
        return activeStudent;
    }

    public static void setActiveStudent(Student activeStudent) {
        SessionData.activeStudent = activeStudent;
    }

    public static String majorFormat(Major major) {
        if (major.getId() == 0)
            return major.getName();
        return major.getName() + " (" + major.getPrefix() + ")";
    }
}
