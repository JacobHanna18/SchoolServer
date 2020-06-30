package server.clientClasses;

public enum Operation {
    // login user
    // (userID, password) -> clientUser
    logIn

    //get list of all teachers
    //PRINCIPLE : () -> [clientUser]
    ,teacherList

    //get all students
    //PRINCIPLE : () -> [clientUser]
    ,studentList

    //get grades of student
    //STUDENT : () -> [clientGrade]
    //OTHER : (studentID) -> [clientGrade]
    ,gradesList

    //get all subjects
    //ALL : () -> [clientSubject]
    ,subjectList

    //get questions of subject
    //TEACHER/PRINCIPLE : (subjectID) -> [clientQuestion]
    ,questionList

    //get exams of subject
    //TEACHER/PRINCIPLE : (subjectID) -> clientExam
    ,examList

    //get questions of teacher
    //TEACHER/PRINCIPLE : (teacherID) -> [clientQuestion]
    ,questionByTeacher

    //`get exams of teacher`
    //TEACHER/PRINCIPLE : (teacherID) -> [clientExam]
    ,examByTeacher

    //get exam by id
    //TEACHER/PRINCIPLE : (examID) -> clientExam
    ,getExam

    // get exam by course id
    //TEACHER/PRINCIPLE : (courseID) -> clientExam
    ,courseExam

    //Select exam from course
    //TEACHER : (courseID) -> void
    ,selectExamForCourse

    // set access code duration online
    //TECAHER : (courseID,AccessCode,duration,online) -> void
    ,startExam

    // create exam from question
    //TEACHER : ([clientQuestion],subjectID) -> void
    ,createExam

    // create question for subject
    //TEACHER : (clientQuestion,subjectID)  -> void
    ,createQuestion

    // get students list in course
    // TEACHER/PRINCIPLE : courseID -> [clientUser]
    ,studentsFromCourse

    // courses list by subject and teacher
    // TEACHER/PRINCIPLE : (subjectID, teacherID) -> [clientCourse]
    ,coursesFromSubjectAndTeacher

    // new request for principle to add time
    // TEACHER : (addedTime, exp) -> void
    ,newRequest

    // confirm student grade
    // TEACHER : (studentID, courseID) -> void
    ,confirmGrade

    // change and confirm student grade
    // TEACHER : (studentID, courseID, newGrade, reason) -> void
    ,changeAndConfirmGrade

    // get grade from student in course
    // STUDENT : (courseID) -> clientGrade
    // TEACHER/PRINCIPLE : (studentID, courseID) -> clientGrade
    ,getGrade

    // get all grades in course
    // TEACHER/PRINCIPLE : (courseID) -> [clientGrade]
    ,getGradesOfCourse

    // take exam for student
    // STUDENT : (AccessCode)-> clientExam
    ,takeExam

    // save student answers for exam
    // STUDENT : (arr, courseID) -> void
    ,submitOnlineExam

    // get all requests
    // PRINCIPLE : () -> [clientRequest]
    ,requestList

    // decide if yes or no
    // PRINCIPLE : (requestID, accept) -> void
    ,decideRequest

    //submit manual exam
    //STUDENT : (file, courseID) -> void
    ,submitManualExam

    //download exam of student (manual)
    //STUDENT : (courseID) -> clientExam
    //TEACHER/PRINCIPLE : (courseID, studentID) -> clientExam
    ,downloadManualExam

    //get teacher exams of course
    //TEACHER : () -> [clientExam]
    //PRINCIPLE : (teacherID) -> [clientExam]
    ,subjectTeacherExamList

    // get all grades in course
    // TEACHER/PRINCIPLE : (teacherID) -> [clientGrade]
    ,getGradesOfTeacher

    // courses list by subject
    // TEACHER/PRINCIPLE : (subjectID) -> [clientCourse]
    ,coursesOfSubject
}
