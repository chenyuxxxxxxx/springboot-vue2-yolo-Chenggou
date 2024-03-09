package com.haut.attendance.Controller;

import com.haut.attendance.Service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.haut.common.domain.Student;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestParam Integer studentID, @RequestParam Integer courseID, @RequestParam Integer signID) {

        Map<String, Object> params = new HashMap<>();
        params.put("StudentID", studentID);
        params.put("CourseID", courseID);
        params.put("signID", signID);

        String result = attendanceService.studentSignIn(params);

        if ("签到成功".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/publish")
    public ResponseEntity<?> publishSignIn(@RequestParam Integer courseId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {

        Map<String, Object> params = new HashMap<>();
        params.put("courseID", courseId);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("endTime", formatter.format(endTime));

        BigInteger signId = attendanceService.releaseSignIn(courseId, endTime);

        if (signId.equals(BigInteger.ZERO)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("签到发布失败");
        } else {
            return ResponseEntity.ok().body("签到发布成功");
        }
    }

    @GetMapping("/absentStudents")
    public ResponseEntity<List<Student>> getAbsentStudents(@RequestParam Integer signID) {
        List<Student> absentStudents = attendanceService.getAbsentStudents(signID);
        if (absentStudents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(absentStudents);
    }
}

