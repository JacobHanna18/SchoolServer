package server.clientClasses;

public enum Operation {
    logIn // login user // (userID, password) -> clientUser
    ,teacherList //get list of all teachers // () -> [clientUser]
    ,studentList //get all students // () -> [clientUser]
    ,gradesList //get grades of student // (studentID) -> [clientGrade]
    ,subjectList //get all subjects // () -> [clientSubject]
    ,questionList //get questions of subject // (subjectID) -> [clientQuestion]
    ,examList //get exams of subject // (subjectID) -> clientExam
    ,questionByTeacher //get questions of teacher // (teacherID) -> [clientQuestion]
    ,examByTeacher //get exams of teacher // (teacherID) -> [clientExam]
    ,getExam //get exam by id // (examID) -> clientExam
    ,courseExam // get exam by course id // (courseID) -> clientExam
    ,selectExamForCourse // (courseID) -> void
    ,startExam // set access code duration online // (courseID,AccessCode,duration,online) -> void
    ,createExam // create exam from question // ([clientQuestion],subjectID,teacherID) -> void
    ,createQuestion // create question for subject //(clientQuestion,subjectID,teacherID)  -> void
    ,studentsFromCourse // get students list in course // courseID -> [clientUser]
    ,coursesFromSubjectAndTeacher // courses list by subject and teacher // (subjectID, teacherID) -> [clientCourse]
    ,newRequest // new request for principle to add time // (addedTime, exp) -> void
    ,confirmGrade // confirm student grade //(studentID, courseID) -> void
    ,changeAndConfirmGrade // change and confirm student grade // (studentID, courseID, newGrade, reason) -> void
    ,getGrade // get grade from student in course //(studentID, courseID) -> clientGrade
    ,getGradesOfCourse // get all grades in course // (courseID) -> [clientGrade]
    ,takeExam // take exam for student // (AccessCode, studentID)-> clientExam
    ,submitOnlineExam // save student answers for exam //(arr, courseID, studentID) -> void
    ,requestList // get all requests // () -> [clientRequest]
    ,decideRequest // decide if yes or no // (requestID, accept) -> void


}
