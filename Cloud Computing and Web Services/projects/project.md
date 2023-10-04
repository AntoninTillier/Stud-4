**Project**

**Introduction**

The University's Department of Computer Science offers Bachelor's (L1) to Master's (M2) programs in computer science. Since its establishment in 2001, each cohort has had 200,000 students per year (1,000,000 students in total each year). Each semester, every student must enroll in 100 mandatory courses (1,000 mandatory courses in total for 10 semesters from L1 to M2) and 100 elective courses from a pool of 500 (5,000 elective courses in total over 10 semesters). The information below pertains to students and courses when designing the database schema:

- Student
    - Student ID: A unique 10-digit student identification number, composed of 4 digits for the year and 6 formatted digits (e.g., 2010000001).
    - Cohort: The current student cohort, e.g., L1, M1, etc.
    - Last Name: Student's last name, e.g., Dupond.
    - First Name: Student's first name, e.g., Jean.
    - Date of Birth: Student's date of birth, e.g., 01/22/1990.
    - Email: Student's unique email address.
    - Phone Number: Student's 10-digit phone number.
    - Address: Student's residential address.

- Course
    - Course Code: A unique course identification composed of characters for the semester code, type, and course number. Mandatory courses are identified from A001 to A100, and elective courses from B001 to B500 (e.g., S03A001, S10B450, etc.).
    - Course Name: The course name, which may change over the years (e.g., UE S07A085 corresponded to the Compilation course between 2010 and 2013 but corresponds to the Code Generation course since 2014).
    - Instructors: The list of instructors teaching the course.

**Tasks**

This project aims to build a University Scholar Administration Web Service by providing REST API access to the academic database. For security reasons, this version only considers the GET method.

- To print transcripts by academic year. /asbu/students/{id}/transcripts/{program} The server will return a JSON string containing a data structure of type Map, for example (First for the first semester and Second for the second semester): {"Name":"Jean DUPOND","Email":"jean.dupond@univ-blois.fr","Program":"M1", "First":[{"Code":"S07A001","Name":"Big Data","Grade":"17.5"},{...},...], "Second":[{"Code":"S08A001","Name":"Database","Grade":"6.25"},{...},...]} If the student or the year does not exist, or the year is ongoing, the server will return a NOT FOUND error.

- To find the success rate of a semester (from 1 to 10) based on the academic year. /asbu/rates/{semester} The server will return a JSON string containing a single number, for example (0.88 for 88%): [{"Year":"2001","Rate":"0.88"},{"Year":"2002","Rate":"0.83"},...] If the semester does not exist, the server will return a NOT FOUND error.

- To determine the success rate of a course since its creation, considering its different names. /asbu/courses/{id}/rates The server will return a JSON string containing a single number, for example (0.88 for 88%): [{"Name":"HPC","Rate":"0.183"},{"Name":"Big Data","Rate":"0.88"},...] If the course does not exist, the server will return a NOT FOUND error.

- More specifically, to find the success rate of a course for an academic year. /asbu/courses/{id}/rates/{year} The server will return a JSON string containing a single number, for example (0.88 for 88%): {"Name":"Big Data","Rate":"0.88"} If the course or the year does not exist, or the year is ongoing, the server will return a NOT FOUND error.

- To determine the average grades of all courses in a cohort for an academic year. /asbu/programs/{program}/means/{year} The server will return a JSON string containing a data structure of type Map, for example: {"S07A001":{"Name":"Big Data","Grade":"17.5"},...,"S07B001":...,... "S08A001":{"Name":"Database","Grade":"6.25"},...,"S08B001":...,...} If the cohort or the year does not exist, or the year is ongoing, the server will return a NOT FOUND error.

- To find the success rates of all courses taught by an instructor. /asbu/instructors/{name}/rates The server will return a JSON string containing a data structure of type Map, for example: {"S07A001/2017":{"Name":"Big Data","Rate":"0.99"}, "S09A003/2016":{"Name":"Data Mining","Rate":"0.19"},...} If the instructor does not exist, the server will return a NOT FOUND error.

- To determine the ranking of students based on their average grades by cohort and academic year. /asbu/ranks/{program}/years/{year} The server will return a JSON string containing a data structure of type Map, for example: {"2008000085":"19.5","2008000031":"18",...,"2008000099":"3"} If the cohort or the year does not exist, or the year is ongoing, the server will return a NOT FOUND error.