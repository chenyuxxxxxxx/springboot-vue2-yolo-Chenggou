package com.haut.attendance.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.haut.common.domain.Student;

public interface AttendanceService {
    /*
        教师发布一条记录
     */
    BigInteger releaseSignIn(int courseId, Date start);
    /*
        学生签到
     */
    String studentSignIn(Map<String, Object> params);

    List<Student> getAbsentStudents(Integer signID);
}
