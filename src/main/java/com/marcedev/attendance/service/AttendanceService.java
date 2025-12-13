package com.marcedev.attendance.service;

import com.marcedev.attendance.dto.AttendanceDTO;
import com.marcedev.attendance.dto.AttendanceMarkDTO;
import com.marcedev.attendance.dto.CourseMonthlyAttendanceDTO;
import com.marcedev.attendance.entities.ClassSession;

import java.util.List;
import java.util.Map;

public interface AttendanceService {

    AttendanceDTO save(AttendanceDTO dto);

    void registerAttendance(Long sessionId, List<AttendanceMarkDTO> attendances);

    void registerAttendanceByCourse(Long courseId, Map<Long, Boolean> attendanceMap);

    List<AttendanceDTO> findAll();

    List<AttendanceDTO> findByClassId(Long classId);

    List<AttendanceDTO> findByCourseId(Long courseId);

    AttendanceDTO findById(Long id);

    void deleteById(Long id);

    ClassSession getOrCreateTodaySession(Long courseId);

    List<CourseMonthlyAttendanceDTO> getCourseMonthlyStats(Long courseId, int month, int year);
}
