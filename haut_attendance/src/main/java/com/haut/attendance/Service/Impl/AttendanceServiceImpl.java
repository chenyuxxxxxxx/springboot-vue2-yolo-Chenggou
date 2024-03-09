package com.haut.attendance.Service.Impl;


import com.haut.attendance.Service.AttendanceService;
import com.haut.attendance.dao.AttendanceMapper;
import com.haut.common.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceMapper attendanceMapper;

    @Autowired
    private SimpleDateFormat formatter;

    @Override
    public BigInteger releaseSignIn(int courseId, Date start) {
        Map<String, Object> mp = new HashMap<>();
        mp.put("courseID", courseId);
        mp.put("endTime", formatter.format(start));

        if(!attendanceMapper.insert(mp)){
            return BigInteger.valueOf(0);
        }
        return (BigInteger)mp.get("id");
    }
    @Override
    public String studentSignIn(Map<String, Object> params) {
        Integer signID = (Integer) params.get("signID");
        Integer courseID = (Integer) params.get("CourseID");
        Map<String, Object> signDetails = attendanceMapper.getSignDetails(signID, courseID);

        if (signDetails == null  || signDetails.isEmpty()){
            return "签到会话不存在";
        }

//        Date currentTime = new Date();
//        Date signEndTime = (Date) signDetails.get("signEndTime");
        LocalDateTime localDateTimeEndTime = (LocalDateTime) signDetails.get("signEndTime");
        Date signEndTime = Date.from(localDateTimeEndTime.atZone(ZoneId.systemDefault()).toInstant());

        Date currentTime = new Date();

        if (currentTime.before(signEndTime)) {
            params.put("Status", 1);
            boolean inserted = attendanceMapper.insertStudentSignRecord(params);
            return inserted ? "签到成功" : "签到失败";
        } else {
            return "签到失败，已超过签到时间";
        }
    }

    public List<Student> getAbsentStudents(Integer signID) {
        return attendanceMapper.findAbsentStudents(signID);
    }
}
