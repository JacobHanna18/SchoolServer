package server.clientClasses;

public enum Operation {
    logIn // login user
    ,teacherList //get list of all teachers
    ,studentList //get all students
    ,gradesList //get grades of student
    ,subjectList //get all subjects
    ,questionList //get questions of course
    ,examList //get exams of course
    ,questionByTeacher //get questions of teacher
    ,examByTeacher //get exams of teacher
    ,getExam //get exam by id
    ,courseExam // get exam by course id
    ,selectExamForCourse //
    ,startExam // set access code duration online
    ,createExam // create exam from question
    ,createQuestion // create question for subject
    ,studentsFromCourse // get students list in course
    ,coursesFromSubjectAndTeacher // courses list by subject and teacher
    ,newRequest // new request for principle to add time
    ,confirmGrade // confirm student grade
    ,changeAndConfirmGrade // change and confirm student grade
    ,getGrade // get grade from student in course
    ,getGradesOfCourse // get all grades in course
    ,takeExam // take exam for student
    ,submitOnlineExam // save student answers for exam
    ,requestList // get all requests
    ,decideRequest // decide if yes or no


}
