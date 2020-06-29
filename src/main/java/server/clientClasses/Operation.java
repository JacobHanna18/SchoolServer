package server.clientClasses;

public enum Operation {
    logIn // login user -> clientUser
    ,teacherList //get list of all teachers -> [clientUser]
    ,studentList //get all students -> [clientUser]
    ,gradesList //get grades of student -> [clientGrade]
    ,subjectList //get all subjects -> [clientSubject]
    ,questionList //get questions of course -> [clientQuestion]
    ,examList //get exams of course -> clientExam
    ,questionByTeacher //get questions of teacher -> [clientQuestion]
    ,examByTeacher //get exams of teacher -> [clientExam]
    ,getExam //get exam by id -> clientExam
    ,courseExam // get exam by course id -> clientExam
    ,selectExamForCourse // -> void
    ,startExam // set access code duration online -> void
    ,createExam // create exam from question -> void
    ,createQuestion // create question for subject -> void
    ,studentsFromCourse // get students list in course -> [clientUser]
    ,coursesFromSubjectAndTeacher // courses list by subject and teacher -> [clientCourse]
    ,newRequest // new request for principle to add time -> void
    ,confirmGrade // confirm student grade -> void
    ,changeAndConfirmGrade // change and confirm student grade -> void
    ,getGrade // get grade from student in course -> clientGrade
    ,getGradesOfCourse // get all grades in course -> [clientGrade]
    ,takeExam // take exam for student -> clientExam
    ,submitOnlineExam // save student answers for exam -> void
    ,requestList // get all requests -> [clientRequest]
    ,decideRequest // decide if yes or no -> void


}
