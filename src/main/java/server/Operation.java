package server;

public enum Operation {
    LogIn //log in user into system
    //userid, password -> student/teacher/principle
    ,StudentGrades //view grades of student
    ,StudentExamFile //view exam file
    ,GetOnlineExam //get online exam
    ,DownloadExam //Download manual exam file
    ,UploadExam // upload exam file
    ,FinishExam //student finished online exam
    ,SubjectList //get subjects of teacher
    ,ExamList //get exams of subject
    ,QuestionList //get questions of subject
    ,FinishedExams //get finished exams of subject
    ,CourseList //get course list of teacher
    ,CourseExam //get the course exam
    ,SelectExam //select exam of the course
    ,CourseGrades //get the grades of the students in the course
    ,ChangeGrade //change the grade of the student in the course
    ,StudentAnswers //get the answers of the student in the course
    ,AddTime //add time to the course exam
    ,TeacherQuestion //get the questions that the teacher wrote
    ,TeacherExams //get the exams that the teacher created
    ,TeacherCoursesAverages //get the averages of all courses from the selected teacher
    ,StudentGradesAverages //get the averages of all students
    ,RequestList //get the request list
    ,AceeptRequest //accept the time change request
}
