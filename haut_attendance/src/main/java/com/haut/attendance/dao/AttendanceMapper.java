package com.haut.attendance.dao;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import com.haut.common.domain.Student;

@Mapper
public interface AttendanceMapper {

    /*
        插入一条签到记录
     */
    @Insert("insert into haut_sign (courseID, signEndTime) values (#{courseID},  #{endTime})")
    @Options(useGeneratedKeys = true, keyColumn = "signID", keyProperty = "id")
    boolean insert(Map<String, Object> params);

    /*
        学生签到
     */
    @Select("SELECT * FROM haut_sign WHERE signID = #{signID} AND courseID = #{courseID}")
    Map<String, Object> getSignDetails(@Param("signID") Integer signID, @Param("courseID") Integer courseID);


    @Insert("INSERT INTO haut_attendance (StudentID, CourseID, AttendanceDate, Status) " +
            "VALUES (#{StudentID}, #{CourseID}, NOW(), #{Status})")
    boolean insertStudentSignRecord(Map<String, Object> params);

    /*
        未签到学生查询
     */
    @Select("SELECT * " +
            "FROM haut_student_courses hsc " +
            "INNER JOIN haut_student hs ON hsc.StudentID = hs.student_id " +
            "WHERE hsc.CourseID = (SELECT courseID FROM haut_sign WHERE signID = #{signID}) " +
            "AND NOT EXISTS ( " +
            "    SELECT 1 FROM haut_attendance ha " +
            "    WHERE ha.StudentID = hsc.StudentID " +
            "    AND ha.CourseID = hsc.CourseID " +
            "    AND ha.AttendanceDate <= (SELECT signEndTime FROM haut_sign WHERE signID = #{signID}) " +
            "    AND ha.Status = 1" +
            ")")
    @Results({
//            @Result(property = "student_id", column = "student_id"),
//            @Result(property = "name", column = "name"),
           @Result(property = "class_", column = "class"),
//            @Result(property = "grade", column = "grade"),
//            @Result(property = "photo_url", column = "photo_url"),
    })
    List<Student> findAbsentStudents(@Param("signID") Integer signID);

}
