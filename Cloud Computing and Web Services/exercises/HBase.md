**HBase Schema Implementation**

**1 Schema Design**

We focus on implementing the HBase schema for Administration Intelligence Service for University project. Notice that each group of the project should use a member’s student ID as the namespace1 in order to better organize the concerned tables.

The Student table S (for Student) stores personal information of all students University that has only the Computer Science department. This table contains a required column family # and a contact column family C (for Contact) containing the following columns:
- #:F - First name of the student.
- #:L - Last name of the student.
- #:P - Program of the student, from 1 (L1) to 5 (M2).
- C:B - Birth date of the student, for instance 12/01/1996 or 1996-01-12. • C:D - Domicile address of the student.
- C:E - E-mail address of the student.
- C:P - Phone number of the student.

A unique student ID with 10 digits (YYYYNNNNNN) is considered as row key, where YYYY is the year of entrance and NNNNNN is a serial ID, for instance 2008000123.

The Course table C (for Course) stores course information including course name and instructor list (one course can be instructed by several instructors). This table contains a required column family # and an instructor column family I (for Instructor) with the following columns:
- #:N - Course name, for instance Big Data and Web Services.
- I:1 - Name of instructor 1 (required).
- I:2 - Name of instructor 2. 
- I:n - Name of instructor n.

A course ID can be redistributed to different courses with respect to different years, so we consider a unique course ID associated with a year as row key, for instance S01A007/2015.

The Instructor table I (for Instructor) stores all courses instructed by each instructor. Indeed, this table is considered as a helper table in order to make instructor related statistical tasks efficient. This table contains only one column family # (for required information) with numbers as columns and with course IDs as values:
-  #:1 - Course 1 instructed by the instructor (required). 
-  #:2 - Course 2 instructed by the instructor.
-  #:n - Course n instructed by the instructor.

Since the course list of each instructor is organized by year, we use NAME/YYYY as row key where NAME is the instructor’s name (can contain space but cannot contain /) and YYYY is the concerned year, for instance Roberto Elego/2015.

The Grade table G (for Grade) stores all student grades that is quite simple but will be filled by a huge increasing number of rows in considering the number of students and the number of courses per student. This table contains only one column 
- #:G in the required column family for the grade of a course of a student.

The row key of this table is consisted of different parts and is organized by year, semester, student and course, for instance 2015/S07/2012000123/S07A006.

We first create the following HBase tables and then fill these tables by synthetic data.