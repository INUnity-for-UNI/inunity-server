package com.inu.inunity.security;

public enum Department {
    COMPUTER_SCIENCE("컴퓨터공학부"),
    INFORMATION_COMMUNICATION("정보통신공학과"),
    EMBEDDED_SYSTEM("임베디드시스템공학과");

    private final String name;

    Department(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 학과 이름으로 Enum 매핑
    public static boolean isValidDepartment(String departmentName) {
        for (Department department : Department.values()) {
            if (department.getName().equals(departmentName)) {
                return true;
            }
        }
        return false;
    }
}